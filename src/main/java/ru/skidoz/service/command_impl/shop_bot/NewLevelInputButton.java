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
public class NewLevelInputButton implements Command {

    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private EditBot editBot;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++NewLevelInputButton+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();
        //Level resultLevel = levelRepository.findFirstByChatAndCallName(TelegramProcessor.CHAT, EDIT_BUTTON_MESSAGE_RESP.name());
        String inputText = update.getMessage().getText();

        System.out.println("inputText***" + inputText);
        System.out.println(level);

        Long chatId = users.getChatId();
        Level changingLevel = levelCacheRepository.findById(users.getCurrentLevelBeforeConfigId());

        System.out.println("changingLevel++++++++++++++++++++++++++" + changingLevel);
        System.out.println("users.getCurrentLevelBeforeConfigId()+++" + users.getCurrentLevelBeforeConfigId());


        //просто проверить гипотезу 12/7/2023
//        Level previousInputLevel = levelCacheRepository.findBySourceIsMessageAndParentLevelId(users.getCurrentLevelBeforeConfigId());
//
//        System.out.println("previousInputLevel***" + previousInputLevel);
//
//        previousInputLevel.setSourceIsMessage(false);
//        levelCacheRepository.save(previousInputLevel);


        Level newLevel = new Level();
        newLevel.setParentLevelId(users.getCurrentLevelBeforeConfigId());
        newLevel.setSourceIsMessage(true);
        newLevel.setBotLevel(true);
        Bot bot = botCacheRepository.findByShopId(users.getCurrentAdminShop());
        newLevel.setBot(bot.getId());
        newLevel.setChatId(chatId);
        newLevel.setCallName(changingLevel.getCallName() + (int) (Math.random()*100));
        levelCacheRepository.save(newLevel);
        newLevel.updateLevel(users, changingLevel.getCallName() + newLevel.getId(), changingLevel, false, true, true);
        levelCacheRepository.save(newLevel);
        Message message = new Message(newLevel, Map.of(LanguageEnum.RU, inputText));
        messageCacheRepository.save(message);
        newLevel.addMessage(message);
        levelCacheRepository.save(newLevel);

        System.out.println("newLevel***" + newLevel);

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(newLevel,
                true,
                false);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(editBot.showEditInterface(resultLevel, users.getLanguage()));
        })), null, null);
    }
}
