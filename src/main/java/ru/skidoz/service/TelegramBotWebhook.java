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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private InitialLevel initialLevel;
    @Autowired
    private MessageCacheRepository messageCacheRepository;

    private Map<String, Runner> telegramKey = new HashMap<>();

    private final ThreadPoolExecutor service = new MyThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());

    //@PostConstruct
    public void onStart() {
        telegramKey.put("646773878:AAHpRkd8CdvcvNLeRZ17mgniXZ5fIPIN1vE", new Runner("646773878:AAHpRkd8CdvcvNLeRZ17mgniXZ5fIPIN1vE"));
        telegramKey.put("5692897772:AAEDw0XEmzMJBWUTjWbBsuH4YD_1TrjjEY0", new Runner("5692897772:AAEDw0XEmzMJBWUTjWbBsuH4YD_1TrjjEY0"));//https://api.telegram.org/bot5692897772:AAEDw0XEmzMJBWUTjWbBsuH4YD_1TrjjEY0/setWebhook?url=https://skidozona.by/telegram/5692897772:AAEDw0XEmzMJBWUTjWbBsuH4YD_1TrjjEY0/
        telegramKey.put("5973647687:AAH5r4Ge-8D6JNufO7LbuNBHyXXxz-RzGC8", new Runner("5973647687:AAH5r4Ge-8D6JNufO7LbuNBHyXXxz-RzGC8"));
        telegramKey.put("5811192913:AAHj_Gp59bsP8CJaNPD0EXJ57K3WqqyrOzI", new Runner("5811192913:AAHj_Gp59bsP8CJaNPD0EXJ57K3WqqyrOzI"));
        telegramKey.put("5948171461:AAEdh54PwEjUZ_534iRLJPQxXlAC4ul1Plg", new Runner("5948171461:AAEdh54PwEjUZ_534iRLJPQxXlAC4ul1Plg"));
        telegramKey.put("5957265275:AAE0aEvthp1STFsYCJQVlp3sVFL0au-oiPI", new Runner("5957265275:AAE0aEvthp1STFsYCJQVlp3sVFL0au-oiPI"));
        telegramKey.put("5512871993:AAHCxYWNlZ2RK1YIpFUfBeWa37rN8fxdxls", new Runner("5512871993:AAHCxYWNlZ2RK1YIpFUfBeWa37rN8fxdxls"));
        telegramKey.put("5886367412:AAGXL1l916hG9RsMvBrT29f22kdfI5NLgpo", new Runner("5886367412:AAGXL1l916hG9RsMvBrT29f22kdfI5NLgpo"));
        telegramKey.put("5868903805:AAGapmcFBCB559TvgB9HXwVy48KteSGFgho", new Runner("5868903805:AAGapmcFBCB559TvgB9HXwVy48KteSGFgho"));
        telegramKey.put("5836197806:AAH7RAoN20CjA3PZXvi49ZSyCc1D7BaawgI", new Runner("5836197806:AAH7RAoN20CjA3PZXvi49ZSyCc1D7BaawgI"));
        telegramKey.put("5976848707:AAHp1_m6Gw7tJv279vfGm8exvPD-aX5D1uY", new Runner("5976848707:AAHp1_m6Gw7tJv279vfGm8exvPD-aX5D1uY"));
        telegramKey.put("5889838648:AAGZq_Sp1FSIAdqAbKdIFIZPDqn9FOcJpdM", new Runner("5889838648:AAGZq_Sp1FSIAdqAbKdIFIZPDqn9FOcJpdM"));
        telegramKey.put("5955051696:AAHHqjyGPnXCrIT23lHQE4uvjJliCrb-v_0", new Runner("5955051696:AAHHqjyGPnXCrIT23lHQE4uvjJliCrb-v_0"));
        telegramKey.put("5793594824:AAEydJJ8egPgSDXf52iCVlQUTh296RPMthU", new Runner("5793594824:AAEydJJ8egPgSDXf52iCVlQUTh296RPMthU"));
        telegramKey.put("5876342467:AAGAJTjDeNnssG5iXVQ1Upp9xgoTlu7hqc8", new Runner("5876342467:AAGAJTjDeNnssG5iXVQ1Upp9xgoTlu7hqc8"));
        telegramKey.put("5943557863:AAEqlWwjX-LBjX8BL3nkNxidmvU2NYrfh0Y", new Runner("5943557863:AAEqlWwjX-LBjX8BL3nkNxidmvU2NYrfh0Y"));
        telegramKey.put("5887173650:AAER4wRjRsLlzXw-DLlTxwCaBGnH0y3H5UM", new Runner("5887173650:AAER4wRjRsLlzXw-DLlTxwCaBGnH0y3H5UM"));
    }


    public Runner getTelegramKey(String key) {

        System.out.println(key);
        System.out.println("telegramKey.get(key)++++++++++" + telegramKey.get(key));

        return telegramKey.get(key);
    }

    @SneakyThrows
    public BotApiMethod onUpdateReceived(Update update, String id, Runner runner) {

        long timeNow = System.currentTimeMillis();

        service.execute(() -> asynchronicUpdateReceive(update, id, runner));

        System.out.println("timeNow onWebhookUpdateReceived***" + (System.currentTimeMillis() - timeNow));

        return null;
    }

    @SneakyThrows
    private void asynchronicUpdateReceive(Update update, String id, Runner runner) {

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
            users = createUser(users, chatId, update);
            users.setRunner(id.getBytes());
            newUser = true;
        }

        Integer currentLevelId = users.getCurrentLevelId();

        List<LevelChat> newLevel = telegramProcessor.plainLevelChoicer(currentLevelId, update, users, chatId, newUser);

        //если ничего не нашли - вернемся к текущему уровню
        if (newLevel == null) {

            System.out.println("AAAAAAAAAAAAAA не найден уровень!!!!!!!!");

            User finalUsers = users;
            newLevel = new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                e.setLevel(initialLevel.convertToLevel(levelCacheRepository.findById(currentLevelId),
                        true,
                        false));
                e.setUser(finalUsers);
                e.setChatId(chatId);
            })));
        }

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

        runner.add(newLevel);

        System.out.println("END full time---*" + (System.currentTimeMillis() - timeNow)
                + "*--------------------------------------------------------------------"
                + (System.currentTimeMillis() - timeNowNewLevelDrawer));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public User createUser(User users, Long chatId, Update update) throws IOException, WriterException {

        System.out.println("-----------------------------------createUser--------------------------------------------");

        if (update.getMessage() != null) {
            System.out.println(update.getMessage().getFrom().getFirstName());

            users = new User(chatId,
                    update.getMessage().getFrom().getFirstName());
        } else if (update.getCallbackQuery() != null) {

            System.out.println(update.getCallbackQuery().getFrom().getFirstName());

            users = new User(chatId,
                    update.getCallbackQuery().getFrom().getFirstName());
        }

        users.setLanguage(RU);
        users.setSessionId(Long.toString((long) (Math.random() * 1000000000000L)));


        Level currentLevel = initialLevel.level_INITIALIZE;

        users.setCurrentLevelId(currentLevel.getId());

        userCacheRepository.save(users);

        Level newConnectLevel = initialLevel.cloneLevel(initialLevel.level_CONNECT, users, false, false);//levelRepository.findFirstByUserAndCallName(Users, initialLevel.level_CONNECT.getCallName()).clone(users);

        Message qr = new Message(newConnectLevel, 0, Map.of(RU,"https://t.me/Skido_Bot?start=" + "PI" + users.getId().toString()),
                IOUtils.toByteArray(qrInputStream(users.getId().toString())), "Покажите QR партнеру");//newConnectLevel.getMessages().get(0);
        messageCacheRepository.save(qr);
        Message link = new Message(newConnectLevel, 1, Map.of(RU, "или перешлите ссылку https://t.me/Skido_Bot?start=" + "PI" + users.getId()));
        messageCacheRepository.save(link);
        Message linkSkidozona = new Message(newConnectLevel, 2, Map.of(RU, "Свяжите с сайтом https://skidozona.by?tm=" + users.getId()));
        messageCacheRepository.save(linkSkidozona);

        newConnectLevel.setUserId(users.getId());
        levelCacheRepository.save(newConnectLevel);
        userCacheRepository.save(users);

        System.out.println();
        System.out.println("users.getCurrentLevel().getId()************************" + users.getCurrentLevelId());
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


    public class Runner extends TelegramWebhookBot {

        private String key;

        private final ParamQueue WRITE_QUEUE = new ParamQueue();

        private final BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("%d")
                .priority(Thread.MAX_PRIORITY)
                .build();
        private final ThreadPoolExecutor executorService = new MyThreadPoolExecutor(1, factory);

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

        void initExecutionAsync() {

            final Future<?> submit = executorService.submit(() -> {
                List<LevelChat> levelChatList = null;
                while ((levelChatList = WRITE_QUEUE.poll()) != null) {

//                    System.out.println(levelChatDTOList + "***" + System.currentTimeMillis() + "---" + Thread.currentThread());

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

                                System.out.println("buttonRows.size()" + buttonRows.size());

                                for (ButtonRow buttonRow : buttonRows) {

                                    System.out.println("buttonRow-------" + buttonRow);

                                    List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                                    for (int j = 0; j < buttonRow.getButtonList().size(); j++) {

                                        Button javaButton = buttonRow.getButtonList().get(j);

                                        System.out.println(javaButton);

                                        if (javaButton.getDisplay()) {

//
//                                            System.out.println("language++++++++++++++++++++++++++++" + language);
//                                            System.out.println("javaButton.getName(language)+++" + javaButton.getName(language));
//                                            System.out.println("javaButton.getCallback()*******" + javaButton.getCallback());
//                                            System.out.println("javaButton.getWebApp()*******" + javaButton.getWebApp());

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
}
