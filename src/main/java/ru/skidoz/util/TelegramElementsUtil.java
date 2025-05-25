package ru.skidoz.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;

/**
 * @author andrey.semenov
 */
@Component
public class TelegramElementsUtil {

    @Autowired
    private XMLGettingService xmlGettingService;

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
