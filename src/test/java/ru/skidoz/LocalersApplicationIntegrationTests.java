package ru.skidoz;

import jakarta.transaction.Transactional;
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
import ru.skidoz.repository.telegram.UserRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LocalersApplicationIntegrationTests {

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
        for (int i = 0; i < 100; i++) {
            var p1 = new User();
            p1.setName("N" + i);
            p1.setChatId((long) i);
            userRepository.save(p1);

            for (int j = 0; j < 10; j++) {
                var c1 = new Cashback();
                c1.setUser(p1.getId());
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
        final User user = userRepository.findByChatId(1L);
        System.out.println("@@@" + user);

        var cb1 = cashbackRepository.findAllByUserId(user.getId());
        System.out.println(cb1);

        System.out.println("BASIC-*-*-*-*-*-*-0 " +
                cashbackRepository
                        .findAllByShopAndBuyerAndAction_Type(shopId, user.getId(), ActionTypeEnum.BASIC));

        final Action action = actionRepository.findById(actionId);
        System.out.println("@@@" + action);
        action.setType(ActionTypeEnum.COUPON);
        actionRepository.save(action);

        System.out.println("BASIC-*-*-*-*-*-*-1 " +
                cashbackRepository
                        .findAllByShopAndBuyerAndAction_Type(shopId, user.getId(), ActionTypeEnum.BASIC));

        System.out.println("COUPON-*-*-*-*-*-*-0 " +
                cashbackRepository
                        .findAllByShopAndBuyerAndAction_Type(shopId, user.getId(), ActionTypeEnum.COUPON));
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
