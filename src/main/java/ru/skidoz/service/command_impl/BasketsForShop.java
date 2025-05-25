package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.ConnectComparator;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.BASKETS_FOR_SHOP;

/**
 * @author andrey.semenov
 */
@Component
public class BasketsForShop implements Command {
    @Autowired
    private ConnectComparator connectComparator;
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {

        Level userLevel = levelRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), BASKETS_FOR_SHOP.name() + users.getCurrentAdminShop());

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(userLevel,
                true,
                true);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
