package ru.skidoz.service.command_impl.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class CashbackLinkStarter extends LinkStarter {

    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    public List<LevelChat> getLevel(Long chatId, String bearingId, User buyer, User friend) {

        Cashback cashback = cashbackCacheRepository.findById(Integer.valueOf(bearingId));

        List<LevelChat> levelChatDTOList = new ArrayList<>(storeRecommendation(buyer, friend, shopCacheRepository.findById(cashback.getShop())));

        final LevelDTOWrapper levelDTOWrapper = initialLevel.convertToLevel(initialLevel.level_PSHARE2P,
                false,
                false);
        Message buyerMessage = new Message(null, Map.of(LanguageEnum.RU, "Вы получили рекомендацию от " + friend.getName()));
        levelDTOWrapper.addMessage(buyerMessage);

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setLevel(levelDTOWrapper);
        }));
        return levelChatDTOList;
    }
}
