package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class GoodsList implements Command {

    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        //Shop shop = shopRepository.findById(users.getCurrentAdminShop());
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_GOODS_LIST,
                true,
                true);
        //List<Prd> productList = shop.getProductList();

        List<Product> productList = productCacheRepository.findAllByShop_Id(users.getCurrentAdminShop());

        System.out.println();
        System.out.println();
        System.out.println("+++++++++++++++GoodsList++++++++++++++++");
        System.out.println();
        System.out.println("users.getCurrentAdminShop()*******" + users.getCurrentAdminShop());
        System.out.println(!productList.isEmpty());

        if (!productList.isEmpty()) {

            for (Product product : productList) {

                System.out.println("product.getName()+++++++" + product.getAlias());

                Message messageProduct = new Message(null, 0,
                        null, product.getImage(), product.getName(users.getLanguage()) + " " + product.getPrice() + "p.");
                resultLevel.addMessage(messageProduct);
            }
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
