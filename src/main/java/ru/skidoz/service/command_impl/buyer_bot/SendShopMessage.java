package ru.skidoz.service.command_impl.buyer_bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.util.TelegramElementsUtil;

/**
 * @author andrey.semenov
 */
@Component
public class SendShopMessage implements Command {
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private TelegramElementsUtil telegramElementsUtil;

    @Override
    public LevelResponse runCommand(Update update, Level shopLevel, User users) throws IOException, WriterException, UnirestException {

        Long shopChatId = users.getChatId();
        Shop shop = shopCacheRepository.findById(userCacheRepository.findByChatId(shopChatId).getSellerShop());

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++SendShopMessage+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

//        User buyer = userRepository.findById(shop.getCurrentConversationShopUser());
        Long buyerChatId = shop.getCurrentConversationShopUserChatId();//buyer.getChatId();

        System.out.println("buyerChatId***" + buyerChatId);

        //Level buyerLevel = initialLevel.level0_1_4;
        LevelDTOWrapper buyerLevel = initialLevel.convertToLevel(initialLevel.level0_1_4,
                false,
                false);

        buyerLevel.addMessage(telegramElementsUtil.convertUpdateToMessage(update, users));
        ButtonRow row = new ButtonRow();
        Button button = new Button(row, Map.of(LanguageEnum.RU, "Ответить магазину 2@" + shop.getName()), initialLevel.level_RESPONSE_SHOP_MESSAGE.getIdString() + shop.getId());
        row.add(button);
        Button button2 = new Button(row, Map.of(LanguageEnum.RU, "Отмена "), initialLevel.level_NON_RESPONSE.getIdString() + shopChatId);
        row.add(button2);
        buyerLevel.addRow(row);

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(buyerChatId);
            e.setLevel(buyerLevel);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
