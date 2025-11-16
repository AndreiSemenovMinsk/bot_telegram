package ru.skidoz.util;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.aop.repo.PurchaseCacheRepository;
import ru.skidoz.aop.repo.PurchaseShopGroupCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.telegram.PurchaseShopGroup;
import ru.skidoz.model.pojo.telegram.ShopGroup;


@Slf4j
@Component
public class CalculatorPartners {

    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private PurchaseShopGroupCacheRepository purchaseShopGroupCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;

    public Integer purchaseSumByUserAndShopAndAction_Type(
            Integer buyerId,
            Integer shopId,
            ActionTypeEnum actionTypeEnum) {

        List<Cashback> cashbacks = cashbackCacheRepository
                .findAllByShopAndUserAndAction_Type(shopId, buyerId, actionTypeEnum);
        Integer sum = 0;
        for (Cashback cashback : cashbacks) {

            Purchase purchase = purchaseCacheRepository.findById(cashback.getId());
            sum += purchase.getSum();
        }
        return sum;
    }


    public int getMaxFromPartners(int shopId, int buyerId, int withdrawLimit, boolean finalWithdrawal) {

        int freeLimit = withdrawLimit;

        final List<PurchaseShopGroup> purchaseShopGroups = purchaseShopGroupCacheRepository
                .findAllByUserAndShop(shopId, buyerId);

        List<Quattro> topInPurchase = new ArrayList<>();
        List<Quattro> otherPurchase = new ArrayList<>();
        Set<Integer> shopGroupSet = new HashSet<>();

        final Map<Integer, List<PurchaseShopGroup>> map = purchaseShopGroups.stream().collect(
                Collectors.groupingBy(
                        PurchaseShopGroup::getPurchase,
                        Collectors.collectingAndThen(
                                Collectors.toList(), list -> {

                                    var purchaseShopGroupSum = list
                                            .stream()
                                            .sorted(Comparator
                                                    .comparingInt(PurchaseShopGroup::getSum)
                                                    .reversed())
                                            .collect(Collectors.toList());

                                    final PurchaseShopGroup topPurchaseShopGroup = purchaseShopGroupSum.get(0);
                                    final int sum = topPurchaseShopGroup.getSum();

                                    topInPurchase.add(
                                            new Quattro(
                                                    sum,
                                                    0,
                                                    topPurchaseShopGroup.getPurchase(),
                                                    topPurchaseShopGroup.getShopGroup()));

                                    shopGroupSet.add(topPurchaseShopGroup.getShopGroup());

                                    for (int i = 1; i < purchaseShopGroupSum.size() && i < 10; i++) {
                                        otherPurchase.add(
                                                new Quattro(
                                                        purchaseShopGroupSum.get(i).getSum(),
                                                        // убывание ценности этого варианта - по размеру возможного списания
                                                        (sum - purchaseShopGroupSum.get(i).getSum()) / sum,
                                                        topPurchaseShopGroup.getPurchase(),
                                                        topPurchaseShopGroup.getShopGroup()
                                                )
                                        );
                                    }
                                    return purchaseShopGroupSum;
                                })));

        Map<Integer, Integer> shopGroupLimits = shopGroupSet.stream()
                .map(id -> shopGroupCacheRepository.findById(id))
                .collect(Collectors.toMap(ShopGroup::getId, ShopGroup::getLimitSum));

        otherPurchase.sort(Comparator.comparingInt(p -> p.rate));
        topInPurchase.addAll(otherPurchase);

        List<Quattro> resultQuatros = new ArrayList<>();

        for (int i = 0; i < topInPurchase.size(); i++) {
            final Quattro quattro = topInPurchase.get(i);

            if (withdrawLimit > quattro.sum
                    && shopGroupLimits.get(quattro.shopGroupId) > quattro.sum
                    && shopGroupSet.remove(quattro.shopGroupId)) {
                withdrawLimit -= quattro.sum;
                resultQuatros.add(quattro);
                shopGroupLimits.put(quattro.shopGroupId, shopGroupLimits.get(quattro.shopGroupId) - quattro.sum);
            }
            // если неиспользованный - выброшенный - остаток больше половины
            if (quattro.rate > 0.5) {
                break;
            }
        }

        if (finalWithdrawal) {
            for (Quattro quattro : resultQuatros) {
                final int purchaseId = quattro.purchaseId;

                purchaseCacheRepository.deleteById(purchaseId);

                shopGroupCacheRepository.deleteShopGroupByIds(
                        map.get(purchaseId).stream()
                                .map(PurchaseShopGroup::getShopGroup)
                                .collect(Collectors.toList())
                );
            }
        }
        return withdrawLimit - freeLimit;
    }

    static class Quattro {
        int sum;
        int rate;
        int purchaseId;
        int shopGroupId;

        Quattro(int sum, int rate, int purchaseId, int shopGroupId) {
            this.sum = sum;
            this.rate = rate;
            this.purchaseId = purchaseId;
            this.shopGroupId = shopGroupId;
        }
    }
}
