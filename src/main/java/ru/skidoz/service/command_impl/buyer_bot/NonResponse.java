package ru.skidoz.service.command_impl.buyer_bot;

import java.io.IOException;
import java.util.Collections;

import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class NonResponse implements Command {

    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        Long chatId = users.getChatId();
        //User users = userRepository.findFirstByChatId(chatId);


        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++NonResponse+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(users.getCurrentLevelBeforeInterruption(),
                true,
                true);

        users.setCurrentLevelBeforeInterruption(null);
        userCacheRepository.save(users);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
