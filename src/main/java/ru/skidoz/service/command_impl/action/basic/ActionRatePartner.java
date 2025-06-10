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
public class ActionRatePartner implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        Long chatId = users.getChatId();
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_ACTION_RATE_PARTNER,
                true,
                false);

        System.out.println();
        System.out.println("**************ActionRatePartner**************");
        System.out.println();
        System.out.println(level.getCallName());

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

        System.out.println(inputText + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        try {
            Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());
            action.setFuturePurchaseRate(Integer.parseInt(inputText));
            actionCacheRepository.save(action);
        } catch (NumberFormatException formatException) {

            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
        }

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
