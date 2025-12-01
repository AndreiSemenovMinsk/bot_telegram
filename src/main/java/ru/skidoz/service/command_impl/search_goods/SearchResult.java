package ru.skidoz.service.command_impl.search_goods;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.BotCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.SEARCH_RESULT;
import static ru.skidoz.util.Structures.getIdString;

/**
 * @author andrey.semenov
 */
@Component
public class SearchResult implements Command {

    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private ProductCacheRepository productRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_RESULT_PRODUCT,
                true,
                false);

        System.out.println();
        System.out.println("-----------------------SearchResult---------------------");
        System.out.println();
        System.out.println(level.getCallName());
        System.out.println(update.getCallbackQuery() != null);


        if (update.getCallbackQuery() != null && level.getCallName().equals(SEARCH_RESULT.name())) {

            System.out.println("update.getCallbackQuery().getData()" + update.getCallbackQuery().getData());

            String code = update.getCallbackQuery().getData().substring(19);
            var product = productRepository.findById(Integer.valueOf(code));

            if (product != null) {

                users.setCurrentConversationShop(product.getChatId());
                userCacheRepository.save(users);

                System.out.println("users.getLanguage()***" + users.getLanguage());

                var shop = shopCacheRepository.findById(product.getShop());

                String description = product.getName(users.getLanguage()) + " " + product.getPrice() + "р. от " + shop.getName();

                System.out.println("code*****" + code);
                System.out.println("product***" + product);
                System.out.println("description+++" + description);
                System.out.println();

                Message message0 = new Message(null, 0, null, product.getImage(), description);
                resultLevel.addMessage(message0);

                Message message1 = new Message(null, Map.of(LanguageEnum.RU, Objects.requireNonNullElse(product.getShortText(), "")));
                resultLevel.addMessage(message1);

                ButtonRow row1 = new ButtonRow();
                Button button1 = new Button(row1, Map.of(LanguageEnum.RU, "Добавить в закладки"), initialLevel.level_ADD_BOOKMARK.getIdString() + product.getId());
                row1.add(button1);
                resultLevel.addRow(row1);
                ButtonRow row2 = new ButtonRow();
                Button button2 = new Button(row2, Map.of(LanguageEnum.RU, "Добавить в корзину"), initialLevel.level_ADD_BASKET.getIdString() + product.getId());
                row2.add(button2);
                resultLevel.addRow(row2);

                ButtonRow shopRow = new ButtonRow();
                Button shopButton = new Button(shopRow, Map.of(LanguageEnum.RU, "Связаться с " + shop.getName()), initialLevel.level_CONNECT_SHOP.getIdString() + product.getShop());
                shopRow.add(shopButton);
                resultLevel.addRow(shopRow);

                Bot bot = botCacheRepository.findByShopId(product.getShop());
                if (bot != null) {
                    ButtonRow row3 = new ButtonRow();
                    Button button3 = new Button(row3, Map.of(LanguageEnum.RU, "Перейти к боту магазина " + product.getName(users.getLanguage())), getIdString(bot.getInitialLevel()));
                    row3.add(button3);
                    resultLevel.addRow(row3);
                }
            }
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
