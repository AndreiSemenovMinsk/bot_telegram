package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.Partner;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public  interface PartnerCacheRepository extends JpaRepositoryTest<Partner, Integer> {

    Partner findFirstByCreditor_IdAndDebtor_Id(Integer creditor, Integer debtor);

    List<Partner> findAllByCreditor_Id(Integer creditor);

    List<Partner> findAllByDebtor_Id(Integer debtor);
}
