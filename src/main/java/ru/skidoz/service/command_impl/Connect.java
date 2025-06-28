package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.util.Collections;

import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.CONNECT;

/**
 * @author andrey.semenov
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Connect implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        Level finalNewLevel = levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), CONNECT.name());
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(finalNewLevel,
                true,
                false);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
