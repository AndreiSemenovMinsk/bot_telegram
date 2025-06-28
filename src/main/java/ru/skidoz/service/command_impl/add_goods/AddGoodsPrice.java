package ru.skidoz.service.command_impl.add_goods;

import java.util.Collections;

import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class AddGoodsPrice implements Command {

    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println("***********AddGoodsPrice**************");

        // ;Level resultLevel = level;//initialLevel.level_ADD_GOODS_DESCRIPTION
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                false);
        String inputText = update.getMessage().getText();

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findById(users.getCurrentAdminShop());

        Product product = productCacheRepository.findById(shopInitiator.getCurrentCreatingProduct());
        product.setBigText(inputText);
        productCacheRepository.save(product);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
