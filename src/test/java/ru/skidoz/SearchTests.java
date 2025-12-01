package ru.skidoz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.skidoz.service.command.CommandName.ADMIN;
import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;

import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopUserCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.integrational.AbstractIntegrationTest;
import ru.skidoz.model.pojo.search.search.Search;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.side.ShopUser;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.batch.CacheSearchTasklet;
import ru.skidoz.service.search.SearchService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author andrey.semenov
 */
@ActiveProfiles("test")
@SpringBootTest
public class SearchTests extends AbstractIntegrationTest {

    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private CacheSearchTasklet cacheSearchTasklet;
    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;
    @Autowired
    private UserCacheRepository userRepository;

    @Test
    void shopUserTest() {
        var shop = new Shop();
        shopRepository.save(shop);

        var product = new Product();
        product.setActive(true);
        product.setNameRU("qwerty");
        product.setShop(shop.getId());
        product.setPrice(120);
        productCacheRepository.save(product);

        searchService.nameWordMapper(List.of("qwerty"), product);

        cacheSearchTasklet.execute();

        Search search = new Search();
        search.setSearch("qwerty");

        List<Integer> valuesPointList = searchService.getProducts(search);
        assertEquals(product.getId(), valuesPointList.get(0));
    }

    @Test
    void shopUserTest2() {

        var user = new User(123L, "name");
        userRepository.save(user);

        var shop = new Shop();
        shopRepository.save(shop);

        var bookmark = new Bookmark();
        bookmark.setUser(user.getId());
        bookmark.setShop(shop.getId());
        bookmarkCacheRepository.save(bookmark);

        List<Bookmark> bookmarkDTOS = bookmarkCacheRepository.findAllByUserId(user.getId());
        assertEquals(bookmark.getId(), bookmarkDTOS.get(0).getId());
    }
}
