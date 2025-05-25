package ru.skidoz.service.command_impl.starter;

import java.util.ArrayList;
import java.util.List;

import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.InitialLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static ru.skidoz.service.command.CommandName.PSHARE2P;

/**
 * @author andrey.semenov
 */
@Component
public class BookmarkLinkStarter extends LinkStarter {

    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userRepository;
    @Autowired
    private InitialLevel initialLevel;

    public List<LevelChat> getLevel(Long chatId, String bearingId, User buyer, User friend) {

        Bookmark bookmark = bookmarkCacheRepository.findById(Integer.valueOf(bearingId));

        List<LevelChat> levelChatDTOList = new ArrayList<>(storeRecommendation(buyer, friend, shopCacheRepository.findById(bookmark.getShop())));

        Bookmark newBookmark = new Bookmark(e -> {
            e.setRadius(50);
            e.setProduct(bookmark.getProduct());
            e.setShop(bookmark.getShop());
        });

        bookmarkCacheRepository.save(newBookmark);
        friend.addBookmarks(newBookmark);
        userRepository.save(friend);

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setLevel(initialLevel.convertToLevel(levelRepository.findFirstByUser_ChatIdAndCallName(chatId, PSHARE2P.name()),
                    true,
                    false));
        }));
        return levelChatDTOList;
    }
}
