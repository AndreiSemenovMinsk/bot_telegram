package ru.skidoz.service.command_impl.construct_shop;

import static ru.skidoz.service.command.CommandName.ADMIN;

import com.google.zxing.WriterException;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.command.Command;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.util.TelegramElementsUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class Admin implements Command {

    @Autowired
    LevelCacheRepository levelCacheRepository;
    @Autowired
    ShopCacheRepository shopCacheRepository;
    @Autowired
    private TelegramElementsUtil telegramElementsUtil;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User user)
            throws IOException, WriterException, CloneNotSupportedException {

        LevelDTOWrapper resultLevel;

        System.out.println();
        System.out.println("****************Admin**************");
        System.out.println();
        System.out.println();
        System.out.println(update.getCallbackQuery());

        LanguageEnum language = user.getLanguage();

        Level adminLevel = levelCacheRepository.findFirstByUser_ChatIdAndCallName(
                user.getChatId(),
                ADMIN.name());
        if (adminLevel == null) {
            adminLevel = initialLevel.level_ADMIN.clone(user);
            levelCacheRepository.save(adminLevel);
        }

        System.out.println("------------------"
                + adminLevel);

        resultLevel = initialLevel.convertToLevel(
                levelCacheRepository.findFirstByUser_ChatIdAndCallName(user.getChatId(), ADMIN.name()),
                true,
                true);

        final Collection<Shop> allByAdminUserId = shopCacheRepository.findAllByAdminUser(user.getId());
        if (allByAdminUserId == null || allByAdminUserId.isEmpty()) {

            System.out.println("-----------------No shops found");

            telegramElementsUtil.buttonDisabler(resultLevel, "Админить магазины/сервисы", language);
        }

        System.out.println();
        System.out.println("ADMIN*****" + resultLevel);
        System.out.println();

        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(user.getChatId());
                    e.setUser(user);
                    e.setLevel(resultLevel);
                })), null, null);
    }
}
