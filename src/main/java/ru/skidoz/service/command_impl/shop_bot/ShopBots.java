package ru.skidoz.service.command_impl.shop_bot;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
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
public class ShopBots implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private BotTypeCacheRepository botTypeCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users)
            throws IOException, WriterException {

        System.out.println();
        System.out.println(
                "++++++++++++++++++++++++++++++++++ShopBots+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        LevelDTOWrapper newLevelWrapper;

        Shop shop = shopCacheRepository.findById(users.getCurrentAdminShop());
        Bot bot = botCacheRepository.findByShopId(shop.getId());

        if (bot != null) {

            BotType botType = botTypeCacheRepository.findById(bot.getBotType());

            newLevelWrapper = initialLevel.convertToLevel(
                    initialLevel.level_SHOP_BOTS,
                    false,
                    true);

            Message message = new Message(
                    null,
                    Map.of(LanguageEnum.RU, "Бот: " + botType.getName()));
            newLevelWrapper.addMessage(message);
/*
            ButtonRow shopRow1 = new ButtonRow();
            Button shopButton1 = new Button(
                    shopRow1, Map.of(LanguageEnum.RU, "Редактировать"),
                    initialLevel.level_EDIT_BOT.getIdString());
            shopRow1.add(shopButton1);
            newLevelWrapper.addRow(shopRow1);

            System.out.println("Structures.getIdString("+bot.getInitialLevel()+") " + Structures.getIdString(bot.getInitialLevel()));

            ButtonRow shopRow2 = new ButtonRow();
            Button shopButton2 = new Button(
                    shopRow2, Map.of(LanguageEnum.RU, "Просмотр бота"),
                    Structures.getIdString(bot.getInitialLevel()));
            shopRow2.add(shopButton2);
            newLevelWrapper.addRow(shopRow2);

            ButtonRow shopRow3 = new ButtonRow();
            Button shopButton3 = new Button(
                    shopRow3, Map.of(LanguageEnum.RU, "Выбрать другой шаблон бота"),
                    initialLevel.level_ADD_BOT.getIdString());
            shopRow3.add(shopButton3);
            newLevelWrapper.addRow(shopRow3);*/
        } else {
            newLevelWrapper = initialLevel.convertToLevel(
                    initialLevel.level_ADD_BOT,
                    true,
                    true);
        }

        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(newLevelWrapper);
                })), null, null);
    }
}
