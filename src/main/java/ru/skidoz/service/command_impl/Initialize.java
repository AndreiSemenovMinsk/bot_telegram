package ru.skidoz.service.command_impl;


import java.util.Collections;

import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class Initialize implements Command {

    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) {

        System.out.println();
        System.out.println("+++++++++++++++++++++++++Initialize++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();
        System.out.println(users.getChatId());

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(initialLevel.convertToLevel(initialLevel.level_INITIALIZE,
                    true,
                    true));
        })), null, null);
    }
}
