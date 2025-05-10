package ru.skidoz.util;


import java.io.IOException;

import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.repository.telegram.LevelRepository;
import ru.skidoz.repository.telegram.MessageRepository;
import ru.skidoz.repository.telegram.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class ConnectComparator {

    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private InitialLevel initialLevel;

    public Level compare(String callback, Level parentLevel, User users) throws IOException {

        System.out.println("compare*******" + callback);
        System.out.println(callback.substring(19));

        /*String code = null;
        if (callback.startsWith(initialLevel.level_BASKET.getIdString())) {
            code = "https://t.me/Skido_Bot?start=BK" + callback.substring(19);
        } else if (callback.startsWith(initialLevel.level_BOOKMARKS.getIdString())) {
            code = "https://t.me/Skido_Bot?start=BM" + callback.substring(19);
        } else if (callback.startsWith(initialLevel.level_CASHBACKS.getIdString())) {
            code = "https://t.me/Skido_Bot?start=CB" + callback.substring(19);
        } else {
            return parentLevel;
        }

        System.out.println();
        System.out.println("ConnectComparator--------------------------" + users.getId());
        System.out.println();

        Level level = new Level(users, CONNECT.name(), parentLevel, false);
        level.setId((int) (Math.random()*1000000000));
//        levelRepository.saveByMap(level);
        levelRepository.save(level);

        Message message2_1 = new Message(level, 0, null, IOUtils.toByteArray(qrInputStream(code)), "Покажите QR партнеру 2");
        messageRepository.save(message2_1);
//        level.addMessage(message2_1);
        Message message2_2 = new Message(level, 1, Map.of(LanguageEnum.RU, "Или перешлите ссылку:"));
        messageRepository.save(message2_2);
//        level.addMessage(message2_2);
        Message message2_3 = new Message(level, 2, Map.of(LanguageEnum.RU, code));
        messageRepository.save(message2_3);
//        level.addMessage(message2_3);
//        levelRepository.save(level);

        return level;*/

        return null;
    }
}
