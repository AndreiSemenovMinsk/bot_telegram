package ru.skidoz.service.command_impl.construct_shop;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
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
public class ConstructSarafanShare implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {


        System.out.println("+++++++++++++++++++++++++++++++ConstructSarafanShare+++++++++++++++++++++++++++++");

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

        System.out.println();
        System.out.println("ConstructSarafanShare**********" + inputText);
        System.out.println();
        LevelDTOWrapper resultLevel;

        try {
            int value = Integer.parseInt(inputText);

            resultLevel = initialLevel.convertToLevel(level,
                    true,
                    true);

            System.out.println("users+++++" + users);
            System.out.println("users.getCurrentConstructShop()*******" + users.getCurrentConstructShop());

            Shop shop = shopCacheRepository.findById(users.getCurrentConstructShop());

            System.out.println("shop+++" + shop);

            shop.setMinBillShare(BigDecimal.valueOf(Math.min(value, 100)));
            shopCacheRepository.save(shop);

        } catch (NumberFormatException formatException){

            resultLevel = initialLevel.convertToLevel(initialLevel.level_ACTION_RATE_WITHDRAW_BASIC,
                    true,
                    false);

            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Необходимо вводить только числовое значение!")));

        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })), null, null);
    }
}
