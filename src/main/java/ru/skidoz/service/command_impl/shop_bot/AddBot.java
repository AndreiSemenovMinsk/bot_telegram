package ru.skidoz.service.command_impl.shop_bot;

import static ru.skidoz.util.Structures.parseInt;

import java.util.Collections;
import java.util.List;

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
public class AddBot implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private BotTypeCacheRepository botTypeCacheRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    private void updateLevelNames(Level cloneEditBotLevel, Bot bot, Long сhatId) {
/*
        ButtonRow lastRow = new ButtonRow();
        Button back = new Button(cloneEditBotLevel, "Назад ", "@" + PARAMS.name());
        buttonRepository.save(back);
        lastRow.add(back);
        Button toMain = new Button(cloneEditBotLevel, "На главную ", "@" + TO_MAIN.name());
        buttonRepository.save(toMain);
        lastRow.add(toMain);
        cloneEditBotLevel.addRow(lastRow);
        levelRepository.save(cloneEditBotLevel);*/

        int parentLevelId = cloneEditBotLevel.getParentLevelId();

        System.out.println("updateLevelNames+++++++" + parentLevelId);

        //Level parentLevel = levelRepository.findById();
        //initialLevel.retrieveButtonRows(level);
        //List<ButtonRow> buttonRowList = buttonRowCacheRepository.findAllByLevel_Id(parentLevelId);
        String oldCallName = cloneEditBotLevel.getCallName();
        String newCallName = cloneEditBotLevel.getIdString();
        for (ButtonRow buttonRow : cloneEditBotLevel.getButtonRows()) {
            for (Button button : buttonRow.getButtonList()) {
                if (button.getCallback().equals(oldCallName)) {
                    button.setCallback(newCallName);
                } /*else if (button.getCallback().startsWith(SUBMIT_BOT.name())){
            }*/
            }
        }
        // теперь это не надо делать cloneEditBotLevel.setCallName(newCallName);

        List<Level> childLevels = levelCacheRepository.findAllByParentLevelId(cloneEditBotLevel.getId());
        for (Level childLevel : childLevels) {
            updateLevelNames(childLevel, bot, сhatId);
        }
        cloneEditBotLevel.setBot(bot.getId());
        cloneEditBotLevel.setChatId(сhatId);
        levelCacheRepository.save(cloneEditBotLevel);
    }

    @Override
    public LevelResponse runCommand(Update update, Level level, User users)
            throws CloneNotSupportedException {

        System.out.println();
        System.out.println("-----------------------------AddBot-----------------------------");
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevel;
        Integer shopInitiator = users.getCurrentAdminShop();

        final BotType botType = botTypeCacheRepository.findByInitialLevelStringId(level.getIdString());

        final Level initialBotLevel = levelCacheRepository.findById(parseInt(botType.getInitialLevelStringId()));

        Level cloneBotLevel = initialLevel.cloneLevel(initialBotLevel, users);

        levelCacheRepository.save(cloneBotLevel);

//        Bot bot = botCacheRepository.findByShopId(shopInitiator);

        System.out.println();

        Bot bot = new Bot(b -> {
                b.setBotType(botType.getId());
                b.setShop(shopInitiator);
                b.setInitialLevel(cloneBotLevel.getId());
        });
        botCacheRepository.save(bot);


        System.out.println("116++++++setCurrentChangingBot" + bot.getId());

//        userCacheRepository.save(users);

        System.out.println("users.getCurrentChangingBot()-----------------------------------------");

        Long chatId = users.getChatId();
        cloneBotLevel.setBot(bot.getId());
        cloneBotLevel.setChatId(chatId);

        updateLevelNames(cloneBotLevel, bot, chatId);

//        List<Level> levels = levelCacheRepository.findAllByParentLevelId(cloneBotLevel.getId());
//        for (Level childLevel : levels) {
//            updateLevelNames(childLevel, bot, chatId);
//        }
        //adder.addShopBot(chatId, bot, shopInitiator);

        levelCacheRepository.save(cloneBotLevel);

        //cloneBotLevel;
        resultLevel = initialLevel.convertToLevel(
                cloneBotLevel,
                true,
                true);

        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(resultLevel);
                })), null, null);
    }
}
