package ru.skidoz.service.command_impl.shop_bot;

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
public class AddTaxiBot implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
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
        //Level parentLevel = levelRepository.findById();
        //initialLevel.retrieveButtonRows(level);
        List<ButtonRow> buttonRowList = buttonRowCacheRepository.findAllByLevel_Id(parentLevelId);
        String oldCallName = cloneEditBotLevel.getCallName();
        String newCallName = cloneEditBotLevel.getIdString();
        for (ButtonRow buttonRow : buttonRowList) {
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
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("-----------------------------AddTaxiBot-----------------------------");
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevel;
//        Shop shopInitiator = shopCacheRepository.findById(users.getCurrentAdminShop());//shopRepository.getByChatId(chatId);
        Integer shopInitiator = users.getCurrentAdminShop();

                Level taxiBotLevel = initialLevel.level_ADD_TAXI_BOT;
        Level cloneBot = initialLevel.cloneLevel(taxiBotLevel, users);
        //Level cloneEditBot = taxiBotLevel.clone(users);
        levelCacheRepository.save(cloneBot);
        //levelRepository.save(cloneEditBot);
        /*users.addLevel(cloneBot);
        users.addLevel(cloneEditBot);
        userRepository.save(users);*/
        Bot bot = botCacheRepository.findByShopId(shopInitiator);
        if (bot == null) {

            System.out.println();

            bot = new Bot(b -> {
                b.setShop(shopInitiator);
                b.setInitialLevel(cloneBot.getId());
            });
            //shopInitiator.addBot(bot);
            botCacheRepository.save(bot);
        }

        System.out.println("116++++++setCurrentChangingBot" + bot.getId());

        users.setCurrentChangingBot(bot.getId());
        userCacheRepository.save(users);

        System.out.println("users.getCurrentChangingBot()-----------------------------------------"+ users.getCurrentChangingBot());

        Long chatId = users.getChatId();
        cloneBot.setBot(bot.getId());
        cloneBot.setChatId(chatId);

        List<Level> levels = levelCacheRepository.findAllByParentLevelId(cloneBot.getId());
        for (Level childLevel : levels) {
            updateLevelNames(childLevel, bot, chatId);
        }
        //adder.addShopBot(chatId, bot, shopInitiator);

        levelCacheRepository.save(cloneBot);

        //cloneBot;
        resultLevel = initialLevel.convertToLevel(cloneBot,
                true,
                true);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
