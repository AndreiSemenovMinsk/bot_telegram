package ru.skidoz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.command.CommandName.ADMIN;
import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;

import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.FilterPointCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopUserCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.integrational.AbstractIntegrationTest;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.search.menu.FilterPoint;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.side.ShopUser;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author andrey.semenov
 */
@ActiveProfiles("test")
@SpringBootTest
public class CacheTests extends AbstractIntegrationTest {

    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private UserCacheRepository userRepository;
    @Autowired
    private ShopUserCacheRepository shopUserCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private FilterPointCacheRepository filterPointRepository;
    @Autowired
    private CategoryCacheRepository categoryRepository;

    @Test
    void shopUserTest() {

        var user = new User(123L, "name");
        userRepository.save(user);

        var shop = new Shop();
        shopRepository.save(shop);

        var shopUser = new ShopUser(shop.getId(), user.getId(), shop.getInitialLevelId());

        System.out.println(shopUser);

        shopUserCacheRepository.save(shopUser);
        
        var result = shopUserCacheRepository.findByUserAndShop(
                user.getId(),
                shop.getId());

        assertNotNull(result);
        assertEquals(shop.getId(), result.getShop());
        assertEquals(user.getId(), result.getUser());
    }

    @Test
    void userTest() {
        var user = new User(123L, "name");
        userRepository.save(user);

        var level = new Level();
        level.setUserId(user.getId());
        level.setCallName( ADMIN_SHOPS.name());
        levelCacheRepository.save(level);

        var result = levelCacheRepository.findFirstByUser_ChatIdAndCallName(
                user.getChatId(),
                ADMIN_SHOPS.name());

        assertNotNull(result);
        assertEquals(ADMIN_SHOPS.name(), result.getCallName());
        assertEquals(user.getId(), result.getUserId());

        assertNull(levelCacheRepository.findFirstByUser_ChatIdAndCallName(user.getChatId(), ADMIN.name()));
    }

    @Test
    void categoryFilterPointTest() {
        var category = new Category(e -> {
            e.setAlias("categoryAlias");
            e.setActual(true);
        });
        categoryRepository.save(category);

        var filterPoint = new FilterPoint();
        filterPoint.setCategoryId(category.getId());
        filterPoint.addName("fp", RU);
        filterPoint.setUnitNameRU("filterPointName");
        filterPointRepository.save(filterPoint);

        FilterPoint foundFilterPoint = filterPointRepository
                .findByCategoryAndUnitNameRU(category.getId(), "filterPointName");

        assertEquals(category.getId(), foundFilterPoint.getCategoryId());
        assertEquals("filterPointName", foundFilterPoint.getUnitNameRU());
    }

}
