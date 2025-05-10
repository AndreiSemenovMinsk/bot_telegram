package ru.skidoz.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.command.Command;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.service.command.CommandProvider;
import ru.skidoz.util.XMLGettingService;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.util.Structures.parseInt;

/**
 * @author andrey.semenov
 */
@Component
public class TelegramProcessor {

    @Autowired
    private CommandProvider commandProvider;

    @Autowired
    private LevelCacheRepository levelCacheRepository;

    @Autowired
    private InitialLevel initialLevel;

    @Autowired
    private XMLGettingService xmlGettingService;



    public ConcurrentMap<String, String> mergeUsers = new ConcurrentHashMap<>();


    private static final String MANAGEMENT_FILE = "config/management.xls";

//    private final Logger log = LoggerFactory.getLogger(TelegramProcessor.class);


    private List<LevelChat> startProcessor(User users, long chatId, Update update, boolean newUser) throws CloneNotSupportedException {

        String inputText = update.getMessage().getText();
        String bearingCommand = inputText.substring("/start=".length(), "/start=".length() + 2);
        String bearingId = inputText.substring("/start=".length() + 2);

        System.out.println("bearingCommand***" + bearingCommand);
        System.out.println("bearingId***" + bearingId);

        //тот, от кого пришло сообщение - значит, тот, кто прочитал qr или перешел по ссылке
        List<LevelChat> levelChatList = new ArrayList<>();

        if (bearingCommand.equals("PP")) {

                User friend = null;
                List<LevelChat> levelChatList2 = new ArrayList<>();

//                if (newUser) {
//                    levelChatDTOList.addAll(p2PNewBuyerLinkStarter.getLevel(chatId, users, friend));
//                }
//
//                levelChatDTOList.addAll(levelChatDTOList2);
            }
        System.out.println("levelChatDTOList+++++++++++++++" + levelChatList);

        return levelChatList;
    }


    List<LevelChat> plainLevelChoicer(Integer currentLevelId, Update update, User users, long chatId, boolean newUser) throws Exception {

        Level newLevel = null;
        if (update.hasMessage()) {

            String inputText = update.getMessage().getText();
            Location location = update.getMessage().getLocation();

            if (inputText != null && inputText.startsWith("/start ")) {
                return startProcessor(users, chatId, update, newUser);
            }
            //TODO - make refactoring, may not be null, but empty...
            if (inputText != null || update.getMessage().hasPhoto()) {

                if (currentLevelId == 0) {
                    return null;
                }

                System.out.println("inputText*" + inputText);

                newLevel = levelCacheRepository.findFirstBySourceIsMessageIsTrueAndParentLevel_Id(currentLevelId);

                System.out.println("newLevel   findFirstBySourceIsMessageIsTrueAndParentLevelId***" + newLevel);

                System.out.println("all child levels " + levelCacheRepository.findAllByParentLevel_Id(currentLevelId));

                if (newLevel == null) {
                    final Level currentLevel = levelCacheRepository.findById(currentLevelId);

                    System.out.println("currentLevel++++++++++++++++" + currentLevel);

                    String levelCallName = currentLevel.getCallName();
                    Command command = commandProvider.getCommand(levelCallName);

                    return command.runCommand(update, newLevel, users);
                }

                String levelCallName = newLevel.getCallName();

                System.out.println("newLevel.getIsBotLevel()***" + newLevel.isBotLevel());

                if (commandProvider.commandExists(levelCallName)) {
                    Command command = commandProvider.getCommand(levelCallName);
                    return command.runCommand(update, newLevel, users);
                }

                throw new RuntimeException("Не смогли обработать сообщение");
            }
        }

        if (update.hasCallbackQuery()) {
            String callback = update.getCallbackQuery().getData();

            System.out.println("callback*****" + callback);

            if (callback.startsWith("@")) {
                newLevel = levelCacheRepository.findById(currentLevelId);
            } else {
                newLevel = levelCacheRepository.findById(parseInt(callback.substring(0, 19)));
            }

            if (newLevel != null) {
                System.out.println("*****newLevel.getCallName() " + newLevel.getCallName());
            }

            System.out.println(newLevel != null);
            System.out.println("commandExists+++" + commandProvider.commandExists(newLevel.getCallName()));
            // работает на новый level

            String levelCallName = newLevel.getCallName();

            System.out.println("newLevel.getIsBotLevel()+++" + newLevel.isBotLevel());

            if (commandProvider.commandExists(levelCallName)) {
                Command command = commandProvider.getCommand(levelCallName);
                return command.runCommand(update, newLevel, users);
            } else {

                LevelDTOWrapper levelDTOWrapper = initialLevel.convertToLevel(newLevel,
                        true,
                        true);

                return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(chatId);
                    e.setUser(users);
                    e.setLevel(levelDTOWrapper);
                })));
            }
        }

// если ничего не совпало - возвратим текущий
        if (newLevel == null) {
            return null;
        }

        LevelDTOWrapper levelDTOWrapper = initialLevel.convertToLevel(newLevel,
                true,
                true);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(levelDTOWrapper);
        })));
    }

    public static InputStream qrInputStream(String text) throws WriterException, IOException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public void buttonDisabler(LevelDTOWrapper buyerLevel, String name, LanguageEnum language) {
        buyerLevel.getButtonRows().stream()
                .flatMap(row -> row.getButtonList().stream())
                .filter(button -> button.getName(language).equals(name))
                .forEach(button -> button.setDisplay(false));
    }


    public Message convertUpdateToMessage(Update update, User users) throws IOException, UnirestException {

        Message message = null;
        if (update.hasMessage()) {
            if (update.getMessage().hasPhoto()) {

                List<PhotoSize> photos = update.getMessage().getPhoto();
                /*String f_id = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getFileId();
                InputStream is = xmlGettingService.getXMLRatesStream("https://api.telegram.org/file/bot" + telegramBot.getBotToken() + "/" + f_id);

                byte[] targetArray = new byte[is.available()];
                is.read(targetArray);*/

                System.out.println("max+++++" + photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)));
                System.out.println("min+++++" + photos.stream().min(Comparator.comparing(PhotoSize::getFileSize)));

                photos.stream().forEach(e -> System.out.println("photos+++" + e));

                String f_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                        .orElse(null)).getFileId();

                System.out.println("f_id***" + f_id);

                Unirest.setTimeouts(0, 0);
                InputStream responseProjectArray = null;
                responseProjectArray = Unirest.get("https://api.telegram.org/bot" + new String(users.getRunner()) + "/getFile?file_id=" + f_id)
                        .asString().getRawBody();

                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode generalNode = objectMapper.readTree(responseProjectArray);

                if (generalNode != null) {
                    String f_path = generalNode.findValue("file_path").textValue();

                    System.out.println("file_path" + f_path);

                    InputStream is = xmlGettingService.getXMLRatesStream("https://api.telegram.org/file/bot"
                            + new String(users.getRunner()) + "/" + f_path);

                    byte[] targetArray = new byte[is.available()];
                    is.read(targetArray);

                    message = new Message(null, 0, null, targetArray, null,
                            update.getMessage().getLocation().getLongitude(), update.getMessage().getLocation().getLatitude());
                }


            } else if (update.getMessage().hasLocation()) {
                message = new Message(null, 0, null, null, null,
                        update.getMessage().getLocation().getLongitude(), update.getMessage().getLocation().getLatitude());
            } else {
                String inputText = update.getMessage().getText();
                message = new Message(null, Map.of(RU, inputText));
            }
        }

        return message;
    }

}
