package ru.skidoz.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
public class Calculator {

    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private PurchaseShopGroupCacheRepository purchaseShopGroupCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;

    public BigDecimal purchaseSumByUserAndShopAndAction_Type(
            Integer buyerId,
            Integer shopId,
            ActionTypeEnum actionTypeEnum) {

        List<Cashback> cashbacks = cashbackCacheRepository
                .findAllByShopAndBuyerAndAction_Type(shopId, buyerId, actionTypeEnum);
        BigDecimal sum = BigDecimal.ZERO;
        for (Cashback cashback : cashbacks) {

            Purchase purchase = purchaseCacheRepository.findById(cashback.getId());
            sum.add(purchase.getSum());
        }
        return sum;
    }

    public void getMax(int shopId, int buyerId, int withdrawLimit) {


        final List<PurchaseShopGroup> purchaseShopGroups = purchaseShopGroupCacheRepository
                .findAllByBuyerAndShop(shopId, buyerId);


        List<Quatro> topInPurchase = new ArrayList<>();
        List<Quatro> otherPurchase = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        Set<Integer> shopGroupSet = new HashSet<>();

        final Map<Integer, List<PurchaseShopGroup>> map = purchaseShopGroups.stream().collect(
                Collectors.groupingBy(
                        PurchaseShopGroup::getPurchase,
                        Collectors.collectingAndThen(
                                Collectors.toList(), list -> {

                                    var sortedList = list
                                            .stream()
                                            .sorted(Comparator
                                                    .comparingInt(PurchaseShopGroup::getSum)
                                                    .reversed())
                                            .collect(Collectors.toList());

                                    final PurchaseShopGroup purchaseShopGroup = sortedList.get(0);

                                    topInPurchase.add(
                                            new Quatro(
                                                    purchaseShopGroup.getSum(),
                                                    0,
                                                    index.get(),
                                                    0));

                                    shopGroupSet.add(purchaseShopGroup.getShopGroup());

                                    for (int i = 1; i < sortedList.size() && i < 10; i++) {

                                        otherPurchase.add(
                                                new Quatro(
                                                        sortedList.get(i).getSum(),
                                                        sortedList.get(i).getSum() / (sortedList
                                                                .get(0)
                                                                .getSum() - sortedList
                                                                .get(i)
                                                                .getSum()),
                                                        index.get(),
                                                        i)
                                        );
                                    }

                                    index.getAndIncrement();
                                    return sortedList;
                                })));


        List<ShopGroup> shopGroups = shopGroupCacheRepository.shopGroupByIds(new ArrayList<>(shopGroupSet));
        Map<Integer, String> shopGroupLimits = shopGroups.stream()
                .collect(Collectors.toMap(ShopGroup::getId, ShopGroup::getLimit));

        Arrays.sort(topInPurchase.toArray());

        topInPurchase.sort(Comparator.comparingInt(p -> p.rate));


        for (int i = 0; i < topInPurchase.size(); i++) {


        }


        List<List<Integer>> shopGroupSelection = new ArrayList<>();
        Map<Integer, Integer> freeLimit = new HashMap<>();
        int min = 0;
        int minK = -1;
        int k = 0;
        while (withdrawLimit > 0) {

            int[] startPoint = new int[map.size()];

            int i = 0;
            for (List<PurchaseShopGroup> list : map.values()) {

                for (int j = startPoint[i]; j < list.size(); j++) {
                    final PurchaseShopGroup purchaseShopGroup = list.get(j);
                    final Integer shopGroup = purchaseShopGroup.getShopGroup();
                    if (freeLimit.get(shopGroup) > purchaseShopGroup.getSum()) {
                        final int sum = purchaseShopGroup.getSum();
                        freeLimit.put(shopGroup, freeLimit.get(shopGroup) - sum);
                        withdrawLimit -= sum;
                        startPoint[i] = j;
                        break;
                    }
                }
                i++;
            }
            if (min < withdrawLimit) {
                min = withdrawLimit;
                minK = k;
            }
            k++;
        }


    }


    static class Quatro {
        int sum;
        int rate;
        int purchaseIndex;
        int purchaseShopGroupIndex;

        Quatro(int sum, int rate, int purchaseIndex, int purchaseShopGroupIndex) {
            this.sum = sum;
            this.rate = rate;
            this.purchaseIndex = purchaseIndex;
            this.purchaseShopGroupIndex = purchaseShopGroupIndex;
        }
    }

}
