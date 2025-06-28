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
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class P2PExistBuyerLinkStarter extends LinkStarter {

    @Autowired
    private InitialLevel initialLevel;

    public List<LevelChat> getLevel(Long chatId, User buyer, User friend) {

        System.out.println();
        System.out.println("+++++++++++++++++++++++++++P2PExistBuyerLinkStarter+++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        List<LevelChat> levelChatDTOList = new ArrayList<>();

        LevelDTOWrapper friendLevel = initialLevel.convertToLevel(initialLevel.level_P2P,
                false,
                false);

        Message friendMessage = new Message(null, Map.of(LanguageEnum.RU,"Пользователь " + buyer.getName() + " уже есть в системе"));

        friendLevel.addMessage(friendMessage);
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(friend.getChatId());
            e.setLevel(friendLevel);
        }));

        System.out.println("friend.getChatId()++" + friend.getChatId());
        System.out.println("buyer.getChatId()+++" + buyer.getChatId());

        Long buyerChatId = buyer.getChatId();
        LevelDTOWrapper buyerLevel = initialLevel.convertToLevel(initialLevel.level_P2P_RESP,
                false,
                false);
        Message buyerMessage = new Message(null, 0,Map.of(LanguageEnum.RU,"Пользователь " + friend.getName() + " пытался добавить Вас в систему"));
        buyerLevel.addMessage(buyerMessage);
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(buyerChatId);
            e.setLevel(buyerLevel);
        }));

        return levelChatDTOList;
    }
}
