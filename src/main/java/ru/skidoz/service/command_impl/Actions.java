package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class Actions implements Command {

    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {

        Shop shop = shopCacheRepository.findById(users.getCurrentAdminShop());
        List<Action> actionList = actionCacheRepository.findAllByShopAndActiveIsTrue(shop.getId());

        System.out.println();
        System.out.println("**************Actions**************");
        System.out.println();
        System.out.println("!actionList.isEmpty()***" + !actionList.isEmpty());
        actionList.forEach(e->System.out.println(e.getName()));

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);

        if (!actionList.isEmpty()) {

           /* Message messageShop = new Message(level, 0, shop.getName() + ": список продуктов", null, null, null, null, new ArrayList<>());
            level.addMessage(messageShop);*/

            for (Action action : actionList) {

                String text;
                if (action.getFuturePurchaseRate() != null) {
                    text = action.getName() + " - кэшбек " + action.getFuturePurchaseRate() + "%";
                } else if (action.getNumberCoupon() != null) {
                    text = action.getName() + " - " + action.getNumberCoupon() + " купонов";
                } else {
                    text = action.getName() + " - невалидная";
                }

                Message messageProduct = new Message(level, Map.of(LanguageEnum.RU, text));
                resultLevel.addMessage(messageProduct);
            }
        }

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
