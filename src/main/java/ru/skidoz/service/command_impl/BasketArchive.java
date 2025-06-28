package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.BasketProduct;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.BasketCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.BasketProductCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.ConnectComparator;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.CONNECT;
import static ru.skidoz.util.Structures.stringBeautifier;

/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketArchive implements Command {
    @Autowired
    private ConnectComparator connectComparator;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private BasketProductCacheRepository basketProductCacheRepository;
    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {


        System.out.println();
        System.out.println();
        System.out.println("+++++++++++++++++++Basket Archive+++++++++++++++++");
        System.out.println();
        System.out.println();
        System.out.println("update.getCallbackQuery()+++" + update.getCallbackQuery());


        if (update.getCallbackQuery() != null) {

            System.out.println("update.getCallbackQuery().getData().length()+++" + update.getCallbackQuery().getData().length());

            if (update.getCallbackQuery().getData().length() == 19) {

                /*LevelDTOWrapper userLevel = initialLevel.convertToLevel(levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), BASKET.name()),
                        true,
                        true);*/

                LevelDTOWrapper userLevel = createLevel(users);

                userLevel.getButtonRows().stream().peek(row -> System.out.println("row.getId()***" + row.getId()))
                        .forEach(row-> row.getButtonList().forEach(button -> System.out.println(button.getId()+"+++" + button.getNameRU())));

                return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(userLevel);
                })), null, null);
            } else {

                LevelDTOWrapper userLevel = initialLevel.convertToLevel(connectComparator.compare(update.getCallbackQuery().getData(), level, users),
                        true,
                        true);

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

        System.out.println("resultLevel+++++++++" + resultLevel);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }


    private LevelDTOWrapper createLevel(User users) {

        Level newLevel = new Level();
        LevelDTOWrapper newLevelWrapper = new LevelDTOWrapper();

        newLevelWrapper.setLevel(newLevel);

        List<Basket> basketDTOS = basketCacheRepository
                .findAllByUserIdAndTemp(users.getId(), false);

        System.out.println("basketDTOS+++" + basketDTOS);

        List<Message> messages = new ArrayList<>();
        newLevelWrapper.setMessages(messages);
        List<ButtonRow> buttonRowDTOS = new ArrayList<>();
        newLevelWrapper.setButtonRows(buttonRowDTOS);

        Message message = new Message(null, Map.of(LanguageEnum.RU, "Мой архив корзины: "));
        messages.add(message);

        Map<Integer, List<Basket>> shopBasket = new HashMap<>();
        Map<Integer, Shop> shopGroup = new HashMap<>();

        int i = 0;
        for (Basket basket : basketDTOS) {

            Shop shop = shopCacheRepository.findById(basket.getShopId());
            List<Basket> array = shopBasket.get(shop.getId());

            if (array == null) {
                array = new ArrayList<>();
                shopBasket.put(shop.getId(), array);
                shopGroup.put(shop.getId(), shop);
            }
            array.add(basket);
        }

        for(Map.Entry<Integer, List<Basket>> entry : shopBasket.entrySet()) {

            Integer  shopId = entry.getKey();
            List<Basket> list = entry.getValue();

            final Shop shop = shopGroup.get(shopId);

            i++;
            Message shopMessage = new Message(null, i, Map.of(LanguageEnum.RU, "Магазин " + shop.getName()));
            messages.add(shopMessage);
            i++;
            Message locationMessage = new Message(null, i, shop.getLng(), shop.getLat());
            messages.add(locationMessage);

            for (int j = 0; j < list.size(); j++) {

                Basket basket = list.get(j);

                String text = basket.getNote() + " от " + DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        .withZone(ZoneId.systemDefault()).format(basket.getTime());
                i++;
                Message basketMessage = new Message(newLevel, i, Map.of(LanguageEnum.RU, text));
                messages.add(basketMessage);
                List<BasketProduct> basketProductList = basketProductCacheRepository.findAllByBasketId(basket.getId());

                for (BasketProduct basketProduct : basketProductList) {

                    int id = basketProduct.getProduct();

                    final Product product = productCacheRepository.findById(id);
                    if (product != null) {
                        i++;
                        Message goodsMessage = new Message(newLevel, i, null, product.getImage(),
                                product.getAlias() + stringBeautifier(product.getShortText()) + " " + basketProduct.getProductAmount() + " шт.");
                        messages.add(goodsMessage);
                    }
                }
            }

            ButtonRow shopRow = new ButtonRow();
            Button shopButton = new Button(shopRow, Map.of(LanguageEnum.RU, "Связаться с " + shop.getName()),
                    initialLevel.level_CONNECT_SHOP.getIdString() + shop.getId());
            shopRow.add(shopButton);
            buttonRowDTOS.add(shopRow);

            ButtonRow row4_0 = new ButtonRow();

            Button button4_0_0 = new Button(row4_0, Map.of(LanguageEnum.RU, "Поделиться ссылкой"),
                    levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), CONNECT.name()).getIdString());
            row4_0.add(button4_0_0);
            buttonRowDTOS.add(row4_0);
        }

        if (i == 0) {
            Message basketMessage = new Message(null, i, Map.of(LanguageEnum.RU, "Список товаров в архиве корзины пуст"));
            messages.add(basketMessage);
        }

        return newLevelWrapper;
    }
}
