package ru.skidoz.service.command_impl.construct_shop;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Structures;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;

/**
 * @author andrey.semenov
 */
@Component
public class ConstructAdd implements Command {

    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException, CloneNotSupportedException {

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

        LevelDTOWrapper resultLevel;

        System.out.println();
        System.out.println();
        System.out.println("*****************************ConstructAdd**********"+ inputText);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("ConstructAdd**********" + inputText);
        System.out.println();

        try {
            long value = Structures.parseLong(inputText);

            resultLevel = initialLevel.convertToLevel(level,
                    true,
                    true);

            System.out.println();

            Shop shop = shopCacheRepository.findById(users.getCurrentConstructShop());
            shop.setSarafanShare(BigDecimal.valueOf(value > 100 ? 100 : value));
            shop.setActive(true);
            shopCacheRepository.save(shop);

            if (levelCacheRepository.findFirstByUsers_ChatIdAndCallName(users.getChatId(), ADMIN_SHOPS.name()) == null) {
                Level adminShopLevel = initialLevel.cloneLevel(initialLevel.level_ADMIN_SHOPS, users);//levelRepository.findFirstByUserAndCallName(Users, initialLevel.level_BASKET.getCallName()).clone(users);

                System.out.println("adminShopLevel*" + adminShopLevel);

                adminShopLevel.setUserId(users.getId());
                levelCacheRepository.save(adminShopLevel);
            }
            //adder.addAdminShop(users);

        } catch (NumberFormatException formatException){

            resultLevel = initialLevel.convertToLevel(initialLevel.level_CONSTRUCT_SARAFAN_SHARE,
                    true,
                    false);

            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Необходимо вводить только числовое значение!")));
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}
