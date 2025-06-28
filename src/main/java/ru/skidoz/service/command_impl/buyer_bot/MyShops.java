package ru.skidoz.service.command_impl.buyer_bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.service.command_impl.shop_bot.ShopBots;
import ru.skidoz.util.Structures;
import com.google.zxing.WriterException;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.util.TelegramElementsUtil;

import static ru.skidoz.service.command.CommandName.MY_SHOPS;

/**
 * @author andrey.semenov
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class MyShops implements Command {
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private TelegramElementsUtil telegramElementsUtil;
    @Autowired
    private ButtonRowCacheRepository buttonRowRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private ShopBots shopBots;

    private void updateLevelNames(Level oldBotLevel, Bot bot, User users) {
/*
        ButtonRow lastRow = new ButtonRow();
        Button back = new Button(oldBotLevel, "Назад ", "@" + PARAMS.name());
        buttonRepository.save(back);
        lastRow.add(back);
        Button toMain = new Button(oldBotLevel, "На главную ", "@" + TO_MAIN.name());
        buttonRepository.save(toMain);
        lastRow.add(toMain);
        oldBotLevel.addRow(lastRow);

        levelRepository.save(oldBotLevel);*/
        //Level parentLevel = levelRepository.findById();
        //initialLevel.retrieveButtonRows(level);
        System.out.println();
        System.out.println("updateLevelNames--------------------------------------");
        Level cloneBotLevel = initialLevel.cloneLevel(oldBotLevel, users);
        System.out.println();
        System.out.println("oldBotLevel  ***" + oldBotLevel);
        System.out.println("cloneBotLevel***" + cloneBotLevel);

        List<ButtonRow> buttonRowList = buttonRowRepository.findAllByLevel_Id(cloneBotLevel.getParentLevelId());
        String oldCallName = cloneBotLevel.getCallName();
        String newCallName = cloneBotLevel.getIdString();
        for (ButtonRow buttonRow : buttonRowList) {
            for (Button button : buttonRow.getButtonList()) {
                if (button.getCallback().equals(oldCallName)) {
                    button.setCallback(newCallName);
                } /*else if (button.getCallback().startsWith(SUBMIT_BOT.name())){
            }*/
            }
        }
        // теперь это не надо делать oldBotLevel.setCallName(newCallName);

        List<Level> childLevels = levelRepository.findAllByParentLevelId(oldBotLevel.getId());
        for (Level childLevel : childLevels) {

            System.out.println("childLevel***" + childLevel);

            childLevel.setParentLevelId(cloneBotLevel.getId());

            System.out.println("after set***" + childLevel);

            updateLevelNames(childLevel, bot, users);
        }
        cloneBotLevel.setBot(bot.getId());
        cloneBotLevel.setChatId(users.getChatId());
        levelRepository.save(cloneBotLevel);
    }

    @Override
    public LevelResponse runCommand(Update update, Level buyerLevel, User users) throws IOException, WriterException, UnirestException, CloneNotSupportedException {

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++MyShops+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevelDTOWrapper;
        //String callback = update.getCallbackQuery().getData();
        /*
        по идее, когда происходит переход по боту,  level уже меняется и сюда не переходим
        if (callback.startsWith(BOT_LEVEL.name())) {

            String code = callback.substring(BOT_LEVEL.name().length());
            BotLevel bot = botRepository.findById(Integer.valueByCode(code));

            resultLevelDTOWrapper = bot.getInitiallevel();
        } else*/
        System.out.println(MY_SHOPS.name() + "*MY_SHOPS.name()*" + buyerLevel.getCallName());


        if (update.getCallbackQuery() != null) {

            String callback = update.getCallbackQuery().getData();

            System.out.println("-----------------------callback");
            System.out.println(callback);
            System.out.println(callback.equals(initialLevel.level_MY_SHOPS.getIdString()));
            System.out.println("initialLevel.level_ADD_TAXI_BOT.getIdString---" + callback.equals(initialLevel.level_ADD_TAXI_BOT.getIdString()));

            if (callback.equals(initialLevel.level_MY_SHOPS.getIdString())) {

                Level resultLevel = levelRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), MY_SHOPS.name());

                resultLevelDTOWrapper = initialLevel.convertToLevel(resultLevel,
                        true,
                        true);
            } else if (callback.equals(initialLevel.level_ADD_TAXI_BOT.getIdString())
            || callback.equals(initialLevel.level_ADD_TAXI_BOT.getIdString())) {//callback.startsWith("@")

                resultLevelDTOWrapper = null;

                System.out.println();
                System.out.println("-----------------------------INTO AddTaxiBot-----------------------------");
                System.out.println();
                System.out.println();

                System.out.println(callback);

                Shop shopInitiator = shopCacheRepository.findById(users.getCurrentAdminShop());//shopRepository.getByChatId(chatId);

                Level taxiBotLevel = levelRepository.findById(Structures.parseInt(callback/*.substring(1)*/));

                System.out.println("taxiBotLevel-----------------------     " + taxiBotLevel);

                Bot bot = botCacheRepository.findByShopId(shopInitiator.getId());
                Level botLevel;

                System.out.println("bot+++" + bot);

                if (bot == null || (botLevel = levelRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), taxiBotLevel.getCallName())) == null) {

                    System.out.println(167);

                    if (bot != null){
                        botCacheRepository.delete(bot.getId());
                    }

                    Level cloneBot = initialLevel.cloneLevel(taxiBotLevel, users);
                    bot = new Bot(b -> {
                        b.setShop(shopInitiator.getId());
                        b.setInitialLevel(cloneBot.getId());
                    });
                    botCacheRepository.save(bot);

                    System.out.println("183++++++setCurrentChangingBot" + bot.getId());

                    users.setCurrentChangingBot(bot.getId());
                    userCacheRepository.save(users);

                    cloneBot.setBot(bot.getId());
                    cloneBot.setChatId(users.getChatId());

                    List<Level> levels = levelRepository.findAllByParentLevelId(taxiBotLevel.getId());
                    for (Level childLevel : levels) {
                        childLevel.setParentLevelId(cloneBot.getId());
                        updateLevelNames(childLevel, bot, users);
                    }
                    levelRepository.save(cloneBot);

                    return shopBots.runCommand(update, cloneBot, users);
                } else {

                    System.out.println(196);

                    LevelDTOWrapper resultLevel = initialLevel.convertToLevel(botLevel,
                            true,
                            true);
                    resultLevel.addMessage(new Message(null, 1,
                            Map.of(LanguageEnum.RU, "Бот типа " + bot.getName()
                            + " уже создан для этого магазина, отредактируйте его либо выберите другой тип")));

                    return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                        e.setChatId(users.getChatId());
                        e.setUser(users);
                        e.setLevel(resultLevel);
                    })), null, null);
                }

                //cloneBot;
                /*resultLevelDTOWrapper = initialLevel.convertToLevel(cloneBot,
                        true,
                        true);*/
            } else {
                System.out.println();
                System.out.println("------------------------------Aquire Button-------------------------------" + users.isShopOwner());
                System.out.println();
                System.out.println();

                resultLevelDTOWrapper = initialLevel.convertToLevel(buyerLevel,
                        true,
                        true);

                Long shopChatId = buyerLevel.getChatId();//shop.getUsers().getChatId();

                System.out.println("buyerLevel+" + buyerLevel);
                System.out.println("users.getChatId()+" + users.getChatId());
                System.out.println("shopChatId++++++++" + shopChatId);
                System.out.println("users.getChatId().equals(shopChatId)++++++" + users.getChatId().equals(shopChatId));

                if (users.isShopOwner()
                        && users.getChatId().equals(shopChatId)) {

                    System.out.println("buyerLevel.getId()*****             " + buyerLevel.getId());
                    System.out.println("buyerLevel.getBot().getId()******** " + buyerLevel.getBot());

                    users.setCurrentConversationShop(shopChatId);
                    userCacheRepository.save(users);


                    Bot bot = botCacheRepository.findById(buyerLevel.getBot());

                    System.out.println("bot.getInitialLevel()******         " + bot.getInitialLevel());

                    ButtonRow row = new ButtonRow();
                    Button button = new Button(row, Map.of(LanguageEnum.RU, "Выбрать шаблон"), "@" + Level.getIdString(bot.getInitialLevel()));
                    row.add(button);
                    resultLevelDTOWrapper.addRow(row);
                } else {

                }
            }

            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setUser(users);
                e.setLevel(resultLevelDTOWrapper);
            })), null, null);

        } else if (update.getMessage() != null) {
            System.out.println();
            System.out.println("------------------------------Aquire Message-------------------------------");
            System.out.println();
            System.out.println();
            Long buyerChatId = users.getChatId();

        /*String callName = buyerLevel.getCallName();
        Chat buyerChat = chatRepository.findFirstByChatId(buyerChatId);
        Chat shopChat = chatRepository.getChatByLevelCallName(callName);
        Shop shop = bot.getShop();
            Bot bot = buyerLevel.getBot();*/
            System.out.println("users+++++++" + users);

            Message finalMessage = telegramElementsUtil.convertUpdateToMessage(update, users);
//SubmitBot не используется - сообщение идет напрямую
 /*
BuyerBot buyerBot = buyerBotRepository.findByUserAndBot(users, bot);
            if (buyerBot == null) {
                buyerBot = new BuyerBot(e -> {
                    e.setBot(bot);
                    e.setUser(users);
                });
                buyerBotRepository.save(buyerBot);
            }
            BuyerBot finalBuyerBot = buyerBot;
 finalMessage.setLevel();
            System.out.println("finalMessage+++" + finalMessage);
            messageRepository.save(finalMessage);
            BuyerBotMessage buyerBotMessage = new BuyerBotMessage(e -> {
                e.setBuyerBot(finalBuyerBot);
                e.setReceivedMessage(finalMessage.getId());
                e.setValuableLevel(buyerLevel.getId());
            });
            buyerBotMessageRepository.save(buyerBotMessage);*/
            LevelDTOWrapper buyerlevel = initialLevel.convertToLevel(buyerLevel,
                    false,
                    false);
            buyerlevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Сообщение отправлено")));


            LevelDTOWrapper shoplevel = initialLevel.convertToLevel(initialLevel.level0_1_1,
                    false,
                    false);
            shoplevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "В бот поступило новое сообщение от " + users.getName())));

            shoplevel.addMessage(finalMessage);

            ButtonRow row = new ButtonRow();
            Button button = new Button(row, Map.of(LanguageEnum.RU, "Ответить покупателю " + users.getName()), initialLevel.level_RESPONSE_BUYER_MESSAGE.getIdString() + buyerChatId);
            row.add(button);
            shoplevel.addRow(row);

            List<LevelChat> levelChatDTOList = new ArrayList<>();
            levelChatDTOList.add(new LevelChat(e -> {
                e.setChatId(buyerChatId);
                e.setUser(users);
                e.setLevel(buyerlevel);
            }));
            levelChatDTOList.add(new LevelChat(e -> {
                e.setChatId(users.getCurrentConversationShop());
                e.setLevel(shoplevel);
            }));
            return new LevelResponse(levelChatDTOList, null, null);
        }
        return null;
    }
}
