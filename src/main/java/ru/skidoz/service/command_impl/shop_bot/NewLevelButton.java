package ru.skidoz.service.command_impl.shop_bot;

import java.util.Collections;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class NewLevelButton implements Command {

    @Autowired
    private ButtonRowCacheRepository buttonRowCacheRepository;
    @Autowired
    private ButtonCacheRepository buttonCacheRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;
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
        System.out.println("++++++++++++++++++++++++++++++++++NewLevelButton+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();
        //Level resultLevel = levelRepository.findFirstByChatAndCallName(TelegramProcessor.CHAT, EDIT_BUTTON_MESSAGE_RESP.name());
        String inputText = update.getMessage().getText();

        System.out.println("inputText***" + inputText);
        System.out.println(level);

        Long chatId = users.getChatId();
        Level changingLevel = levelCacheRepository.findById(users.getCurrentLevelBeforeConfigId());

        System.out.println("changingLevel+++" + changingLevel.getCallName());

        Level newLevel = new Level();
        newLevel.setParentLevelId(users.getCurrentLevelBeforeConfigId());
        newLevel.setCallName(changingLevel.getCallName() + (int) (Math.random()*1000));
        newLevel.setBotLevel(true);
        Bot bot = botCacheRepository.findById(users.getCurrentChangingBot());
        newLevel.setBot(bot.getId());
        newLevel.setChatId(chatId);
        levelCacheRepository.save(newLevel);
        newLevel.updateLevel(users, changingLevel.getCallName() + newLevel.getId(), changingLevel, false, false, true);
        levelCacheRepository.save(newLevel);
        Message message = new Message(newLevel, Map.of(LanguageEnum.RU, "Измените этот текст нажатием кнопки ниже"));
        messageCacheRepository.save(message);
        newLevel.addMessage(message);
        levelCacheRepository.save(newLevel);

        System.out.println("newLevel+++" + newLevel.getCallName());

        ButtonRow row = new ButtonRow(changingLevel);
        buttonRowCacheRepository.save(row);
        //*saveNew*//changingLevel.addRow(row);
        Button button = new Button(row, Map.of(users.getLanguage(), inputText), newLevel.getIdString());
        buttonCacheRepository.save(button);
//        /*****/row.add(button);
        buttonRowCacheRepository.save(row);
        //*saveNew*//levelRepository.save(changingLevel);

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(newLevel,
                true,
                true);
        
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(shopBots.showEditInterface(resultLevel, users.getLanguage()));
        })), null, null);
    }
}
