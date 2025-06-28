package ru.skidoz.service.command_impl.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.RecommendationCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.service.initializers.InitialLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class LinkStarter {

    @Autowired
    private RecommendationCacheRepository recommendationCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    public List<LevelChat> getLevel(Long chatId, User buyer, User friend, Shop shopPartner) throws CloneNotSupportedException {
        return null;
    }

    public List<LevelChat> storeRecommendation(User buyer, User friend, Shop shop){

        System.out.println("friend*****" + friend);
        List<LevelChat> levelChatDTOList = new ArrayList<>();

        LevelDTOWrapper buyerLevel = initialLevel.convertToLevel(initialLevel.level_INITIALIZE,
                false,
                true);
        LevelDTOWrapper friendLevel = initialLevel.convertToLevel(initialLevel.level0_1_1,
                false,
                false);
        try {
            Recommendation recommendation = new Recommendation(e -> {
                e.setBuyer(buyer.getId());
                e.setFriend(friend.getId());
                e.setShop(shop.getId());
            });
            recommendationCacheRepository.save(recommendation);

            String buyerText;
            String friendText;
            if (shop != null){
                buyerText = friend.getName() + " рекомендовал Вам магазин " + shop.getName();
                friendText = "Вы рекомендовали " + buyer.getName() + " магазин " + shop.getName();
            } else {
                buyerText = friend.getName() + " добавил Вас в систему Skidozona";
                friendText = "Вы добавили " + buyer.getName() + "  в систему Skidozona";
            }
            Message buyerMessage = new Message(null, Map.of(LanguageEnum.RU,buyerText));
            buyerLevel.addMessage(buyerMessage);

            Message friendMessage = new Message(null, Map.of(LanguageEnum.RU,friendText));
            friendLevel.addMessage(friendMessage);

        } catch (Exception exception){

            String buyerText;
            String friendText;
            if (shop != null){
                buyerText = friend.getName() + " рекомендовал Вам магазин " + shop.getName() + ", но он уже есть в рекомендациях";
                friendText = "Вы пытались рекомендовать " + buyer.getName() + " магазин " + shop.getName() + ", но он уже есть в рекомендациях";
            } else {
                buyerText = friend.getName() + " пытался добавить Вас в систему Skidozona, но Вы уже являетесь участником Skidozona";
                friendText = buyer.getName() + " уже является участником Skidozona";
            }
            Message buyerMessage = new Message(null, Map.of(LanguageEnum.RU,buyerText));
            buyerLevel.addMessage(buyerMessage);

            Message friendMessage = new Message(null, Map.of(LanguageEnum.RU,friendText));
            friendLevel.addMessage(friendMessage);
        }

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(buyer.getChatId());
            e.setLevel(buyerLevel);
        }));
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(friend.getChatId());
            e.setLevel(friendLevel);
        }));
        return levelChatDTOList;
    }
}
