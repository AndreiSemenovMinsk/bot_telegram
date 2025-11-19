package ru.skidoz.service.command_impl.construct_shop;

import java.io.IOException;
import java.util.Collections;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.BotCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
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
    public LevelResponse runCommand(Update update, Level level, User users)
            throws IOException, WriterException {

        LevelDTOWrapper resultLevel;

        System.out.println();
        System.out.println("****************AdminShop**************");
        System.out.println();
        System.out.println();
        System.out.println(update.getCallbackQuery());


        System.out.println("------------------"
                + levelCacheRepository.findFirstByUser_ChatIdAndCallName(
                users.getChatId(),
                ADMIN_SHOPS.name()));

        resultLevel = initialLevel.convertToLevel(
                levelCacheRepository.findFirstByUser_ChatIdAndCallName(
                        users.getChatId(),
                        ADMIN_SHOPS.name()),
                true,
                true);
        System.out.println();
        System.out.println("ADMIN_SHOPS*****" + resultLevel);
        System.out.println();

        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(resultLevel);
                })), null, null);
    }
}
