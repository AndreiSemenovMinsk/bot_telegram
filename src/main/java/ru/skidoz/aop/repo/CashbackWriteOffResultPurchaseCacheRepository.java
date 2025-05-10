package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.CashbackWriteOffResultPurchase;
import org.springframework.stereotype.Service;


/**
 * @author andrey.semenov
 */
@Service
public interface CashbackWriteOffResultPurchaseCacheRepository extends JpaRepositoryTest<CashbackWriteOffResultPurchase, Integer> {

    List<CashbackWriteOffResultPurchase> findAllByCashbackWriteOff_Id(Integer id);
}
