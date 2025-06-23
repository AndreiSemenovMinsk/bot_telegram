package ru.skidoz.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import com.google.zxing.WriterException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.util.TelegramElementsUtil.qrInputStream;


/**
 * @author andrey.semenov
 */
@Component
@RequiredArgsConstructor
public class TelegramBot {

    @Autowired
    private TelegramProcessor telegramProcessor;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    private Runner mainRunner;
    public Map<User, byte[]> excels = new HashMap<>();
    private Map<String, Runner> telegramKey = new HashMap<>();

    private final ThreadPoolExecutor service = new MyThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());

    @PostConstruct
    public void onStart() throws TelegramApiException {
        mainRunner = new Runner("646773878:AAFxwMEdey27R4AJkhFcWFxicu3SSjrC7Gc");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(mainRunner);

    }



    public User createUser(Long chatId, Update update) throws IOException, WriterException {

        System.out.println("-----------------------------------createUser--------------------------------------------");
        String name = null;
        String alias = null;
        if (update.getMessage() != null) {
            name = update.getMessage().getFrom().getFirstName();
            alias = update.getMessage().getFrom().getUserName();
        } else if (update.getCallbackQuery() != null) {
            name = update.getCallbackQuery().getFrom().getFirstName();
            alias = update.getCallbackQuery().getFrom().getUserName();
        }

        User user = new User(chatId, name);
        user.setLanguage(RU);
        user.setAlias(alias);

        Level currentLevel = initialLevel.level_INITIALIZE;

        System.out.println("initialLevel.level_INITIAL ++++" + initialLevel.level_INITIALIZE.getId());

        user.setCurrentLevelId(currentLevel.getId());

        Level newConnectLevel = initialLevel.cloneLevel(initialLevel.level_CONNECT, user, false, false);//levelRepository.findFirstByUserAndCallName(Users, initialLevel.level_CONNECT.getCallName()).clone(user);

        Message qr = new Message(newConnectLevel, 0, Map.of(RU,"https://t.me/Yandex_locals_bot?start=PI" + user.getId().toString()),
                IOUtils.toByteArray(qrInputStream(user.getId().toString())), "Покажите QR партнеру");
        messageCacheRepository.save(qr);
        Message link = new Message(newConnectLevel, 1, Map.of(RU, "или перешлите ссылку https://t.me/Skido_Bot?start=" + "PI" + user.getId()));
        messageCacheRepository.save(link);

        newConnectLevel.setUserId(user.getId());
        levelCacheRepository.save(newConnectLevel);
        userCacheRepository.save(user);

        return user;
    }

    private static SendPhoto sendPhoto(Long chatId, InputStream inputStream, String subText) {
        SendPhoto messagePhoto2 = new SendPhoto();
        messagePhoto2.setChatId(chatId.toString());
        messagePhoto2.setPhoto(new InputFile(inputStream, subText));
        messagePhoto2.setCaption(subText);

        return messagePhoto2;
    }




    public class Runner extends TelegramLongPollingBot {

        private String key;

        private final ParamQueue WRITE_QUEUE = new ParamQueue();

        private final BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("%d")
                .priority(Thread.MAX_PRIORITY)
                .build();
        private final ThreadPoolExecutor executorService = new MyThreadPoolExecutor(1, factory);


        @Override
        public void onUpdateReceived(Update update) {
            //long timeNow = System.currentTimeMillis();
            System.out.println("update ++++++" + update);

            service.execute(() -> asynchronicUpdateReceive(update));

            //System.out.println("timeNow onWebhookUpdateReceived***" + (System.currentTimeMillis() - timeNow));
        }

        @Override
        public void onUpdatesReceived(List<Update> updates) {
            super.onUpdatesReceived(updates);
        }

        @Override
        public String getBotUsername() {
            return "Yandex_Locals_+bot";
        }

        @Override
        public String getBotToken() {
            return this.key;
        }

        @Override
        public void onRegister() {
            super.onRegister();
        }

        @SneakyThrows
        private void asynchronicUpdateReceive(Update update) {

            long timeNow = System.currentTimeMillis();

            long chatId;
            if (update.hasMessage()) {
                chatId = update.getMessage().getChatId();
            } else if (update.hasCallbackQuery()) {
                chatId = update.getCallbackQuery().getMessage().getChatId();
            } else {
                throw new Exception("не понятно что за сообщение");
            }

            User users = userCacheRepository.findByChatId(chatId);

            boolean newUser = false;

            if (users == null) {
                users = createUser(chatId, update);
                newUser = true;
            }

            Integer currentLevelId = users.getCurrentLevelId();

            //System.out.println("users.getCurrentLevelId();+++++" + users.getCurrentLevelId());


            List<LevelChat> newLevel = telegramProcessor.plainLevelChoicer(currentLevelId, update, users, chatId, newUser);

            for (LevelChat levelChat : newLevel) {
                User users1 = levelChat.getUser();

                System.out.println(users1 + "users1********" + levelChat.getLevel().getLevel());

                if (users1 != null) {
                    System.out.println("CurrentLevelId*******" + levelChat.getLevel().getLevel().getId());

                    users1.setCurrentLevelId(levelChat.getLevel().getLevel().getId());

                    userCacheRepository.save(users1);

                    System.out.println("users1***********" + users1);
                }
            }

            long timeNowNewLevelDrawer = System.currentTimeMillis();


            add(newLevel);

            System.out.println("END full time---*" + (System.currentTimeMillis() - timeNow)
                    + "*--------------------------------------------------------------------"
                    + (System.currentTimeMillis() - timeNowNewLevelDrawer));
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }




        private class ParamQueue extends ConcurrentLinkedQueue<List<LevelChat>> {
            public boolean addAsync(List<LevelChat> levelChatList) {
                boolean success = offer(levelChatList);
                initExecutionAsync();
                return success;
            }
        }

        public Runner(String key) {
            this.key = key;
        }

        void initExecutionAsync() {

            final Future<?> submit = executorService.submit(() -> {
                List<LevelChat> levelChatList = null;
                while ((levelChatList = WRITE_QUEUE.poll()) != null) {

                    for (LevelChat levelChat : levelChatList) {

                        try {
                            Long chatId = levelChat.getChatId();
                            LevelDTOWrapper newLevel = levelChat.getLevel();
                            List<ButtonRow> buttonRows = newLevel.getButtonRows();
                            List<Message> levelMessages = newLevel.getMessages();

                            LanguageEnum language;
                            if (levelChat.getUser() != null) {
                                language = Objects.requireNonNullElse(levelChat.getUser().getLanguage(), RU);
                            } else {
                                language = RU;
                            }

                            boolean buttonsExist = buttonRows.size() > 0;
                            int levelMessagesSize = levelMessages.size();

                            String messageText = null;

                            if (buttonsExist && levelMessages.size() > 0) {
                                messageText = levelMessages.get(levelMessages.size() - 1).getName(language);
                            }

                            if (messageText != null) {
                                levelMessagesSize--;
                            }

                            for (int i = 0; i < levelMessagesSize; i++) {

                                Message message = levelMessages.get(i);

                                if (message.getImage() != null) {

                                    SendPhoto sendPhoto = sendPhoto(chatId, new ByteArrayInputStream(message.getImage()), message.getImageDescription());
                                    executeAsync(sendPhoto);
                                }

                                if (message.getText(language) != null) {
                                    SendMessage messageSend = new SendMessage();
                                    messageSend.setChatId(chatId.toString());
                                    messageSend.setText(message.getText(language));

                                    long timeNow = System.currentTimeMillis();
                                    executeAsync(messageSend);

                                    System.out.println();
                                    System.out.println("********messageSend time*******" + (System.currentTimeMillis() - timeNow));
                                }

                                if (message.getLatitude() != null) {
                                    SendLocation location = new SendLocation();
                                    location.setChatId(chatId.toString());
                                    location.setLongitude(message.getLongitude());
                                    location.setLatitude(message.getLatitude());
                                    executeAsync(location);
                                }
                            }

                            if (buttonsExist) {
                                SendMessage buttonMessage = new SendMessage();
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

                                for (ButtonRow buttonRow : buttonRows) {

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

                                buttonMessage.setText(StringUtils.defaultIfEmpty(messageText, "Опции: "));

//                                System.out.println("buttonMessage+++" + buttonMessage);

                                executeAsync(buttonMessage);

//                                System.out.println("messageText+++" + messageText);
                            }
                        } catch (TelegramApiException e) {
                            System.out.println(e);
                            e.printStackTrace();
                        } catch (Exception e) {
                            System.out.println(e);
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(System.currentTimeMillis());
            });
        }

        public void add(List<LevelChat> levelChatList) {
            WRITE_QUEUE.addAsync(levelChatList);
        }
    }


    public Runner getTelegramKey(String key) {

        System.out.println(key);
        System.out.println("telegramKey.get(key)++++++++++" + telegramKey.get(key));

        return telegramKey.get(key);
    }
}
