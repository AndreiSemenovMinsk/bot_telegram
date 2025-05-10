package ru.skidoz.repository;


import java.util.List;

import ru.skidoz.model.entity.PartnerEntity;
import ru.skidoz.model.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Integer> {

/*
if there is no such a link - we should not change anything
 */
    @Modifying
    @Query(value = "INSERT INTO partner (limit, creditor_id, debtor_id) (0, :creditorId, :debtorId) ON DUPLICATE KEY UPDATE limit = :limit",
    nativeQuery = true)
    void addPartner(@Param("limit") Integer limit, @Param("creditorId") Integer creditorId,  @Param("debtorId") Integer debtorId);

    PartnerEntity findFirstByCreditorAndDebtor(ShopEntity creditor, ShopEntity debtor);

    PartnerEntity findFirstByCreditor_IdAndDebtor_Id(Integer/*Shop*/ creditor, Integer/*Shop*/ debtor);

    List<PartnerEntity> findAllByCreditor_Id(Integer/*Shop*/ creditor);

    List<PartnerEntity> findAllByDebtor_Id(Integer/*Shop*/ creditor);
/*
    @Query(value = "select * from partner ptnr inner join partner_group on where byr.session_id=:session_id", nativeQuery = true)
    List<Partner> findAllByDebtor_Id(PartnerGroup creditor);*/

    PartnerEntity save(PartnerEntity partner);
}
