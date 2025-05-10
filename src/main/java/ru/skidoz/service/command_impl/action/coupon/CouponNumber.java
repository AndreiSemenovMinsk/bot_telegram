package ru.skidoz.service.command_impl.action.coupon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class CouponNumber implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_COUPON_NUMBER,
                true,
                false);

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());
        String inputText = update.getMessage().getText();

        System.out.println();
        System.out.println();
        System.out.println("***********************CouponNumber*************************");
        System.out.println();
        System.out.println();
        System.out.println("inputText*******" + inputText);

        action.setName(inputText);
        actionCacheRepository.save(action);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
