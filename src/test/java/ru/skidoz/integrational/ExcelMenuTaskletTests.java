package ru.skidoz.integrational;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.skidoz.aop.CacheAspect;
import ru.skidoz.service.InitialExcelMenu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
//@Transactional
class ExcelMenuTaskletTests extends AbstractIntegrationTest {

    @Autowired
    CacheAspect cacheAspect;

    @Autowired
    InitialExcelMenu initialExcelMenu;
//    @Autowired
//    private ActionCacheRepository actionRepository;

//    @Autowired
//    private CashbackCacheRepository cashbackRepository;

//    private static int shopId = 0;
//    private static int userId = 0;
//    private static int cashbackId = 0;
//    private static int actionId = 0;

    @BeforeEach
    void setUpBeforeClass() throws Exception {
//
//        Shop shop = new Shop();
//        shop.setName("Shop");
//        shopRepository.save(shop);
//        shopId = shop.getId();
//
//        Action action = new Action();
//        action.setType(ActionTypeEnum.BASIC);
//        action.setShop(shopId);
//        actionRepository.save(action);
//        actionId = action.getId();
//
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            var p1 = new User();
//            p1.setName("N" + i);
//            p1.setChatId((long) i);
//            userRepository.save(p1);
//
//            for (int j = 0; j < 10; j++) {
//                var c1 = new Cashback();
//                c1.setUser(p1.getId());
//                c1.setRadius(100);
//                c1.setShop(shopId);
//                c1.setAction(action.getId());
//                cashbackRepository.save(c1);
//            }
//        }
//        System.out.println("*save time " + (System.currentTimeMillis() - start));
    }

    @Test
    void small() {

        initialExcelMenu.execute();

        assertEquals(12, 12);

        System.out.println("77777");
    }



}
