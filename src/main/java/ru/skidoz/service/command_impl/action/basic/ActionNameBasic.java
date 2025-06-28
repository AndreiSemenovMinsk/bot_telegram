package ru.skidoz.service.command_impl.action.basic;

import java.util.Collections;

import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
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
public class ActionNameBasic implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SELECT_LEVEL_TYPE,
                true,
                true);

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        System.out.println();
        System.out.println("**********ActionNameBasic***********");
        System.out.println();
        System.out.println();
        System.out.println("shopInitiator*******" + shopInitiator);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");

        Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());
        String inputText = update.getMessage().getText();
        action.setName(inputText);
        actionCacheRepository.save(action);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
