package ru.skidoz.service.command_impl.buyer_bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mashape.unirest.http.exceptions.UnirestException;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import  ru.skidoz.service.TelegramProcessor;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.util.TelegramElementsUtil;

/**
 * @author andrey.semenov
 */
@Component
public class SendBuyerMessage implements Command {

    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private TelegramElementsUtil telegramElementsUtil;

    @Override
    public List<LevelChat> runCommand(Update update, Level buyerLevel, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++SendBuyerMessage+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        Long buyerChatId = users.getChatId();
        LevelDTOWrapper shopLevel = initialLevel.convertToLevel(initialLevel.level0_1_5,
                false,
                false);

        Shop shop = shopCacheRepository.findById(users.getCurrentConversationShopId());

        if (shop != null) {
            Long shopChatId = shop/*.getAdminUser()*/.getChatId();

            //Level shopLevel = initialLevel.level0_1_5;
            //TODO - это надо отправить
            try {
                shopLevel.addMessage(telegramElementsUtil.convertUpdateToMessage(update, users));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
            ButtonRow row = new ButtonRow();
            Button button = new Button(row, Map.of(LanguageEnum.RU, "Ответить клиенту 1@" + users.getName()), initialLevel.level_RESPONSE_BUYER_MESSAGE.getIdString() + buyerChatId);
            row.add(button);
            Button button2 = new Button(row, Map.of(LanguageEnum.RU, "Отмена "), initialLevel.level_NON_RESPONSE.getIdString() + shopChatId);
            row.add(button2);
            shopLevel.addRow(row);


            levelChatDTOList.add(new LevelChat(e -> {
                e.setChatId(shopChatId);
                e.setUser(users);
                e.setLevel(shopLevel);
            }));
        }
        /*levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(buyerChatId);
            e.setUser(users);
            e.setLevel(shopLevel);
        }));*/
        return levelChatDTOList;
    }
}
