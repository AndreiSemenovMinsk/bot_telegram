 package ru.skidoz.service.command_impl.action;

 import java.util.Collections;
 import java.util.Map;


 import ru.skidoz.model.entity.category.LanguageEnum;
 import ru.skidoz.model.pojo.main.Action;
 import ru.skidoz.model.pojo.side.Shop;
 import ru.skidoz.model.pojo.telegram.*;
 import ru.skidoz.aop.repo.ActionCacheRepository;
 import ru.skidoz.aop.repo.LevelCacheRepository;
 import ru.skidoz.aop.repo.ShopCacheRepository;
 import ru.skidoz.service.initializers.InitialLevel;
 import ru.skidoz.service.command.Command;
 import ru.skidoz.util.MenuTypeEnum;
 import ru.skidoz.util.Structures;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 import org.telegram.telegrambots.meta.api.objects.Update;

 import static ru.skidoz.service.command.CommandName.*;

/**
 * @author andrey.semenov
 */
@Component
public class AddActionSource implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    //@Transactional
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("**************AddActionSource**************");
        System.out.println();
        System.out.println("level.getCallName()--------------------------" + level.getCallName());
        System.out.println();
        System.out.println();

        if (update.getCallbackQuery() != null && update.getCallbackQuery().getData().startsWith("@" + MenuTypeEnum.LEVEL_CHOICER)) {

            String code = update.getCallbackQuery().getData().substring(("@" + MenuTypeEnum.LEVEL_CHOICER).length()+20);

            System.out.println("code***" + code);
            Integer id = null;
            if (!CATEGORY_SUPER_GROUP.name().equals(code)) {
                id = Structures.parseInt(code);
            }

            /*
            AbstractGroupEntity abstractGroupEntity = null;
            if (!code.equals(CATEGORY_SUPER_GROUP.name())){
                Long id = Structures.parseLong(code);
                abstractGroupEntity = abstractGroupEntityRepository.findById(id).orElse(null);;
            }*/

            Long chatId = users.getChatId();
            Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
            Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());


            LevelDTOWrapper resultLevel = null;
            if (level.getCallName().equals(ADD_ACTION_RATE_SOURCE.name())) {

                action.setProductSource(id/*abstractGroupEntity*/);

                resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_ACTION_RATE_TARGET,
                        true,
                        true);
            } else if (level.getCallName().equals(ADD_ACTION_COUPON_SOURCE.name())) {

                action.setProductSource(id/*abstractGroupEntity*/);
                //resultLevel = initialLevel.level_ADD_ACTION_COUPON_TARGET
                resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_ACTION_COUPON_TARGET,
                        true,
                        true);
            } else if (level.getCallName().equals(ADD_ACTION_LINK_SOURCE.name())) {

                action.setProductSource(id/*abstractGroupEntity*/);

                resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_ACTION_LINK_TARGET,
                        true,
                        true);
            } else if (level.getCallName().equals(ADD_ACTION_RATE_TARGET.name())
                    || level.getCallName().equals(ADD_ACTION_COUPON_TARGET.name())
                    || level.getCallName().equals(ADD_ACTION_LINK_TARGET.name())) {

                resultLevel = initialLevel.convertToLevel(initialLevel.level_ACTIONS,
                        true,
                        true);

                resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Акция сохранена!")));

                action.setProductTarget(id/*abstractGroupEntity*/);
                action.setActive(true);
            }

            actionCacheRepository.save(action);

            LevelDTOWrapper finalResultLevel = resultLevel;
            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(chatId);
                e.setUser(users);
                e.setLevel(finalResultLevel);
            })), null, null);

        } else if (update.getMessage() != null) {

            ///ADD_ACTION_LINK_TARGE
            System.out.println("level.getCallName()-+++-------" + level.getCallName());
            System.out.println("ADD_ACTION_RATE_SOURCE.name()-" + ADD_ACTION_RATE_SOURCE.name());

            Long chatId = users.getChatId();
            LevelDTOWrapper resultLevel = null;

            if (level.getCallName().equals(ADD_ACTION_RATE_SOURCE.name())) {

                resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_ACTION_RATE_SOURCE,
                        true,
                        true);

                String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

                System.out.println();
                System.out.println("**************++++++++++++++**************");
                System.out.println();
                System.out.println(level.getCallName());
                System.out.println(inputText);

                Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

                try {
                    Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());
                    action.setFriendFuturePurchaseRate(Integer.parseInt(inputText));
                    actionCacheRepository.save(action);
                } catch (NumberFormatException formatException) {
                    resultLevel = initialLevel.convertToLevel(levelCacheRepository.findById(level.getParentLevelId()),
                            true,
                            true);

                    resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
                }
            } else if (level.getCallName().equals(ADD_ACTION_COUPON_SOURCE.name())) {

                //resultLevel = initialLevel.level_ADD_ACTION_COUPON_SOURCE;
                resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_ACTION_COUPON_SOURCE,
                        true,
                        true);

                String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

                Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

                System.out.println("shopInitiator-------" + shopInitiator);

                try {
                    Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());

                    System.out.println("action-------" + action);

                    action.setFuturePurchaseRate(Integer.parseInt(inputText));
                    actionCacheRepository.save(action);

                    System.out.println("action+++++++" + action);

                } catch (NumberFormatException formatException){

                    resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));

                    System.out.println(formatException);
                }
            }

            LevelDTOWrapper finalResultLevel = resultLevel;
            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(chatId);
                e.setUser(users);
                e.setLevel(finalResultLevel);
            })), null, null);
        }

        return null;
    }
}
