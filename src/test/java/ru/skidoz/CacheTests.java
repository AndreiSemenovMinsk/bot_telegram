package ru.skidoz;

import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopUserCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.integrational.AbstractIntegrationTest;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.side.ShopUser;
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

    @Test
    void shopUserTest() {

        var user = new User(123L, "name");
        userRepository.save(user);

        var shop = new Shop();
        shopRepository.save(shop);

        var shopUser = new ShopUser(shop.getId(), user.getId(), shop.getInitialLevelId());

        System.out.println(shopUser);

        shopUserCacheRepository.save(shopUser);

        System.out.println(shopUserCacheRepository.findByUserAndShop(user.getId(), shop.getId()));
    }

}
