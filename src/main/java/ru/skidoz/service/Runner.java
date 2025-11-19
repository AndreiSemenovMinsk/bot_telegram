package ru.skidoz.service;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;

import lombok.RequiredArgsConstructor;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author andrey.semenov
 */
@Component
@RequiredArgsConstructor
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

    public void processSend(List<LevelChat> levelChatList, String secretHash) {

//        System.out.println(" *****-------processSend secretHash " + secretHash);

        for (LevelChat levelChat : levelChatList) {

//            System.out.println("levelChat-----" + levelChat);

            try {
                this.key = secretHash;

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
                        System.out.println();
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

                    for (ButtonRow buttonRow : buttonRows) {

//                        System.out.println("buttonRow---" + buttonRow);

                        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                        for (int j = 0; j < buttonRow.getButtonList().size(); j++) {

                            Button javaButton = buttonRow.getButtonList().get(j);

//                            System.out.println(javaButton);

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


    private static SendPhoto sendPhoto(Long chatId, InputStream inputStream, String subText) {
        SendPhoto messagePhoto2 = new SendPhoto();
        messagePhoto2.setChatId(chatId.toString());
        messagePhoto2.setPhoto(new InputFile(inputStream, subText));
        messagePhoto2.setCaption(subText);

        return messagePhoto2;
    }


    public byte[] processDocument(Document inputDocument, String secretHash) {

        this.key = secretHash;

        String doc_id = inputDocument.getFileId();
        String doc_name = inputDocument.getFileName();
        String doc_mine = inputDocument.getMimeType();
        long doc_size = inputDocument.getFileSize();
        //String getID = String.valueByCode(update.getMessage().getFrom().getId());

        Document document = new Document();
        document.setMimeType(doc_mine);
        document.setFileName(doc_name);
        document.setFileSize(doc_size);
        document.setFileId(doc_id);

        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);


            return IOUtils.toByteArray(downloadFileAsStream(file));



//                telegramProcessor.processExcel(downloadFileAsStream(file), users);
            //File excel = downloadFile(file);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
