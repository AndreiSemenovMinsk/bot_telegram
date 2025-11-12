package ru.skidoz.service.command_impl.construct_shop;

import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Data;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.command.Command;
import ru.skidoz.service.initializers.InitialLevel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class Construct implements Command {

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
    public LevelResponse runCommand(Update update, Level level,  User users)
            throws IOException, WriterException, UnirestException {

        String inputText = update.getMessage().getText();
        System.out.println();
        System.out.println();
        System.out.println("*****************************Construct**********");
        System.out.println();
        System.out.println();

        System.out.println(shopCacheRepository.findBySecretHash(inputText) != null);

        if (shopCacheRepository.findBySecretHash(inputText) != null) {
            LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_CONSTRUCT,
                    false,
                    false);

            Message message = new Message(null, Map.of(LanguageEnum.RU, "Ошибка! Токен " + inputText + " уже зарегистрирован! "));
            resultLevel.addMessage(message);

            System.out.println("++++++++-+-+-+-+-+-+++-+");

            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setUser(users);
                e.setLevel(resultLevel);
            })), null, null);
        }


        Unirest.setTimeouts(0, 0);
        String responseString = Unirest.post(
                "https://api.telegram.org/bot"
                        + inputText
                        + "/setWebhook?url=https://skidozona.by/telegram/" + inputText + "/")
                .asString().getBody();
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("responseString+++++" + responseString);


        Response response = mapper.readValue(responseString, Response.class);

        if (!response.isOk()) {
            LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_CONSTRUCT,
                    false,
                    false);

            Message message = new Message(null, Map.of(LanguageEnum.RU, "Ошибка при регистрации токена "));
            resultLevel.addMessage(message);

            System.out.println("++++++++-+-+-+-+-+-+++-+" + response.getDescription());

            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setUser(users);
                e.setLevel(resultLevel);
            })), null, null);
        }

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);

        Shop shop = new Shop();
        shop.setSecretHash(inputText);
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
            e.setFuturePurchaseRate(100);
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
            e.setFuturePurchaseRate(100);
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

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }

    @Data
    private static class Response {
        private boolean ok;
        private int error_code;
        private String description;
        private String result;
    }

}
