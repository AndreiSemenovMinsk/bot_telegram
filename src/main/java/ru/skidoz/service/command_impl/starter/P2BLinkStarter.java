package ru.skidoz.service.command_impl.starter;

import java.util.ArrayList;
import java.util.List;

import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.command_impl.search_goods.ConnectShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class P2BLinkStarter extends LinkStarter {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ConnectShop connectShop;

    @Override
    public List<LevelChat> getLevel(Long chatId, User  buyer, User  friend, Shop shopPartner) {

        System.out.println();
        System.out.println("-------------------------------P2BLinkStarter---------------------------");
        System.out.println();

        Shop shopGetter = shopCacheRepository.findById(buyer.getCurrentAdminShop());

        List<LevelChat> levelChatDTOList = new ArrayList<>(storeRecommendation(buyer, friend, null));

        levelChatDTOList.addAll(connectShop.getInfoLevel(buyer, friend, shopGetter));

        return levelChatDTOList;
    }
}