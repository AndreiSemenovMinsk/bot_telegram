package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.aop.repo.PurchaseCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.CASHBACKS;
import static ru.skidoz.service.command.CommandName.CONNECT;
import static ru.skidoz.util.Structures.stringBeautifier;

/**
 * @author andrey.semenov
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Cashbacks implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("----------------------------Cashbacks--------------------------");
        System.out.println();
        System.out.println(level.getCallName());

        if (update.getCallbackQuery() != null) {
            if (update.getCallbackQuery().getData().length() == 19) {

                LevelDTOWrapper userLevel = createLevel(users);

                return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(userLevel);
                })), null, null);
            }
        }

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }


    private LevelDTOWrapper createLevel(User users) {

        LevelDTOWrapper newLevelWrapper = initialLevel.convertToLevel(
                initialLevel.level_CASHBACKS,
                false,
                false);


        Map<Integer, List<Purchase>> shopPurchase = new HashMap<>();
        Map<Integer, Shop> shopGroup = new HashMap<>();

        final List<Cashback> cashbacks = cashbackCacheRepository.findAllByUserId(users.getId());


        for (Cashback cashback : cashbacks) {

            final Purchase purchase = purchaseCacheRepository.findById(cashback.getPurchase());

            Shop shop = shopCacheRepository.findById(cashback.getShop());
            List<Purchase> array = shopPurchase.get(shop.getId());

            if (array == null) {
                array = new ArrayList<>();
                shopPurchase.put(shop.getId(), array);
                shopGroup.put(shop.getId(), shop);
            }
            array.add(purchase);
        }

        Message message = new Message(null, Map.of(LanguageEnum.RU, "Мои корзины: "));
        newLevelWrapper.addMessage(message);

        int i = 0;
        for(Map.Entry<Integer, List<Purchase>> entry : shopPurchase.entrySet()) {

            Integer  shopId = entry.getKey();
            List<Purchase> list = entry.getValue();

            final Shop shop = shopGroup.get(shopId);

            i++;
            Message shopMessage = new Message(null, i, Map.of(LanguageEnum.RU, "Магазин " + shop.getName()));
            newLevelWrapper.addMessage(shopMessage);
            i++;
            Message locationMessage = new Message(null, i, shop.getLng(), shop.getLat());
            newLevelWrapper.addMessage(locationMessage);

            for (int j = 0; j < list.size(); j++) {

                Purchase purchase = list.get(j);

                    String text = purchase.getSum() + "р. от " + DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            .withZone(ZoneId.systemDefault()).format(purchase.getTime());

                    i++;
                Message goodsMessage = new Message(null, i, Map.of(LanguageEnum.RU, text));
                newLevelWrapper.addMessage(goodsMessage);
            }

            ButtonRow shopRow = new ButtonRow();
            Button shopButton = new Button(shopRow, Map.of(LanguageEnum.RU, "Связаться с " + shop.getName()),
                    initialLevel.level_CONNECT_SHOP.getIdString() + shop.getId());
            shopRow.add(shopButton);
            newLevelWrapper.addRow(shopRow);

            ButtonRow row4_0 = new ButtonRow();

            Button button4_0_0 = new Button(row4_0, Map.of(LanguageEnum.RU, "Поделиться ссылкой"),
                    levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), CONNECT.name()).getIdString());
            row4_0.add(button4_0_0);
            newLevelWrapper.addRow(row4_0);
        }

        if (i == 0) {
            Message basketMessage = new Message(null, i, Map.of(LanguageEnum.RU, "Список товаров в корзине пуст"));
            newLevelWrapper.addMessage(basketMessage);
        }

        return newLevelWrapper;


    }
}
