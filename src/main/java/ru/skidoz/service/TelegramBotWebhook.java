package ru.skidoz.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import jakarta.annotation.PostConstruct;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopUserCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.side.ShopUser;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.skidoz.service.initializers.InitialLevel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ThemeResolver;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    public Map<User, byte[]> excels = new HashMap<>();

    private List<Runner> runners = new ArrayList<>();

    private final ThreadPoolExecutor service = new MyThreadPoolExecutor(
            Runtime
                    .getRuntime()
                    .availableProcessors(), Executors.defaultThreadFactory());

    @PostConstruct
    public void onStart() {
        for (int i = 0; i < 16; i++) {
            runners.add(new Runner());
        }
    }

    @SneakyThrows
    public BotApiMethod onUpdateReceived(Update update, String id) {

        long timeNow = System.currentTimeMillis();

        service.execute(() -> asynchronicUpdateReceive(update, id));

        System.out.println("timeNow onWebhookUpdateReceived***" + (System.currentTimeMillis()
                - timeNow));

        return null;
    }

    @SneakyThrows
    private void asynchronicUpdateReceive(Update update, String secretId) {

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
        Shop shop = shopCacheRepository.findBySecretId(secretId);

        boolean newUser = false;

        if (user == null) {
            user = createUser(user, chatId, update, shop);
            //user.setRunner(id.getBytes());
            newUser = true;
        }

        Integer currentLevelId = shopUserCacheRepository.findByUserAndShop(user.getId(), shop.getId()).getCurrentLevelId();

        LevelResponse levelResponse = telegramProcessor.plainLevelChoicer(
                currentLevelId,
                update,
                user,
                chatId,
                newUser,
                secretId);

        List<LevelChat> newLevel = levelResponse.getLevelChats();
        List<LevelChat> newLevelAfterSave = levelResponse.getLevelChatsAfterSave();

        //если ничего не нашли - вернемся к текущему уровню
        if (newLevel == null) {

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
        }

        for (LevelChat levelChat : newLevel) {
            User users1 = levelChat.getUser();

            System.out.println(users1 + "users1********" + levelChat.getLevel().getLevel());

            if (users1 != null) {
                System.out.println("CurrentLevelId*******" + levelChat
                        .getLevel()
                        .getLevel()
                        .getId());

                shopUserCacheRepository.save(new ShopUser(shop.getId(), user.getId(), levelChat.getLevel().getLevel().getId()));
                userCacheRepository.save(users1);

                System.out.println("users1***********" + users1);
            }
        }

        long timeNowNewLevelDrawer = System.currentTimeMillis();

        addAsync(levelResponse);

        if (newLevelAfterSave != null) {
            addAfterSave(levelResponse);
        }

        System.out.println("END full time---*" + (System.currentTimeMillis() - timeNow)
                + "*--------------------------------------------------------------------"
                + (System.currentTimeMillis() - timeNowNewLevelDrawer));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public User createUser(User users, Long chatId, Update update, Shop shop)
            throws IOException, WriterException {
        System.out.println(
                "-----------------------------------createUser--------------------------------------------");
        if (update.getMessage() != null) {
            System.out.println(update.getMessage().getFrom().getFirstName());

            users = new User(
                    chatId,
                    update.getMessage().getFrom().getFirstName());
        } else if (update.getCallbackQuery() != null) {

            System.out.println(update.getCallbackQuery().getFrom().getFirstName());

            users = new User(
                    chatId,
                    update.getCallbackQuery().getFrom().getFirstName());
        }
        users.setLanguage(RU);
//        users.setSessionId(Long.toString((long) (Math.random() * 1000000000000L)));



        shopUserCacheRepository.save(new ShopUser(shop.getId(), users.getId(), shop.getInitialLevelId()));

        //Level currentLevel = initialLevel.level_INITIALIZE;


        userCacheRepository.save(users);

        Level newConnectLevel = initialLevel.cloneLevel(
                initialLevel.level_CONNECT,
                users,
                false,
                false);//levelRepository.findFirstByUserAndCallName(Users, initialLevel.level_CONNECT.getCallName()).clone(users);

        Message qr = new Message(
                newConnectLevel, 0,
                Map.of(RU, "https://t.me/Skido_Bot?start=" + "PI" + users.getId().toString()),
                IOUtils.toByteArray(qrInputStream(users.getId().toString())),
                "Покажите QR партнеру");//newConnectLevel.getMessages().get(0);
        messageCacheRepository.save(qr);
        Message link = new Message(
                newConnectLevel, 1,
                Map.of(
                        RU,
                        "или перешлите ссылку https://t.me/Skido_Bot?start="
                                + "PI"
                                + users.getId()));
        messageCacheRepository.save(link);
        Message linkSkidozona = new Message(
                newConnectLevel, 2,
                Map.of(RU, "Свяжите с сайтом https://skidozona.by?tm=" + users.getId()));
        messageCacheRepository.save(linkSkidozona);

        newConnectLevel.setUserId(users.getId());
        levelCacheRepository.save(newConnectLevel);
//        userCacheRepository.save(users);

        System.out.println();

        return users;
    }

    private static SendPhoto sendPhoto(Long chatId, InputStream inputStream, String subText) {
        SendPhoto messagePhoto2 = new SendPhoto();
        messagePhoto2.setChatId(chatId.toString());
        messagePhoto2.setPhoto(new InputFile(inputStream, subText));
        messagePhoto2.setCaption(subText);

        return messagePhoto2;
    }


    private final BasicThreadFactory factory = new BasicThreadFactory.Builder()
            .namingPattern("%d")
            .priority(Thread.MAX_PRIORITY)
            .build();
    private final ThreadPoolExecutor executorService = new MyThreadPoolExecutor(16, factory);
    private ConcurrentLinkedQueue<LevelResponse> WRITE_QUEUE = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<LevelResponse> WRITE_AFTER_SAVE_QUEUE = new ConcurrentLinkedQueue<>();

    void initExecutionAsync() {
        executorService.submit(() -> {
            LevelResponse levelChatList = null;
            while ((levelChatList = WRITE_QUEUE.poll()) != null) {
                try {
                    final int threadInd = Integer.parseInt(Thread.currentThread().getName());
                    Runner r = runners.get(threadInd);
                    r.processSend(levelChatList.getLevelChats(), levelChatList.getKey());

                } catch (Exception e) {
                    e.printStackTrace();
                    java.lang.System.out.println(e);
                }
            }
        });
    }

    public void initExecutionAfterSave() {
        executorService.submit(() -> {
            LevelResponse levelChatList = null;
            while ((levelChatList = WRITE_AFTER_SAVE_QUEUE.poll()) != null) {
                try {
                    final int threadInd = Integer.parseInt(Thread.currentThread().getName());
                    Runner r = runners.get(threadInd);
                    r.processSend(levelChatList.getLevelChatsAfterSave(), levelChatList.getKey());

                } catch (Exception e) {
                    e.printStackTrace();
                    java.lang.System.out.println(e);
                }
            }
        });
    }

    public void addAsync(LevelResponse levelResponse) {
        WRITE_QUEUE.offer(levelResponse);
        initExecutionAsync();
    }

    private void addAfterSave(LevelResponse levelResponse) {
        WRITE_AFTER_SAVE_QUEUE.offer(levelResponse);
    }


    public class Runner extends TelegramWebhookBot {

        private String key;

        @Override
        public String getBotUsername() {
            return "Skido_Bot";
        }

        @Override
        public String getBotToken() {
            return this.key;
        }

        @Override
        public BotApiMethod onWebhookUpdateReceived(Update update) {
            return null;
        }

        @Override
        public String getBotPath() {
            return null;
        }

        public void processSend(List<LevelChat> levelChatList, String secretId) {

                for (LevelChat levelChat : levelChatList) {
                    try {
                        this.key = secretId;

                        Long chatId = levelChat.getChatId();
                        LevelDTOWrapper newLevel = levelChat.getLevel();
                        List<ButtonRow> buttonRows = newLevel.getButtonRows();
                        List<Message> levelMessages = newLevel.getMessages();

                        LanguageEnum language;
                        if (levelChat.getUser() != null) {
                            language = Objects.requireNonNullElse(
                                    levelChat.getUser().getLanguage(),
                                    RU);
                        } else {
                            language = RU;
                        }

                        boolean buttonsExist = buttonRows.size() > 0;
                        int levelMessagesSize = levelMessages.size();

                        String messageText = null;

                        if (buttonsExist && levelMessages.size() > 0) {
                            messageText = levelMessages
                                    .get(levelMessages.size() - 1)
                                    .getName(language);
                        }

                        if (messageText != null) {
                            levelMessagesSize--;
                        }

                        for (int i = 0; i < levelMessagesSize; i++) {

                            Message message = levelMessages.get(i);

                            if (message.getImage() != null) {

                                SendPhoto sendPhoto = sendPhoto(
                                        chatId,
                                        new ByteArrayInputStream(message.getImage()),
                                        message.getImageDescription());
                                execute(sendPhoto);
                            }

                            if (message.getText(language) != null) {
                                SendMessage messageSend = new SendMessage();
                                messageSend.setChatId(chatId.toString());
                                messageSend.setText(message.getText(language));

                                long timeNow = System.currentTimeMillis();
                                execute(messageSend);

                                System.out.println();
                                System.out.println("********messageSend time*******"
                                        + (System.currentTimeMillis() - timeNow));
                            }

                            if (message.getLatitude() != null) {
                                SendLocation location = new SendLocation();
                                location.setChatId(chatId.toString());
                                location.setLongitude(message.getLongitude());
                                location.setLatitude(message.getLatitude());
                                execute(location);
                            }
                        }

                        if (buttonsExist) {
                            SendMessage buttonMessage = new SendMessage();
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

                            System.out.println("buttonRows.size()" + buttonRows.size());

                            for (ButtonRow buttonRow : buttonRows) {

                                System.out.println("buttonRow-------" + buttonRow);

                                List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                                for (int j = 0; j < buttonRow.getButtonList().size(); j++) {

                                    Button javaButton = buttonRow.getButtonList().get(j);

                                    System.out.println(javaButton);

                                    if (javaButton.getDisplay()) {

                                        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                                        keyboardButton.setText(javaButton.getName(language));

                                        if (javaButton.getWebApp() != null) {
                                            keyboardButton.setWebApp(new WebAppInfo(javaButton.getWebApp()));
                                            keyboardButton.setUrl(javaButton.getWebApp());
                                        } else {
                                            keyboardButton.setCallbackData(javaButton.getCallback());
                                        }

                                        keyboardButtonsRow.add(keyboardButton);
                                    } else {
                                        javaButton.setDisplay(true);
                                    }
                                }
                                rowList.add(keyboardButtonsRow);
                            }
                            inlineKeyboardMarkup.setKeyboard(rowList);

                            buttonMessage.setReplyMarkup(inlineKeyboardMarkup);
                            buttonMessage.setChatId(chatId.toString());

                            buttonMessage.setText(StringUtils.defaultIfEmpty(
                                    messageText,
                                    "Опции: "));

                            execute(buttonMessage);
                        }
                    } catch (TelegramApiException e) {
                        System.out.println(e);
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }

                System.out.println(System.currentTimeMillis());
        }
    }

}
