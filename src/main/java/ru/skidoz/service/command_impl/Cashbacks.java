package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.util.Collections;

import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.ConnectComparator;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.CASHBACKS;

/**
 * @author andrey.semenov
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Cashbacks implements Command {
    @Autowired
    ConnectComparator connectComparator;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("----------------------------Cashbacks--------------------------");
        System.out.println();
        System.out.println(level.getCallName());

        if (update.getCallbackQuery() != null) {
            if (update.getCallbackQuery().getData().length() == 19) {

                Level userLevel = levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), CASHBACKS.name());
                LevelDTOWrapper resultLevel = initialLevel.convertToLevel(userLevel,
                        true,
                        true);

                return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(resultLevel);
                })), null, null);
            } else {

                //TODO - это не срабатывает, так как нет такой кнопки на BOOKMARKS + id
                Level finalNewLevel = connectComparator.compare(update.getCallbackQuery().getData(), level, users);
                LevelDTOWrapper resultLevel = initialLevel.convertToLevel(finalNewLevel,
                        true,
                        true);
                return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(resultLevel);
                })), null, null);
            }
        }

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
