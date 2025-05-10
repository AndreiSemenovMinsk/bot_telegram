package ru.skidoz.service.command_impl.add_goods;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.util.XMLGettingService;

/**
 * @author andrey.semenov
 */
@Component
public class AddGoodsDescription implements Command {

    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private XMLGettingService xmlGettingService;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException, IOException, ParseException, UnirestException {

        System.out.println("***********AddGoodsDescription**************");
        //Level resultLevel = level;//initialLevel.level_ADD_GOODS_PHOTO;
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                false);
        Long chatId = users.getChatId();

        if (update.getMessage().hasPhoto()){

            Shop shopInitiator = shopCacheRepository.findById(users.getCurrentAdminShop());
            Product product = productCacheRepository.findById(shopInitiator.getCurrentCreatingProduct());

            List<PhotoSize> photos = update.getMessage().getPhoto();

            System.out.println("max+++++" + photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)));
            System.out.println("min+++++" + photos.stream().min(Comparator.comparing(PhotoSize::getFileSize)));

            System.out.println("photos"+ photos.size());
            System.out.println("photos.stream()" + photos.stream());
            photos.forEach(PhotoSize::getFileSize);
            photos.forEach(e -> System.out.println());
            System.out.println(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)));
            System.out.println(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null));
            System.out.println("Objects" + Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null)));

            String f_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();

            System.out.println("f_id***" + f_id);

            Unirest.setTimeouts(0, 0);
            InputStream responseProjectArray = null;
            responseProjectArray = Unirest.get("https://api.telegram.org/bot"
                    + new String(users.getRunner()) +"/getFile?file_id="+f_id)
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
                product.setImage(targetArray);
            }

            productCacheRepository.save(product);
        }

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
