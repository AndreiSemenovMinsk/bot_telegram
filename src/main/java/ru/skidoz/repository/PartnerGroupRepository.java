package ru.skidoz.repository;

import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.PartnerGroupEntity;
import ru.skidoz.model.pojo.telegram.PartnerGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PartnerGroupRepository extends JpaRepository<PartnerGroupEntity, Integer> {
/*
if there is no such a link - we should not change anything
 */
//    PartnerGroupEntity findFirstByCreditor_IdAndDebtor_Id(Integer/*Shop*/ creditor, Integer/*ShopGroup*/ debtor);

//    List<PartnerGroupEntity> findAllByCreditor_Id(Integer/*Shop*/ creditor);

//    @Query(value = "SELECT * FROM partner_group " +
//            " INNER JOIN shop_group_shop_set ON (partner_group.debtor_id=shop_group_shop_set.shop_group_id) " +
//            " WHERE partner_group.creditor_id = :creditorId AND shop_group_shop_set.shop_id = :debtorId ",
//            nativeQuery = true)
//    List<PartnerGroupEntity> findAllByCreditorAndDebtorInGroup(Integer creditorId, Integer debtorId);

    PartnerGroupEntity findFirstByShop_IdAndShopGroup_Id(Integer /*Shop*/ creditor, Integer/*ShopGroup*/ debtor);

    List<PartnerGroupEntity> findAll();

    Optional<PartnerGroupEntity> findById(Integer id);

    List<PartnerGroupEntity> findAllByShop_Id(Integer /*Shop*/ creditor);

    PartnerGroupEntity save(PartnerGroupEntity partnerGroup);
}
