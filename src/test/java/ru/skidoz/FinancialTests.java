package ru.skidoz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.skidoz.aop.repo.PurchaseShopGroupCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.integrational.AbstractIntegrationTest;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.PurchaseShopGroup;
import ru.skidoz.model.pojo.telegram.User;

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
public class FinancialTests extends AbstractIntegrationTest {

    @Autowired
    private PurchaseShopGroupCacheRepository purchaseShopGroupCacheRepository;
    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private UserCacheRepository userRepository;

    @Test
    void shopUserTest2() {

        var user = new User(123L, "name");
        userRepository.save(user);

        var shop = new Shop();
        shopRepository.save(shop);

        var purchaseShopGroup = new PurchaseShopGroup();
        purchaseShopGroup.setUser(user.getId());
        purchaseShopGroup.setShop(shop.getId());
        purchaseShopGroupCacheRepository.save(purchaseShopGroup);

        List<PurchaseShopGroup> purchaseShopGroupDTOS = purchaseShopGroupCacheRepository
                .findAllByUserAndShop(user.getId(), shop.getId());

        assertEquals(purchaseShopGroup.getId(), purchaseShopGroupDTOS.get(0).getId());
    }
}
