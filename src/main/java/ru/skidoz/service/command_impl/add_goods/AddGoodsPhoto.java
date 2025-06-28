package ru.skidoz.service.command_impl.add_goods;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.search.SearchService;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.util.TextParser.textParser;


/**
 * @author andrey.semenov
 */
@Component
public class AddGoodsPhoto implements Command {

    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException, IOException {


        System.out.println("**************AddGoodsPhoto************");

        String inputText = update.getMessage().getText();

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                false);

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findById(users.getCurrentAdminShop());

        Product product = productCacheRepository.findById(shopInitiator.getCurrentCreatingProduct());
        product.setAlias(inputText);
        product.setNameRU(inputText);
        productCacheRepository.save(product);

        List<String> productWords = textParser(product.getAlias());
        productWords.addAll(textParser(product.getShortText()));

        productWords.forEach(e -> System.out.println("productWords***++++" + e));

        searchService.nameWordMapper(productWords, product);

        /*List<NameWord> nameWords = searchService.nameWordMapper(productWords, product);
        product.setNameWordSet(new HashSet<>(nameWords));
        productRepository.save(product);*/

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
