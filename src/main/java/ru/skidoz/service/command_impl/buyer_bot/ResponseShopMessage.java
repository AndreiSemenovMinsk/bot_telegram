package ru.skidoz.service.command_impl.buyer_bot;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
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
public class ResponseShopMessage implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level buyerLevel, User users) throws IOException, WriterException {

        Long buyerChatId = users.getChatId();
        //User users = userRepository.findFirstByChatId(buyerChatId);

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++ResponseShopMessage+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        if (users.getCurrentLevelBeforeInterruption() == null) {
            users.setCurrentLevelBeforeInterruption(buyerLevel.getId());
            userCacheRepository.save(users);
        }

        String callback = update.getCallbackQuery().getData();
        Integer  shopId = Integer.valueOf(callback.substring(19));
        Shop shop = shopCacheRepository.findById(shopId);

        users.setCurrentConversationShopId(shop.getId());
        userCacheRepository.save(users);

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level0_1_5,
                false,
                false);
        resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Введите ответ магазину @3" + shop.getName())));

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(buyerChatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
