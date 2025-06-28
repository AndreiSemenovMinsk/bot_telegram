package ru.skidoz.service.command_impl.action.basic;

import java.util.Collections;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class MultiLevelQuestionBasic implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        Long chatId = users.getChatId();

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_MULTI_LEVEL_QUESTION_BASIC,
                true,
                true);

        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());

        System.out.println();
        System.out.println("********************MultiLevelQuestionBasic******************");
        System.out.println();
        System.out.println();

        if (update.getMessage() != null) {
            try {

                String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

                action.addRatePreviousPurchase(Integer.parseInt(inputText));

                System.out.println("inputText**************" + Integer.parseInt(inputText));

                actionCacheRepository.save(action);
            } catch (NumberFormatException formatException) {

                System.out.println("MultiLevelRate********************" + formatException.getMessage());

                resultLevel = initialLevel.convertToLevel(initialLevel.level_MULTI_LEVEL_RATE_BASIC,
                        true,
                        false);

                resultLevel.addMessage(new Message( null, 1, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
            }
        }


        LevelDTOWrapper finalResultLevel = resultLevel;
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })), null, null);
    }
}
