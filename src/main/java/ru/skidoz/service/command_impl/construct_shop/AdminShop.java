package ru.skidoz.service.command_impl.construct_shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.BotCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Structures;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.util.TelegramElementsUtil;

import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;

/**
 * @author andrey.semenov
 */
@Component
public class AdminShop implements Command {


    @Autowired
    ShopCacheRepository shopCacheRepository;
    @Autowired
    BotCacheRepository botCacheRepository;
    @Autowired
    LevelCacheRepository levelCacheRepository;
    @Autowired
    private TelegramElementsUtil telegramElementsUtil;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {

        LevelDTOWrapper resultLevel;

        System.out.println();
        System.out.println("****************AdminShop**************");
        System.out.println();
        System.out.println();
        System.out.println(update.getCallbackQuery());

        LanguageEnum language = users.getLanguage();

        if (update.getCallbackQuery() != null && update.getCallbackQuery().getData().startsWith("@")) {

            String shopId = update.getCallbackQuery().getData().substring(1);
            //Shop shop = shopRepository.findById(Structures.parseLong(shopId));
            users.setCurrentAdminShop(Structures.parseInt(shopId));

            System.out.println("@@@@@"  + shopId);
            System.out.println("-----"  + Structures.parseLong(shopId));

            Shop shop = shopCacheRepository.findById(Structures.parseInt(shopId));

            System.out.println("67++++++setCurrentChangingBot" + shopId);
            System.out.println("68++++++shop.getId()" + shop.getId());

            Bot bot = botCacheRepository.findByShopId(shop.getId());

            System.out.println("botId++++" + bot);

            if (bot != null && bot.getId() != null) {
                users.setCurrentChangingBot(bot.getId());
//            users.setCurrentChangingBot(Structures.parseLong(shopId));//botRepository.findByShopId(Structures.parseLong(shopId))
            }

            resultLevel = initialLevel.convertToLevel(initialLevel.level_ADMIN_ADMIN,
                    true,
                    true);

            System.out.println("shop----------------+++" + shop);
            System.out.println("shop.getAdminUser()+++" + shop.getAdminUser());
            System.out.println("users*********+++++++++" + users);
            System.out.println(shop.getAdminUser().equals(users.getId()));

            if (!shop.getAdminUser().equals(users.getId())) {
                telegramElementsUtil.buttonDisabler(resultLevel, "Акции", language);
                telegramElementsUtil.buttonDisabler(resultLevel, "Партнеры", language);
                telegramElementsUtil.buttonDisabler(resultLevel, "Продавцы", language);
                telegramElementsUtil.buttonDisabler(resultLevel, "Бот магазина", language);
                telegramElementsUtil.buttonDisabler(resultLevel, "Товары", language);
            }

            System.out.println();
            System.out.println("ADMIN_ADMIN*****" + resultLevel);
            System.out.println();
        } else {
            System.out.println("------------------" + levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), ADMIN_SHOPS.name()));

            resultLevel = initialLevel.convertToLevel(
                    levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), ADMIN_SHOPS.name()),
                    true,
                    true);
            System.out.println();
            System.out.println("ADMIN_SHOPS*****" + resultLevel);
            System.out.println();
        }

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
