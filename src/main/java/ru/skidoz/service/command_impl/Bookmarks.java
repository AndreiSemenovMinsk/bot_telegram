package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.BasketProduct;
import ru.skidoz.model.pojo.side.Bookmark;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.BOOKMARKS;
import static ru.skidoz.service.command.CommandName.CONNECT;
import static ru.skidoz.util.Structures.stringBeautifier;

/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmarks implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;


    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("----------------------------Bookmarks--------------------------");
        System.out.println();
        System.out.println(level.getCallName());

        if (update.getCallbackQuery() != null) {

            if (update.getCallbackQuery().getData().length() == 19) {

                System.out.println(update.getCallbackQuery().getData());

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

        LevelDTOWrapper newLevelWrapper = initialLevel.convertToLevel(initialLevel.level_BOOKMARKS,
                false,
                false);

        List<Bookmark> bookmarkDTOS = bookmarkCacheRepository.findAllByUserId(users.getId());

        System.out.println("bookmarkDTOS+++" + bookmarkDTOS);

        Message message = new Message(null, Map.of(LanguageEnum.RU, "Мои закладки: "));
        newLevelWrapper.addMessage(message);

        Map<Integer, List<Bookmark>> shopBookmark = new HashMap<>();
        Map<Integer, Shop> shopGroup = new HashMap<>();

        int i = 0;
        for (Bookmark bookmark : bookmarkDTOS) {

            Shop shop = shopCacheRepository.findById(bookmark.getShop());
            List<Bookmark> array = shopBookmark.get(shop.getId());

            if (array == null) {
                array = new ArrayList<>();
                shopBookmark.put(shop.getId(), array);
                shopGroup.put(shop.getId(), shop);
            }
            array.add(bookmark);
        }

        for(Map.Entry<Integer, List<Bookmark>> entry : shopBookmark.entrySet()) {

            Integer  shopId = entry.getKey();
            List<Bookmark> list = entry.getValue();

            final Shop shop = shopGroup.get(shopId);

            i++;
            Message shopMessage = new Message(null, i, Map.of(LanguageEnum.RU, "Магазин " + shop.getName()));
            newLevelWrapper.addMessage(shopMessage);
            i++;
            Message locationMessage = new Message(null, i, shop.getLng(), shop.getLat());
            newLevelWrapper.addMessage(locationMessage);

            for (int j = 0; j < list.size(); j++) {

                Bookmark bookmark = list.get(j);

                final Product product = productCacheRepository.findById(bookmark.getProduct());
                if (product != null) {

                    String text = product.getNameRU() + " от " + DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            .withZone(ZoneId.systemDefault()).format(bookmark.getTime());

                    i++;
                    Message goodsMessage = new Message(
                                null, i, null, product.getImage(),
                                product.getAlias()
                                        + stringBeautifier(product.getShortText())
                                        + " " + text);
                    newLevelWrapper.addMessage(goodsMessage);
                }

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
