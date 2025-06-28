package ru.skidoz.service.command_impl.shop_bot;

import java.util.Collections;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class EditMessage implements Command {

    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ShopBots shopBots;
    @Autowired
    private InitialLevel initialLevel;



    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++EditMessage+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();
        //Level resultLevel = levelRepository.findFirstByChatAndCallName(TelegramProcessor.CHAT, EDIT_BUTTON_MESSAGE_RESP.name());
        String inputText = update.getMessage().getText();

        LanguageEnum language = users.getLanguage();

        System.out.println("inputText***" + inputText);
        System.out.println("level*******" + level);
        System.out.println("users.getCurrentLevelBeforeConfigId()+++" + users.getCurrentLevelBeforeConfigId());

        Long chatId = users.getChatId();

        System.out.println("users+++" + users);
        System.out.println("users.getCurrentChangingMessage()" + users.getCurrentChangingMessage());

        Message message = messageCacheRepository.findById(users.getCurrentChangingMessage());
        message.setText(language, inputText);
        messageCacheRepository.save(message);

        LevelDTOWrapper resultLevel = shopBots.showEditInterface(initialLevel.convertToLevel(levelCacheRepository.findById(users.getCurrentLevelBeforeConfigId()),
                true,
                true),
                language);
        
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
