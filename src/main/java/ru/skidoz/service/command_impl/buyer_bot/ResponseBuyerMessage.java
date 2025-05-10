package ru.skidoz.service.command_impl.buyer_bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class ResponseBuyerMessage implements Command {
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level shopLevel, User users) throws IOException, WriterException {

        Long shopChatId = users.getChatId();
        Shop shop = shopCacheRepository.findBySellerChatId(shopChatId);

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++ResponseBuyerMessage+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        //User users = shop.getUsers();
        if (users.getCurrentLevelBeforeInterruption() == null) {
            users.setCurrentLevelBeforeInterruption(shopLevel.getId());
        }
        userCacheRepository.save(users);

        String callback = update.getCallbackQuery().getData();
        Long buyerChatId = Long.valueOf(callback.substring(19));
        User buyer = userCacheRepository.findByChatId(buyerChatId);

        System.out.println("buyerChatId***" + buyerChatId);

        shop.setCurrentConversationShopUserChatId(buyerChatId);
        shopCacheRepository.save(shop);

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level0_1_4,
                false,
                false);
        resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Введите ответ покупателю " + buyer.getName())));

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
