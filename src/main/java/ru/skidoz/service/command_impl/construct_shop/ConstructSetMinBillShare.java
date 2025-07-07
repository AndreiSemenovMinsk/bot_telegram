package ru.skidoz.service.command_impl.construct_shop;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;

/**
 * @author andrey.semenov
 */
@Component
public class ConstructSetMinBillShare implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level,  User users) throws IOException, WriterException {

        String inputText = update.getMessage().getText();
        System.out.println();
        System.out.println();
        System.out.println("*****************************ConstructSetMinBillShare**********"+ inputText);
        System.out.println();
        System.out.println();


        System.out.println(shopCacheRepository.findByName(inputText) != null);

        if (shopCacheRepository.findByName(inputText) != null) {

            LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_CONSTRUCT,
                    false,
                    false);

            Message message = new Message(null, Map.of(LanguageEnum.RU, "Ошибка! Название " + inputText + " уже зарегистрировано! "));
            resultLevel.addMessage(message);

            System.out.println("++++++++-+-+-+-+-+-+++-+");

            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setUser(users);
                e.setLevel(resultLevel);
            })), null, null);

        }

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);

        final Integer currentConstructShop = users.getCurrentConstructShop();
        final Shop shop = shopCacheRepository.findById(currentConstructShop);

        shop.setName(inputText);
        shopCacheRepository.save(shop);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
