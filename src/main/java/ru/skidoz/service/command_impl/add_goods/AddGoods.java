package ru.skidoz.service.command_impl.add_goods;

import java.util.Collections;



import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.MenuTypeEnum;
import ru.skidoz.util.Structures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class AddGoods implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {


        String callback = update.getCallbackQuery().getData();
        LevelDTOWrapper resultLevel;
        Long chatId = users.getChatId();

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++AddGoods+++++++++++++++++++++++++++");
        System.out.println(callback);
        System.out.println("******************************" +callback.startsWith("@" + MenuTypeEnum.LEVEL_CHOICER));
        System.out.println();

        if (callback.startsWith("@" + MenuTypeEnum.LEVEL_CHOICER)) {

            String code = callback.substring(("@" + MenuTypeEnum.LEVEL_CHOICER).length()+20);

            System.out.println("code***" + code);
            int id = Structures.parseInt(code);

            Category category = categoryCacheRepository.findById(id);

            System.out.println("category.getName()******" + category.getAlias());
            System.out.println(category);

            Shop shopInitiator = shopCacheRepository.findById(users.getCurrentAdminShop());
            Product product = new Product();
            product.setAlias("creating");
//            productRepository.save(product);
            product.setCategory(category.getId());

            product.setChatId(users.getChatId());
//            category.addProduct(product);
            product.setShop(shopInitiator.getId());
            product.setActive(false);

            Integer/*CategoryGroupDTO*/ categoryGroupId = category.getCategoryGroupId();
            product.setCategoryGroup(categoryGroupId);

//            CatSG categorySuperGroup = category.getCategorySuperGroupId();
            product.setCategorySuperGroup(category.getCategorySuperGroup());


            System.out.println("product------ " + product);

            productCacheRepository.save(product);

            shopInitiator.setCurrentCreatingProduct(product.getId());

            shopCacheRepository.save(shopInitiator);
            //resultLevel = initialLevel.level_ADD_GOODS_NAME;
            resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_GOODS_NAME,
                    true,
                    false);
        } else {
            //resultLevel = initialLevel.level_ADD_GOODS;
            resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_GOODS,
                    true,
                    true);
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
