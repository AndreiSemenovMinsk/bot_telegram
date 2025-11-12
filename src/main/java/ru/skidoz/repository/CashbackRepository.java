package ru.skidoz.repository;


import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.*;
import ru.skidoz.model.entity.telegram.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CashbackRepository extends JpaRepository<CashbackEntity, Integer> {

    List<CashbackEntity> findAllByUserId(Integer id);

    CashbackEntity findFirstByActionAndUser(ActionEntity action, UserEntity buyer);

    @Query(value = "SELECT cbk FROM CashbackEntity cbk JOIN cbk.shop shp WHERE shp.adminUser = :shopBuyer AND cbk.user = :buyer")
    List<CashbackEntity> findAllByBuyerAndShopBuyer(@Param("buyer") UserEntity buyer, @Param("shopBuyer") UserEntity shopBuyer);


//    @Query(value = "select * from cashback \n" +
//            "inner join action on (cashback.action_id=action.id) \n" +
//            "inner join shop on (cashback.shop_id=shop.id) \n" +
//            "inner join partner on (partner.debtor_id=shop.id) \n" +
//            "where partner.creditor_id=:shopId " +
//            "and cashback.user_id=:buyerId " +
//            "and action.type=:actionType",
//            nativeQuery = true)
//    List<CashbackEntity> findAllByBuyerAndShopPartners(@Param("buyerId") Integer buyerId, @Param("shopId") Integer shopId, @Param("actionType") String actionType);


//    @Query(value = "select SUM(purhase.sum) FROM purhase \n" +
//            "inner join cashback on (purhase.id=cashback.purсhase_id) \n" +
//            "inner join action on (cashback.action_id=action.id) \n" +
//            "WHERE  purhase.shop_id=:shopId AND " +
//            " purhase.user_id=:buyerId " +
//            "AND action.type=:actionType",
//            nativeQuery = true)
//    Integer purchaseSumByUserAndShopAndAction_Type(@Param("buyerId") Integer buyerId, @Param("shopId") Integer shopId, @Param("actionType") String actionTypeEnum);



    List<CashbackEntity> findAllByShop_IdAndUser_IdAndAction_Type(Integer shopId, Integer buyerId, ActionTypeEnum type);

    List<CashbackEntity> findAllByUser_IdAndShop_Id(Integer buyerId, Integer shopId);

    List<CashbackEntity> findAllByUser_IdAndAction_Id(Integer buyerId, Integer actionId);

    List<CashbackEntity> findAllByUserAndShopAndAction_Type(UserEntity buyer, ShopEntity shop, ActionTypeEnum actionTypeEnum);

    List<CashbackEntity> findAllByUserAndShopAndAction_TypeIn(UserEntity buyer, ShopEntity shop, List<ActionTypeEnum> types);

    CashbackEntity findByIdAndUserId(Integer id, Integer buyerId);

    Optional<CashbackEntity> findById(Integer id);

    List<CashbackEntity> findAllByUser(UserEntity buyer);

    List<CashbackEntity> findAllByPurchase_Id(Integer purchaseId);

    CashbackEntity save(CashbackEntity cashback);
}
