package ru.skidoz.service.command_impl.shop_bot;

import static ru.skidoz.service.command.CommandName.DELETE_BUTTON;
import static ru.skidoz.service.command.CommandName.DELETE_LEVEL;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_END_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_INPUT_BUTTON;
import static ru.skidoz.service.command.CommandName.UPDATE_BUTTON;
import static ru.skidoz.service.command.CommandName.UPDATE_MESSAGE;

import com.google.zxing.WriterException;
import ru.skidoz.aop.repo.BotCacheRepository;
import ru.skidoz.aop.repo.BotTypeCacheRepository;
import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.Bot;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.command.Command;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.util.Structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class LookBot implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users)
            throws IOException, WriterException {

        System.out.println();
        System.out.println(
                "++++++++++++++++++++++++++++++++++LookBot+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();


        Bot bot = botCacheRepository.findByShopId(users.getCurrentAdminShop());
        System.out.println("bot+++++" + bot);

        bot.setEdited(false);


        final Level botLevel = levelCacheRepository.findById(bot.getInitialLevel());

        LevelDTOWrapper newLevelWrapper = new LevelDTOWrapper();
        newLevelWrapper.setLevel(botLevel);

        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(newLevelWrapper);
                })), null, null);

    }
}
