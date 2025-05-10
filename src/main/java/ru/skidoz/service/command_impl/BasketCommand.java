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
import ru.skidoz.aop.repo.BasketProductCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
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
public class BasketCommand implements Command {
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
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {


        System.out.println();
        System.out.println();
        System.out.println("+++++++++++++++++++Basket+++++++++++++++++");
        System.out.println();
        System.out.println();
        System.out.println("update.getCallbackQuery()+++" + update.getCallbackQuery());


        if (update.getCallbackQuery() != null) {

            System.out.println("update.getCallbackQuery().getData().length()+++" + update.getCallbackQuery().getData().length());

            if (update.getCallbackQuery().getData().length() == 19) {

                /*LevelDTOWrapper userLevel = initialLevel.convertToLevel(levelCacheRepository.findFirstByUsers_ChatIdAndCallName(users.getChatId(), BASKET.name()),
                        true,
                        true);*/

                LevelDTOWrapper userLevel = createLevel(users);

                userLevel.getButtonRows().stream().peek(row -> System.out.println("row.getId()***" + row.getId()))
                        .forEach(row-> row.getButtonList().forEach(button -> System.out.println(button.getId()+"+++" + button.getNameRU())));

                return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(userLevel);
                })));
            } else {

                LevelDTOWrapper userLevel = initialLevel.convertToLevel(connectComparator.compare(update.getCallbackQuery().getData(), level, users),
                        true,
                        true);

                return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(userLevel);
                })));
            }
        }

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);

        System.out.println("resultLevel+++++++++" + resultLevel);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }


    private LevelDTOWrapper createLevel(User users) {

        LevelDTOWrapper newLevelWrapper = initialLevel.convertToLevel(initialLevel.level_BASKET,
                false,
                false);

        List<Basket> basketDTOS = basketCacheRepository
                .findAllByUserIdAndTemp(users.getId(), true);

        System.out.println("basketDTOS+++" + basketDTOS);

        Message message = new Message(null, Map.of(LanguageEnum.RU, "Мои корзины: "));
        newLevelWrapper.addMessage(message);

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
            newLevelWrapper.addMessage(shopMessage);
            i++;
            Message locationMessage = new Message(null, i, shop.getLng(), shop.getLat());
            newLevelWrapper.addMessage(locationMessage);

            for (int j = 0; j < list.size(); j++) {

                Basket basket = list.get(j);

                String text = basket.getNote() + " от " + DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        .withZone(ZoneId.systemDefault()).format(basket.getTime());
                i++;
                Message basketMessage = new Message(null, i, Map.of(LanguageEnum.RU, text));
                newLevelWrapper.addMessage(basketMessage);
                List<BasketProduct> basketProductList = basketProductCacheRepository.findAllByBasketId(basket.getId());

                for (BasketProduct basketProduct : basketProductList) {

                    int id = basketProduct.getProduct();

                    final Product product = productCacheRepository.findById(id);
                    if (product != null) {
                        i++;
                        Message goodsMessage = new Message(null, i, null, product.getImage(),
                                product.getAlias() + stringBeautifier(product.getShortText()) + " " + basketProduct.getProductAmount() + " шт.");
                        newLevelWrapper.addMessage(goodsMessage);
                    }
                }
            }

            ButtonRow shopRow = new ButtonRow();
            Button shopButton = new Button(shopRow, Map.of(LanguageEnum.RU, "Связаться с " + shop.getName()),
                    initialLevel.level_CONNECT_SHOP.getIdString() + shop.getId());
            shopRow.add(shopButton);
            newLevelWrapper.addRow(shopRow);

            ButtonRow row4_0 = new ButtonRow();

            Button button4_0_0 = new Button(row4_0, Map.of(LanguageEnum.RU, "Поделиться ссылкой"),
                    levelCacheRepository.findFirstByUsers_ChatIdAndCallName(users.getChatId(), CONNECT.name()).getIdString());
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
