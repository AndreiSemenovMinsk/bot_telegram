package ru.skidoz.service.command_impl.construct_shop;

import java.io.IOException;
import java.util.ArrayList;
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
import ru.skidoz.service.InitialLevel;
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
    public List<LevelChat> runCommand(Update update, Level level,  User users) throws IOException, WriterException {

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

            return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setUser(users);
                e.setLevel(resultLevel);
            })));

        }

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);



        Shop shop = new Shop();
        shop.setName(inputText);
        shop.setAdminUser(users.getId());
        shop.setSellerSet(List.of(users.getId()));
        shop.setChatId(users.getChatId());
        shop.setCurrentConversationShopUserChatId(users.getChatId());
        shop.setActive(false);
        shopCacheRepository.save(shop);

        users.setCurrentConstructShop(shop.getId());
        users.setCurrentAdminShop(shop.getId());
        users.setShopOwner(true);
        users.setRole("USER");

        if (users.getSessionId() == null) {
            users.setSessionId(Long.toString((long) (Math.random() * 1000000000000L)));
        }


        Action defaultBasicManualAction = new Action(e -> {
            e.setName("Начисление кешбека вручную");
            e.setDescription("Начисление кешбека вручную");
            e.setShop(shop.getId());
            e.setTowardFriend(false);
            e.setType(ActionTypeEnum.BASIC_MANUAL);
            e.setAccommodateSum(true);
            e.addRatePreviousPurchase(100);
            e.addLevelSum(0);
            e.setRateFuturePurchase(100);
            e.setActive(true);
        });
        actionCacheRepository.save(defaultBasicManualAction);

        Action defaultCouponAction = new Action(e -> {
            e.setName("Дефолтовая по купонам");
            e.setDescription("Дефолтовая по купонам");
            e.setShop(shop.getId());
            e.setTowardFriend(false);
            e.setType(ActionTypeEnum.COUPON_DEFAULT);
            e.setAccommodateSum(true);
            e.setActive(true);
            e.setNumberCoupon(0);
        });
        actionCacheRepository.save(defaultCouponAction);


        Action defaultBasicAction = new Action(e -> {
            e.setName("Дефолтовая по сумме");
            e.setDescription("Дефолтовая по сумме");
            e.setShop(shop.getId());
            e.setTowardFriend(false);
            e.setType(ActionTypeEnum.BASIC_DEFAULT);
            e.setAccommodateSum(true);
            e.addRatePreviousPurchase(0);
            e.addLevelSum(0);
            e.setRateFuturePurchase(100);
            e.setActive(true);
        });
        actionCacheRepository.save(defaultBasicAction);



        if (levelCacheRepository.findFirstByUser_ChatIdAndCallName(users.getChatId(), ADMIN_SHOPS.name()) == null) {
            Level adminShopLevel = initialLevel.cloneLevel(initialLevel.level_ADMIN_SHOPS, users);//levelRepository.findFirstByUserAndCallName(Users, initialLevel.level_BASKET.getCallName()).clone(users);

            System.out.println("adminShopLevel*" + adminShopLevel);

            adminShopLevel.setUserId(users.getId());
            levelCacheRepository.save(adminShopLevel);
        }


        System.out.println("defaultBasicAction+++++++ " + defaultBasicAction);

        shop.setCurrentCreatingAction(defaultBasicAction.getId());

        System.out.println("155 action*********************" + defaultBasicAction.getId());

        shopCacheRepository.save(shop);

        System.out.println("shopCacheRepository.saveNew++++" + shop);

        //users.setCurrentChangingBot(botRepository.findByShopId(shop.getId()));
        userCacheRepository.save(users);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
