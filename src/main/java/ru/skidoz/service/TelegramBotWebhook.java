package ru.skidoz.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopUserCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.side.ShopUser;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.skidoz.service.initializers.InitialLevel;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.util.TelegramElementsUtil.qrInputStream;


/**
 * @author andrey.semenov
 */
@Component
@RequiredArgsConstructor
public class TelegramBotWebhook {
    @Autowired
    private TelegramProcessor telegramProcessor;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopUserCacheRepository shopUserCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private Sender sender;

    public Map<User, byte[]> excels = new HashMap<>();

    private final ThreadPoolExecutor service = new MyThreadPoolExecutor(
            Runtime
                    .getRuntime()
                    .availableProcessors(), Executors.defaultThreadFactory());

    @SneakyThrows
    public BotApiMethod onUpdateReceived(Update update, String id) {

        long timeNow = System.currentTimeMillis();

        service.execute(() -> asynchronicUpdateReceive(update, id));

        System.out.println("timeNow onWebhookUpdateReceived***" + (System.currentTimeMillis()
                - timeNow));

        return null;
    }

    @SneakyThrows
    private void asynchronicUpdateReceive(Update update, String secretHash) {

        long timeNow = System.currentTimeMillis();

        long chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            throw new Exception("не понятно что за сообщение");
        }

        User user = userCacheRepository.findByChatId(chatId);
        Shop shop = shopCacheRepository.findBySecretHash(secretHash);


        System.out.println("secretHash--------" + secretHash);

        boolean newUser = false;
        ShopUser shopUser = null;

        if (user == null) {
            user = createUser(chatId, update);

            newUser = true;
        } else {
            shopUser = shopUserCacheRepository.findByUserAndShop(user.getId(), shop.getId());

            System.out.println("shopUser: " + shopUser + "\n user.getId(): " + user.getId() + " shop.getId(): " + shop.getId());
        }

        if (shopUser == null) {
            shopUser = createShopUser(user, shop);
        }

        Integer currentLevelId = shopUser.getCurrentLevelId();

        System.out.println("shopUser-----" + shopUser);

        LevelResponse levelResponse = telegramProcessor.plainLevelChoicer(
                currentLevelId,
                update,
                user,
                chatId,
                newUser,
                secretHash);

        if (levelResponse.getKey() == null) {
            levelResponse.setKey(secretHash);
        }

        List<LevelChat> newLevel = levelResponse.getLevelChats();
        List<LevelChat> newLevelAfterSave = levelResponse.getLevelChatsAfterSave();

        //если ничего не нашли - вернемся к текущему уровню
        if (newLevel == null || newLevel.isEmpty()) {

            System.out.println("AAAAAAAAAAAAAA не найден уровень!!!!!!!!");

            User finalUsers = user;
            levelResponse.setLevelChats(new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                e.setLevel(initialLevel.convertToLevel(
                        levelCacheRepository.findById(currentLevelId),
                        true,
                        false));
                e.setUser(finalUsers);
                e.setChatId(chatId);
            }))));
        } else {

            for (LevelChat levelChat : newLevel) {
                User users1 = levelChat.getUser();

                if (users1 != null) {

                    System.out.println(users1 + "users1********" + levelChat.getLevel().getLevel());

                    ShopUser shopUser1 = null;
                    if (shopUser.getUser() == users1.getId()) {
                        shopUser1 = shopUser;
                    } else {
                        shopUser1 = shopUserCacheRepository.findByUserAndShop(users1.getId(), shop.getId());
                    }


                    System.out.println("CurrentLevelId*******" + levelChat
                            .getLevel()
                            .getLevel()
                            .getId());

                    shopUser1.setCurrentLevelId(levelChat.getLevel().getLevel().getId());

                    levelCacheRepository.cache(levelChat.getLevel().getLevel());

                    shopUserCacheRepository.save(shopUser1);
                    userCacheRepository.save(users1);

                    System.out.println("users1***********" + users1);
                }
            }
        }

        sender.addAsync(levelResponse);

        if (newLevelAfterSave != null && !newLevelAfterSave.isEmpty()) {
            sender.addAfterSave(levelResponse);
        }

        long timeNowNewLevelDrawer = System.currentTimeMillis();

        System.out.println("END full time---*" + (System.currentTimeMillis() - timeNow)
                + "*--------------------------------------------------------------------"
                + (System.currentTimeMillis() - timeNowNewLevelDrawer));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private User createUser(Long chatId, Update update)
            throws IOException, WriterException {
        System.out.println("-----------------------------------createUser--------------------------------------------");

        User user = null;
        if (update.getMessage() != null) {
            System.out.println(update.getMessage().getFrom().getFirstName());

            user = new User(
                    chatId,
                    update.getMessage().getFrom().getFirstName());
        } else if (update.getCallbackQuery() != null) {

            System.out.println(update.getCallbackQuery().getFrom().getFirstName());

            user = new User(
                    chatId,
                    update.getCallbackQuery().getFrom().getFirstName());
        }
        user.setLanguage(RU);
//        user.setSessionId(Long.toString((long) (Math.random() * 1000000000000L)));

        userCacheRepository.save(user);


        Level newConnectLevel = initialLevel.cloneLevel(
                initialLevel.level_CONNECT,
                user,
                false,
                false);//levelRepository.findFirstByUserAndCallName(Users, initialLevel.level_CONNECT.getCallName()).clone(user);

        Message qr = new Message(
                newConnectLevel, 0,
                Map.of(RU, "https://t.me/Skido_Bot?start=" + "PI" + user.getId().toString()),
                IOUtils.toByteArray(qrInputStream(user.getId().toString())),
                "Покажите QR партнеру");//newConnectLevel.getMessages().get(0);
        messageCacheRepository.save(qr);
        Message link = new Message(
                newConnectLevel, 1,
                Map.of(
                        RU,
                        "или перешлите ссылку https://t.me/Skido_Bot?start="
                                + "PI"
                                + user.getId()));
        messageCacheRepository.save(link);
        Message linkSkidozona = new Message(
                newConnectLevel, 2,
                Map.of(RU, "Свяжите с сайтом https://skidozona.by?tm=" + user.getId()));
        messageCacheRepository.save(linkSkidozona);

        newConnectLevel.setUserId(user.getId());
        levelCacheRepository.save(newConnectLevel);


//        initialLevel.cloneLevel(initialLevel.level_MY_SHOPS, user, false, false);
//        initialLevel.cloneLevel(initialLevel.level_BASKET, user, false, false);
//        initialLevel.cloneLevel(initialLevel.level_BASKET_ARCHIVE, user, false, false);
//        initialLevel.cloneLevel(initialLevel.level_BOOKMARKS, user, false, false);
//        initialLevel.cloneLevel(initialLevel.level_CASHBACKS, user, false, false);

        return user;
    }


    private ShopUser createShopUser(User user, Shop shop) {

        System.out.println("shop.getInitialLevelId()*****" + shop.getInitialLevelId());

        var shopUser = new ShopUser(shop.getId(), user.getId(), shop.getInitialLevelId());

        user.setFirstRunnerShop(shop.getId());
        userCacheRepository.save(user);

        System.out.println("shopUser++++" + shopUser);

        return shopUserCacheRepository.save(shopUser);
    }

}
