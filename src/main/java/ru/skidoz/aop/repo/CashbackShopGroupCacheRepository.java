package ru.skidoz.aop.repo;

import java.math.BigDecimal;
import java.util.List;

import ru.skidoz.model.pojo.telegram.CashbackShopGroup;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface CashbackShopGroupCacheRepository extends JpaRepositoryTest<CashbackShopGroup, Integer> {

    public List<CashbackShopGroup> findAllByUserAndManual(Integer  user, Boolean manual);

    BigDecimal purchaseSumByUserAndShop(Integer  buyerId, Integer shopGroupId, Boolean manual);
}
