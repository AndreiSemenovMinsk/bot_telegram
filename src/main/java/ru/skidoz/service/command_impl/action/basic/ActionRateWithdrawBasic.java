package ru.skidoz.service.command_impl.action.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class ActionRateWithdrawBasic implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        Long chatId = users.getChatId();

        LevelDTOWrapper resultLevel = null;

        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);


        System.out.println("shopInitiator.getCurrentCreatingAction()+ +++++++ " + shopInitiator.getCurrentCreatingAction());

        Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());

        System.out.println();
        System.out.println("*****************************ActionRateWithdrawBasic************************");
        System.out.println();
        System.out.println();

        if (update.getMessage() != null) {
            try {
                String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

                resultLevel = initialLevel.convertToLevel(initialLevel.level_ACTION_RATE_WITHDRAW_BASIC,
                        true,
                        true);

                System.out.println("action+++++" + action);
                System.out.println(Integer.parseInt(inputText) + " *********** ");

                action.addRatePreviousPurchase(Integer.parseInt(inputText));

                System.out.println("inputText**************" + Integer.parseInt(inputText));

                actionCacheRepository.save(action);
            } catch (NumberFormatException formatException) {

                System.out.println("ActionRateWithdrawBasic********************" + formatException.getMessage());

                resultLevel = initialLevel.convertToLevel(initialLevel.level_ONE_LEVEL_RATE_BASIC,
                        true,
                        false);

                resultLevel.addMessage(new Message(null, 1, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
            }
        }

        if (resultLevel == null){
            return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(chatId);
                e.setUser(users);
                e.setLevel(initialLevel.convertToLevel(level,
                        true,
                        false));
            })));
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}
