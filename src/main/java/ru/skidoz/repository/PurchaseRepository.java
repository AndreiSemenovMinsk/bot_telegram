package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Integer> {

    List<PurchaseEntity> findAllByShop_IdAndBuyer_Id(Integer/*Shop*/ shop, Integer/*Users*/ buyer);

    List<PurchaseEntity> findAllByBuyer_Id(Integer/*Users*/ buyer);

    List<PurchaseEntity> findAllByIdIsIn(List<Integer> ids);

    /*@Query(value = "select * from purchase \n" +
            "inner join shop on (purchase.shop_id=shop.id) \n" +
            "inner join partner on (partner.debtor_id=shop.id) \n" +
            "where partner.creditor_id=:shopId " +
            "and purchase.user_id=:buyerId " +
            "and ",
            nativeQuery = true)
    List<Purchase> findAllByBuyerAndShopPartners(@Param("buyerId") Integer buyerId, @Param("shopId") Integer shopId, @Param("actionType") String actionType);*/

    PurchaseEntity save(PurchaseEntity purchase);
}
