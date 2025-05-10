package ru.skidoz.service.command_impl.action.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class AddActionBasic implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_BASIC,
                true,
                false);

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("**************************************AddActionBasic*************************************");
        System.out.println();
        System.out.println();
        System.out.println("shopInitiator*******" + shopInitiator.getName());

        System.out.println("-------------------------------------------------------------------------");

        Action action = new Action(e -> {
            e.setName("Дефолтовый");
            e.setDescription("Дефолтовый");
            e.setShop(shopInitiator.getId());
            e.setTowardFriend(false);
            e.setType(ActionTypeEnum.BASIC);
            e.setAccommodateSum(true);
            e.setActive(false);
            //e.setLevelRatePreviousPurchaseList("100");
        });
        actionCacheRepository.save(action);

        System.out.println("68 action*********************" + action);

        shopInitiator.setCurrentCreatingAction(action.getId());
        shopCacheRepository.save(shopInitiator);

        System.out.println("shopInitiator.getCurrentCreatingAction()-----------" + shopInitiator.getCurrentCreatingAction());

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
