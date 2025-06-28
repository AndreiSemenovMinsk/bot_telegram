package ru.skidoz.service.command_impl.add_goods;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Structures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class AddGoodsEnd implements Command {

    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println("***********AddGoodsEnd**************");

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findById(users.getCurrentAdminShop());

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                false);

        try {
            Product product = productCacheRepository.findById(shopInitiator.getCurrentCreatingProduct());
            product.setPrice(BigDecimal.valueOf(Structures.parseLong(inputText)));
            product.setActive(true);
            productCacheRepository.save(product);
        } catch (NumberFormatException formatException){

            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
