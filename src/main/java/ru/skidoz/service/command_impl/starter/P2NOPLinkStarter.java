package ru.skidoz.service.command_impl.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.initializers.InitialLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class P2NOPLinkStarter extends LinkStarter {

    @Autowired
    private InitialLevel initialLevel;

    public List<LevelChat> getLevel(Long chatId, User buyer, User friend) {

        List<LevelChat> levelChatDTOList = new ArrayList<>(storeRecommendation(buyer, friend, null));

        LevelDTOWrapper friendLevel = initialLevel.convertToLevel(initialLevel.level_P2NOP,//levelRepository.findFirstByUser_ChatIdAndCallName(chatId, P2NOP.name()),
                false,
                false);

        Message friendMessage = new Message(null, Map.of(LanguageEnum.RU,"Добро пожаловать в систему Skidozona! " + buyer.getName() + " добавил Вас в систему"));

        friendLevel.addMessage(friendMessage);
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(friend.getChatId());
            e.setLevel(friendLevel);
        }));

        Long buyerChatId = buyer.getChatId();
        LevelDTOWrapper buyerLevel = initialLevel.convertToLevel(initialLevel.level_P2NOP_RESP,//levelRepository.findFirstByUser_ChatIdAndCallName(buyerChatId, P2NOP_RESP.name()),
                false,
                false);

        Message buyerMessage = new Message(null, Map.of(LanguageEnum.RU,"Пользователь " + friend.getName() + "  успешно добавлен в систему!"));

        buyerLevel.addMessage(buyerMessage);

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(buyerChatId);
            e.setLevel(buyerLevel);
        }));

        return levelChatDTOList;
    }
}
