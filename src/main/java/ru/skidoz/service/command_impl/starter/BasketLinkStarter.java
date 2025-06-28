package ru.skidoz.service.command_impl.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.BasketProduct;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.BasketCacheRepository;
import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class BasketLinkStarter extends LinkStarter {

    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;
    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;

    public List<LevelChat> getLevel(Long chatId, String bearingId, User buyer, User friend) {

        Basket basket = basketCacheRepository.findById(Integer.valueOf(bearingId));

        List<LevelChat> levelChatDTOList = new ArrayList<>(storeRecommendation(buyer, friend, shopCacheRepository.findById(basket.getShopId())));

        for (BasketProduct basketProduct : basket.getBasketProductList()) {
            Bookmark newBookmark = new Bookmark(e -> {
                e.setRadius(50);
                e.setProduct(basketProduct.getProduct());
                e.setShop(basket.getShopId());
            });
            bookmarkCacheRepository.save(newBookmark);
            friend.addBookmarks(newBookmark);
        }
        userCacheRepository.save(friend);

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
