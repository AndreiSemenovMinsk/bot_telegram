package ru.skidoz.repository;

import java.math.BigDecimal;
import java.util.List;

import ru.skidoz.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CashbackShopGroupRepository extends JpaRepository<CashbackShopGroupEntity, Integer> {

    List<CashbackShopGroupEntity> findAllByUser_IdAndManual(Integer userId, Boolean manual);

    @Query(value = "select SUM(purhase.sum) from purhase \n" +
            "inner join cashback_shop_group on (purhase.id=cashback_shop_group.purсhase_id) \n" +
            "where  cashback_shop_group.shop_group_id=:shopGroupId AND " +
            " purhase.user_id=:buyerId " +
            "AND cashback_shop_group.manual=:manual",
            nativeQuery = true)
    BigDecimal purchaseSumByUserAndShop(@Param("buyerId") Integer buyerId, @Param("shopGroupId") Integer shopGroupId, @Param("manual") Boolean manual);

    CashbackShopGroupEntity save(CashbackShopGroupEntity cashbackShopGroup);
}
