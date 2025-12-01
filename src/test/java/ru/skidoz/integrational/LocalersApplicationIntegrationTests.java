package ru.skidoz.integrational;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ru.skidoz.aop.CacheAspect;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.User;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
//@Transactional
class LocalersApplicationIntegrationTests extends AbstractIntegrationTest {

    @Autowired
    CacheAspect cacheAspect;

    @Autowired
    private ActionCacheRepository actionRepository;
    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private UserCacheRepository userRepository;
    @Autowired
    private CashbackCacheRepository cashbackRepository;

    private static int shopId = 0;
    private static int userId = 0;
    private static int cashbackId = 0;
    private static int actionId = 0;

    @BeforeEach
    void setUpBeforeClass() throws Exception {

        Shop shop = new Shop();
        shop.setName("Shop");
        shopRepository.save(shop);
        shopId = shop.getId();

        Action action = new Action();
        action.setType(ActionTypeEnum.BASIC);
        action.setShop(shopId);
        actionRepository.save(action);
        actionId = action.getId();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            var user = new User();
            user.setName("N" + i);
            user.setChatId((long) i);
            userRepository.save(user);

            for (int j = 0; j < 1; j++) {
                var c1 = new Cashback();
                c1.setUser(user.getId());
                c1.setRadius(100);
                c1.setShop(shopId);
                c1.setAction(action.getId());
                cashbackRepository.save(c1);
            }
        }
        System.out.println("*save time " + (System.currentTimeMillis() - start));
    }

    @Test
    void small() {
        final User user = userRepository.findByChatId(0L);
        assertNotNull(user);
        assertEquals(0L, user.getChatId());

        var cb1 = cashbackRepository.findAllByUserId(user.getId());
        assertNotNull(cb1);
        assertEquals(1, cb1.size());
        assertEquals(user.getId(), cb1.get(0).getUser());

        var basicActionCashback = cashbackRepository
                        .findAllByShopAndUserAndAction_Type(shopId, user.getId(), ActionTypeEnum.BASIC);

        assertNotNull(basicActionCashback);
        assertEquals(1, basicActionCashback.size());
        assertEquals(shopId, basicActionCashback.get(0).getShop());
        assertEquals(user.getId(), basicActionCashback.get(0).getUser());
        assertEquals(actionId, basicActionCashback.get(0).getAction());

        final Action action = actionRepository.findById(actionId);
        action.setType(ActionTypeEnum.COUPON);
        actionRepository.save(action);

        var couponActionCashback = cashbackRepository
                .findAllByShopAndUserAndAction_Type(shopId, user.getId(), ActionTypeEnum.COUPON);

        assertNotNull(couponActionCashback);
        assertEquals(1, couponActionCashback.size());
        assertEquals(shopId, couponActionCashback.get(0).getShop());
        assertEquals(user.getId(), couponActionCashback.get(0).getUser());
        assertEquals(action.getId(), couponActionCashback.get(0).getAction());
    }

    @Test
    void large() {
        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        long start = System.currentTimeMillis();

        int s = 0;
        for (int i = 0; i < 100; i++) {
            int finI = i;
            //executor.submit(() -> {
            List<User> parentss = userRepository.findAllByName("N" + finI);

            parentss.forEach(user -> {
                try {
                    //System.out.println("Achild  " + personJPARepository.findByParentDTOId(user.getId()));
                    var cb1 = cashbackRepository.findAllByUserId(user.getId());
                    System.out.println(cb1.size());

                    user.setId(finI);

                    userRepository.save(user);

                    System.out.println("finI " + finI + " user.getId() " + user.getId());

                    //System.out.println("Bchild  " + personJPARepository.findByParentDTOId(user.getId()));
                    var cb2 = cashbackRepository.findAllByUserId(user.getId());

                    System.out.println(cb2.size());
                    //cashbackRepository.save(child.get(0));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            //countDownLatch.countDown();
            //return null;
            //});
            s++;
        }
        //countDownLatch.await();
        System.out.println(s);
        System.out.println(System.currentTimeMillis() - start);
    }
}
