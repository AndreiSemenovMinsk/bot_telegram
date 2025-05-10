package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.PartnerGroup;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface PartnerGroupCacheRepository extends JpaRepositoryTest<PartnerGroup, Integer> {

    PartnerGroup findFirstByCreditor_IdAndDebtor_Id(Integer /*Shop*/ creditor, Integer/*ShopGroup*/ debtor);

    List<PartnerGroup> findAllByCreditor_Id(Integer /*Shop*/ creditor);

    List<PartnerGroup> findAllByCreditorAndDebtorInGroup(Integer  creditorId, Integer debtorId);

    List<PartnerGroup> findAll();

//     PartnerGroup findById(Integer id);
}
