package ru.skidoz.aop.repo;


import java.util.List;

import ru.skidoz.model.pojo.telegram.PurchaseShopGroup;

import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface PurchaseShopGroupCacheRepository extends JpaRepositoryTest<PurchaseShopGroup, Integer> {

//    public List<PurchaseShopGroup> findAllByUserAndManual(Integer  user, Boolean manual);

//    Integer purchaseSumByUserAndShop(Integer  buyerId, Integer shopGroupId, Boolean manual);

//    @Query(value = "select purchase_shop_group.* from purchase_shop_group \n" +
//            "inner join purchase on (purchase_shop_group.purchase_id=purchase.id) \n" +
//            "where purchase_shop_group.shop_id=:shopId " +
//            "and purchase_shop_group.user_id=:buyerId ",
//            nativeQuery = true)
//    List<PurchaseShopGroup> findAllByBuyerAndShop(@Param("buyerId") Integer buyerId, @Param("shopId") Integer shopId);

    List<PurchaseShopGroup> findAllByUserAndShop(Integer buyerId, Integer shopId);
}
