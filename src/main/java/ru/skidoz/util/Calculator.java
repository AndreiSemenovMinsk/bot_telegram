package ru.skidoz.util;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.aop.repo.PurchaseCacheRepository;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.Cashback;


@Component
public class Calculator {

    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;


    public BigDecimal purchaseSumByUserAndShopAndAction_Type(Integer buyerId, Integer shopId, ActionTypeEnum actionTypeEnum) {

        List<Cashback> cashbacks = cashbackCacheRepository
                .findAllByShopAndBuyerAndAction_Type(shopId, buyerId, actionTypeEnum);
        BigDecimal sum = BigDecimal.ZERO;
        for (Cashback cashback : cashbacks) {

            Purchase purchase = purchaseCacheRepository.findById(cashback.getId());
            sum.add(purchase.getSum());
        }
        return sum;
    }
}
