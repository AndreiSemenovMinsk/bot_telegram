package ru.skidoz.service.command_impl.starter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class B2BLinkStarter extends LinkStarter {

    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> getLevel(Long chatId, User buyer, User friend, Shop shopPartner) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("++++++++++++++++++++B2BLinkStarter+++++++++++++++++++++  friend***" + friend);
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_PARTNER_RATE,
                false,
                false);

        List<LevelChat> levelChatDTOList = new ArrayList<>(storeRecommendation(buyer, friend, null));

        Shop shopInitiator = shopCacheRepository.findBySellerChatId(chatId);
        shopInitiator.setCurrentConversationShop(shopPartner.getId());
        shopCacheRepository.save(shopInitiator);

        Partner partner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopPartner.getId());

        if (partner == null) {
            partner = new Partner(e -> {
                e.setCreditor(shopInitiator.getId());
                e.setDebtor(shopPartner.getId());
                e.setRate(0);
                e.setSum(BigDecimal.ZERO);
                e.setLim(BigDecimal.ZERO);
            });
        }
        partnerCacheRepository.save(partner);
        //это уже переход на следующий уровень, поэтому текст следующего уровня
        //Level resultLevel = initialLevel.level_SEARCH_PARTNER_RATE.clone();

        Message message = new Message(null, Map.of(LanguageEnum.RU,"кэшбек в процентах по рекомендациям от " + shopPartner.getName()));
        resultLevel.addMessage(message);

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setLevel(resultLevel);
        }));
        return levelChatDTOList;
    }
}
