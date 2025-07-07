package ru.skidoz.repository;

import java.math.BigDecimal;
import java.util.List;

import ru.skidoz.model.entity.*;
import ru.skidoz.model.pojo.telegram.PurchaseShopGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseShopGroupRepository extends JpaRepository<PurchaseShopGroupEntity, Integer> {

    List<PurchaseShopGroupEntity> findAllByUser_IdAndManual(Integer userId, Boolean manual);

    @Query(value = "select * from purchase_shop_group \n" +
            "inner join purchase on (purchase_shop_group.purchase_id=purchase.id) \n" +
            "where purchase_shop_group.shop_id=:shopId " +
            "and purchase_shop_group.user_id=:buyerId ",
            nativeQuery = true)
    List<PurchaseShopGroupEntity> findAllByBuyerAndShop(@Param("buyerId") Integer buyerId, @Param("shopId") Integer shopId);

    PurchaseShopGroupEntity save(PurchaseShopGroupEntity cashbackShopGroup);
}
