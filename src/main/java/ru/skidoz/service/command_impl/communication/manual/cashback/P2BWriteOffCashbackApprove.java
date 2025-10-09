package ru.skidoz.service.command_impl.communication.manual.cashback;

import java.io.IOException;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.util.Calculator;

import static ru.skidoz.util.Optimizator.getRate;

/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2BWriteOffCashbackApprove implements Command {
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private Calculator calculator;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    //@Transactional
    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getCurrentConversationShop();
        Long buyerChatId = users.getChatId();

        System.out.println();
        System.out.println();
        System.out.println("----------------------------------P2BWriteOffCashbackApprove-----------------------------");
        System.out.println();

        String code = update.getCallbackQuery().getData().substring(19);

        System.out.println("code+++++++++++++++++++++" + code);
        System.out.println();

        try {
            CashbackWriteOff cashbackWriteOff = cashbackWriteOffCacheRepository.findById(Integer.valueOf(code));
            Integer proposedSum = cashbackWriteOff.getSum();

            System.out.println("0 proposedSum********" + proposedSum);

            List<Cashback> cashbackBasicDefaultList = new ArrayList<>();//cashbackRepository.findAllByUserAndShopAndAction_Type(friend, shopGetter, ActionTypeEnum.BASIC_DEFAULT);
            List<Cashback> cashbackManualList = new ArrayList<>();//cashbackRepository.findAllByUserAndShopAndAction_Type(friend, shopGetter, ActionTypeEnum.BASIC_MANUAL);
            Shop shopInitial = shopCacheRepository.findBySellerChatId(shopChatId);


            List<Cashback> cashbackList = cashbackCacheRepository.findAllByUser_IdAndShop_Id(users.getId(), shopInitial.getId());
            cashbackList.forEach(cashback -> {
                ActionTypeEnum type = actionCacheRepository.findById(cashback.getAction()).getType();
                if (type.equals(ActionTypeEnum.BASIC_DEFAULT)) {
                    cashbackBasicDefaultList.add(cashback);
                } else if (type.equals(ActionTypeEnum.BASIC_MANUAL)) {
                    cashbackManualList.add(cashback);
                }
            });


            System.out.println(" " + cashbackBasicDefaultList);
            System.out.println("BASIC_MANUAL  " + cashbackManualList);


            if (proposedSum > 0) {
                for (Cashback cashbackManual : cashbackManualList) {
                    Purchase purchase = purchaseCacheRepository.findById(cashbackManual.getPurchase());

                    System.out.println(purchase.getSum() + "+++ purchase.getSum() " + proposedSum);

                    if (purchase.getSum() > proposedSum) {

                        purchase.setSum(purchase.getSum() - proposedSum);
                        purchaseCacheRepository.save(purchase);
                        proposedSum = 0;
                        break;
                    } else {
                        proposedSum = proposedSum - purchase.getSum();
                        purchaseCacheRepository.delete(purchase);

                        System.out.println("1 proposedSum********" + proposedSum);
                    }
                }
            }

            System.out.println("2 proposedSum********" + proposedSum);

            if (proposedSum > 0) {

                Integer sumBasicDefault = 0;

                for (Cashback cashbackBasicDefault : cashbackBasicDefaultList) {
                    Purchase purchase = purchaseCacheRepository.findById(cashbackBasicDefault.getPurchase());
                    if (purchase.getSum() != null
                            && purchase.getSum().intValue() > 0) {
                        sumBasicDefault = sumBasicDefault + purchase.getSum();
                    }
                }

                System.out.println("sumBasicDefault********" + sumBasicDefault);

                Action actionDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shopInitial.getId(), ActionTypeEnum.BASIC_DEFAULT);
                int rateDefault = getRate(
                        sumBasicDefault,
                        actionDefault.getLevelSumList(),
                        actionDefault.accessLevelRatePreviousPurchaseList());

                sumBasicDefault = sumBasicDefault * rateDefault;

                System.out.println("rateDefault********" + rateDefault);
                System.out.println("sumBasicDefault********" + sumBasicDefault);


                if (sumBasicDefault.compareTo(proposedSum) > 0) {

                    Integer ratio = 100 - proposedSum * 100 / sumBasicDefault;

                    System.out.println("ratio********" + ratio);

                    for (Cashback cashbackBasicDefault : cashbackBasicDefaultList) {
                        Purchase purchase = purchaseCacheRepository.findById(cashbackBasicDefault.getPurchase());

                        final Integer multiply = purchase.getSum() * ratio;

                        System.out.println("ratio********" + ratio);
                        System.out.println("multiply********" + multiply);

                        purchase.setSum(multiply);
                        purchaseCacheRepository.save(purchase);
                    }

                    proposedSum = 0;
                } else {

                    proposedSum = proposedSum - sumBasicDefault;

                    System.out.println("3 proposedSum********" + proposedSum);

                    for (Cashback cashbackBasicDefault : cashbackBasicDefaultList) {
                        purchaseCacheRepository.deleteById(cashbackBasicDefault.getPurchase());
                        cashbackCacheRepository.deleteById(cashbackBasicDefault.getId(), cashbackBasicDefault.getPurchase(), actionDefault.getId(), ActionTypeEnum.BASIC_DEFAULT);
                    }
                }
            }


            System.out.println("4 proposedSum********" + proposedSum);

            if (proposedSum > 0) {


                List<PartnerGroup> partnerList = partnerGroupCacheRepository.findAllByShop_Id(shopInitial.getId());
                StringBuilder cashbackMessagePartner = new StringBuilder("\r\n");
                Integer sumPartners = 0;

                Map<Integer, Integer> shopNonReachableSum = new HashMap<>();
                Map<Integer, Integer> shopResultSum = new HashMap<>();
                Map<Integer, Integer> shopUserSum = new HashMap<>();
//                Set<Integer> partnerShopSet = new HashSet<>();
//                Map<Integer, PartnerGroup> shopPartners = new HashMap<>();
//                Map<Integer, Integer> shopFreeLimit = new HashMap<>();
                Integer maxFreeLimitGroup = 0;

                for (PartnerGroup partnerGroup : partnerList) {

                    final ShopGroup shopGroup = shopGroupCacheRepository.findById(partnerGroup.getShopGroup());


                    int limit = shopGroup.getLimit();

                    System.out.println("2 partner **********" + partner);

                    Partner reversePartner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(partnerShop, shopInitial.getId());

                    BigDecimal balance;
                    if (reversePartner != null) {
                        balance = partner.getSum().subtract(reversePartner.getSum());
                    } else {
                        balance = partner.getSum();
                    }

                    BigDecimal freeLimit;
                    if (balance.compareTo(limit) > 0) {
                        freeLimit = BigDecimal.ZERO;
                    } else {
                        freeLimit = limit.subtract(balance);
                    }
                    shopFreeLimit.put(partnerShop, freeLimit);


                    System.out.println("3 freeLimit********" + freeLimit);


                    BigDecimal sum = calculator.purchaseSumByUserAndShopAndAction_Type(users.getId(), partnerShop, ActionTypeEnum.BASIC_MANUAL);

                    System.out.println("4 proposedSum BASIC_MANUAL**********" + sum);

                    if (proposedSum.compareTo(sum) > 0) {

                        Action actionPartnerBasicDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(partnerShop, ActionTypeEnum.BASIC_DEFAULT);
//            List<Cashback> cashbackPartnerList = cashbackRepository.findAllByUserAndAction(friend, actionPartnerBasicDefault);
//            BigDecimal sum = cashbackPartnerList.stream().map(cashback -> cashback.getPurchase().getSum()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal totalSum = calculator.purchaseSumByUserAndShopAndAction_Type(users.getId(), partnerShop, ActionTypeEnum.BASIC_DEFAULT);

                        int ratePartnerDefault = getRate(
                                totalSum,
                                actionPartnerBasicDefault.getLevelSumList(),
                                actionPartnerBasicDefault.accessLevelRatePreviousPurchaseList());


                        System.out.println("5 totalSum **********" + totalSum);

                        sum = sum.add(totalSum.multiply(BigDecimal.valueOf(ratePartnerDefault)).divide(new BigDecimal(100), 4, RoundingMode.CEILING));

                        System.out.println("6 sum **********" + sum);

                        shopUserSum.put(partnerShop, sum);

//            List<Cashback> cashbackPartnerShopManualList = cashbackRepository.findAllByUserAndShopAndAction_Type(friend, partnerShop, ActionTypeEnum.BASIC_MANUAL);
//            for (Cashback cashbackPartnerShopManual : cashbackPartnerShopManualList) {
//                Purchase purchase = cashbackPartnerShopManual.getPurchase();
//                if (purchase.getSum() != null
//                        && purchase.getSum().intValue() > 0) {
//                    sum = sum.add(purchase.getSum());
//                }
//            }
                        Integer resultSum;
                        if (sum.compareTo(freeLimit) > 0) {
                            resultSum = freeLimit;
                            shopNonReachableSum.put(partnerShop, sum.subtract(freeLimit));
                        } else {
                            resultSum = sum;
                            shopNonReachableSum.put(partnerShop, 0);
                        }
                        shopResultSum.put(partnerShop, resultSum);

                        sumPartners = sumPartners.add(resultSum);
                    } else {
                        break;
                    }
                }

                System.out.println("cashbackMessagePartner***" + cashbackMessagePartner);

                List<Map.Entry<Integer, Integer>> list = new ArrayList<>(shopNonReachableSum.entrySet());
                list.sort(Map.Entry.comparingByValue());

                List<Integer> resultNonReachableSumMap = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : list) {
                    resultNonReachableSumMap.add(entry.getKey());
                }

                PartnerGroup maxPartnerGroup = null;
                Integer maxResultSum = sumPartners;
//                Map<Integer, Integer> maxPartnerShops = shopResultSum;
                Set<Integer> maxPartners = new HashSet<>();
/////////////////////////////////////////////////////////////// PARTNER GROUP

                List<PartnerGroup> partnerGroupList = partnerGroupCacheRepository.findAllByCreditor_Id(shopInitial.getId());
                for (PartnerGroup partnerGroup : partnerGroupList) {

                    ShopGroup partnerShopGroup = shopGroupCacheRepository.findById(partnerGroup.getShopGroup());
//            if (userShopGroup_SumListHashMap.containsKey(partnerShopGroup)) {
                    Integer balance = partnerGroup.getSum();
                    Integer limit = partnerGroup.getLim();
                    Integer freeLimitGroup = limit - balance;
                    Integer userSum = 0;


                    System.out.println("6 partnerGroup**************** " + partnerGroup);

//                TODO-по идее, можно отказаться от PurchaseShopGroup вообще как от класса???
//                List<PurchaseShopGroup> purchaseShopGroups = userShopGroup_CashbackMap.get(partnerShopGroup);
                    Map<Integer, Boolean> shopIsGroupMemberMap = new HashMap<>();
                    partnerShopSet.forEach(shopPartner -> shopIsGroupMemberMap.put(shopPartner, false));

                    for (Shop shop : partnerShopGroup.getShopSet()) {

                        if (proposedSum > userSum) {

                            if (shopUserSum.containsKey(shop.getId())) {

                                userSum = userSum + shopUserSum.get(shop);
                            } else {
                                int sum = calculator.purchaseSumByUserAndShopAndAction_Type(users.getId(), shop.getId(), ActionTypeEnum.BASIC_MANUAL);


                                System.out.println("7 BASIC_MANUAL********" + sum);


                                if (proposedSum.compareTo(userSum.add(sum)) > 0) {


                                    Action actionPartnerBasicDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shop.getId(), ActionTypeEnum.BASIC_DEFAULT);
                                    sum = calculator.purchaseSumByUserAndShopAndAction_Type(users.getId(), shop.getId(), ActionTypeEnum.BASIC_DEFAULT);


                                    System.out.println("8 BASIC_DEFAULT********" + sum);


                                    int ratePartnerDefault = getRate(
                                            sum,
                                            actionPartnerBasicDefault.getLevelSumList(),
                                            actionPartnerBasicDefault.accessLevelRatePreviousPurchaseList());

                                    sum = sum * ratePartnerDefault / 100;
                                }

                                shopUserSum.put(shop.getId(), sum);

                                userSum = userSum.add(shopUserSum.get(shop));

                            }

                            if (shopIsGroupMemberMap.containsKey(shop)) {
                                shopIsGroupMemberMap.put(shop.getId(), true);
                            }
                        } else {
                            break;
                        }
                    }
//            BigDecimal sum = purchaseShopGroupList.stream().map(cashback -> cashback.getPurchase().getSum()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    //BigDecimal sum = purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), false);
//sum = sum.add(purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), true));
                    BigDecimal basicGroupResultSum;
                    if (userSum.compareTo(freeLimitGroup) > 0) {
                        basicGroupResultSum = freeLimitGroup;
                    } else {
                        basicGroupResultSum = userSum;
                    }

                    for (Map.Entry<Integer, Boolean> entry : shopIsGroupMemberMap.entrySet()) {
                        if (!entry.getValue()) {
                            basicGroupResultSum = basicGroupResultSum.add(shopResultSum.get(entry.getKey()));
                        }
                    }

                    if (basicGroupResultSum.compareTo(maxResultSum) > 0) {
                        maxResultSum = basicGroupResultSum;
                        maxPartnerGroup = partnerGroup;
                        maxPartners = new HashSet<>();
                        maxFreeLimitGroup = freeLimitGroup;
                    }


                    Set<Integer> resultNonReachableSumTrySet = new HashSet<>();
                    for (Integer resultNonReachableSum : resultNonReachableSumMap) {

                        resultNonReachableSumTrySet.add(resultNonReachableSum);

                        Map<Integer, Boolean> tryShopIsGroupMemberMap = new HashMap<>();
                        partnerShopSet.forEach(shop -> tryShopIsGroupMemberMap.put(shop, false));

                        BigDecimal tryUserSum = BigDecimal.ZERO;
                        for (Shop shop : partnerShopGroup.getShopSet()) {
                            // что магазин из группы не содержится в списке замещаемых партнеров
                            if (proposedSum.compareTo(userSum) > 0) {
                                if (!resultNonReachableSumTrySet.contains(shop)) {
                                    if (shopUserSum.containsKey(shop)) {
                                        tryUserSum = tryUserSum.add(shopUserSum.get(shop));
                                    }

                                    if (tryShopIsGroupMemberMap.containsKey(shop)) {
                                        tryShopIsGroupMemberMap.put(shop.getId(), true);
                                    }
                                }
                            }
                        }
//            BigDecimal sum = purchaseShopGroupList.stream().map(cashback -> cashback.getPurchase().getSum()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        //BigDecimal sum = purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), false);
//sum = sum.add(purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), true));
                        BigDecimal tryResultSum;
                        if (tryUserSum.compareTo(freeLimitGroup) > 0) {
                            tryResultSum = freeLimitGroup;
                        } else {
                            tryResultSum = tryUserSum;
                        }

                        for (Map.Entry<Integer, Boolean> isGroupMember : tryShopIsGroupMemberMap.entrySet()) {
                            if (!isGroupMember.getValue()) {
                                tryResultSum = tryResultSum.add(shopResultSum.get(isGroupMember.getKey()));
                            }
                        }

                        if (tryResultSum.compareTo(maxResultSum) > 0) {
                            maxResultSum = tryResultSum;
                            maxPartnerGroup = partnerGroup;
                            maxPartners = resultNonReachableSumTrySet;
                            maxFreeLimitGroup = freeLimitGroup;
                        }
                    }
//            }
                }


///начинаем отнимать


//                List<Partner> partnerList = partnerCacheRepository.findAllByCreditor_Id(shop);

                for (Integer partnerShop : maxPartners) {

                    Partner partner = shopPartners.get(partnerShop);
                    BigDecimal freeLimit = shopFreeLimit.get(partnerShop);

                    BigDecimal withdrawal;
                    if (proposedSum.compareTo(freeLimit) > 0) {
                        withdrawal = freeLimit;
                    } else {
                        withdrawal = proposedSum;
                    }
                    proposedSum = proposedSum.subtract(withdrawal);


                    List<Cashback> cashbackPartnerShopManualList = cashbackCacheRepository.findAllByUser_IdAndShop_IdAndAction_Type(users.getId(), partnerShop, ActionTypeEnum.BASIC_MANUAL);
                    for (Cashback cashbackPartnerShopManual : cashbackPartnerShopManualList) {
                        Purchase purchase = purchaseCacheRepository.findById(cashbackPartnerShopManual.getPurchase());
                        BigDecimal purchaseCashback = purchase.getSum();
                        if (purchaseCashback != null
                                && purchaseCashback.intValue() > 0) {

                            if (withdrawal.compareTo(purchaseCashback) > 0) {
                                withdrawal = withdrawal.subtract(purchaseCashback);
                                purchaseCacheRepository.delete(purchase);
                            } else {
                                purchase.setSum(purchase.getSum().subtract(withdrawal));
                                purchaseCacheRepository.save(purchase);
                                withdrawal = BigDecimal.ZERO;
                                break;
                            }
                        }
                    }

                    if (withdrawal.compareTo(BigDecimal.ZERO) > 0) {

                        Action actionPartnerBasicDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(partnerShop, ActionTypeEnum.BASIC_DEFAULT);
                        List<Cashback> cashbackPartnerList = cashbackCacheRepository.findAllByUser_IdAndAction_Id(users.getId(), actionPartnerBasicDefault.getId());
                        List<Purchase> purchaseList = purchaseCacheRepository.findAllByIdIsIn(cashbackPartnerList.stream().map(Cashback::getPurchase).collect(Collectors.toList()));
                        BigDecimal sum = purchaseList.stream().map(Purchase::getSum).reduce(BigDecimal.ZERO, BigDecimal::add);
//                    BigDecimal sum =  cashbackRepository.purchaseSumByUserAndShopAndAction_Type(friend.getId(), partnerShop.getId(), ActionTypeEnum.BASIC_DEFAULT.getValue());
                        int ratePartnerDefault = getRate(
                                sum,
                                actionPartnerBasicDefault.getLevelSumList(),
                                actionPartnerBasicDefault.accessLevelRatePreviousPurchaseList());

                        for (Purchase purchase : purchaseList) {
                            if (withdrawal.compareTo(BigDecimal.ZERO) > 0) {

                                BigDecimal purchaseCashback = purchase.getSum()
                                        .multiply(BigDecimal.valueOf(ratePartnerDefault))
                                        .divide(new BigDecimal(100), 4, RoundingMode.CEILING);

                                if (withdrawal.compareTo(purchaseCashback) > 0) {
                                    withdrawal = withdrawal.subtract(purchaseCashback);
                                    purchaseCacheRepository.delete(purchase);
                                } else {
                                    purchase.setSum(purchase.getSum().subtract(
                                            withdrawal
                                                    .multiply(new BigDecimal(100))
                                                    .divide(BigDecimal.valueOf(ratePartnerDefault), 4, RoundingMode.CEILING)));
                                    purchaseCacheRepository.save(purchase);
                                    withdrawal = BigDecimal.ZERO;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }

                        proposedSum = proposedSum.add(withdrawal);

                        partner.setSum(partner.getSum().add(withdrawal));
                        partnerCacheRepository.save(partner);

                        List<PartnerGroup> partnerGroupAddWithdrawalList = partnerGroupCacheRepository.findAllByCreditorAndDebtorInGroup(shopInitial.getId(), partnerShop);
                        for (PartnerGroup partnerGroup : partnerGroupAddWithdrawalList) {

                            partnerGroup.setSum(partnerGroup.getSum().add(withdrawal));
                            partnerGroupCacheRepository.save(partnerGroup);
                        }
                    } else {
                        break;
                    }
                }

                BigDecimal withdrawSum;
                if (proposedSum.compareTo(maxFreeLimitGroup) > 0) {
                    withdrawSum = maxFreeLimitGroup;
                } else {
                    withdrawSum = proposedSum;
                }
                proposedSum = proposedSum.subtract(withdrawSum);

                ShopGroup partnerShopGroup = shopGroupCacheRepository.findById(maxPartnerGroup.getShopGroup());
                for (Shop groupShop : partnerShopGroup.getShopSet()) {

                    if (withdrawSum.compareTo(BigDecimal.ZERO) > 0) {

                        if (maxPartners.contains(groupShop)
                                && shopUserSum.containsKey(groupShop)) {


                            List<Cashback> cashbackPartnerShopManualList = cashbackCacheRepository.findAllByUser_IdAndShop_IdAndAction_Type(users.getId(), groupShop.getId(), ActionTypeEnum.BASIC_MANUAL);
                            for (Cashback cashbackPartnerShopManual : cashbackPartnerShopManualList) {
                                Purchase purchase = purchaseCacheRepository.findById(cashbackPartnerShopManual.getPurchase());
                                BigDecimal purchaseCashback = purchase.getSum();
                                if (purchaseCashback != null
                                        && purchaseCashback.intValue() > 0) {

                                    if (withdrawSum.compareTo(purchaseCashback) > 0) {
                                        withdrawSum = withdrawSum.subtract(purchaseCashback);
                                        purchaseCacheRepository.delete(purchase);
                                    } else {
                                        purchase.setSum(purchase.getSum().subtract(withdrawSum));
                                        purchaseCacheRepository.save(purchase);
                                        withdrawSum = BigDecimal.ZERO;
                                        break;
                                    }
                                }
                            }

                            if (withdrawSum.compareTo(BigDecimal.ZERO) > 0) {

                                Action actionPartnerBasicDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(groupShop.getId(), ActionTypeEnum.BASIC_DEFAULT);
                                List<Cashback> cashbackPartnerList = cashbackCacheRepository.findAllByUser_IdAndAction_Id(users.getId(), actionPartnerBasicDefault.getId());
                                List<Purchase> purchaseList = purchaseCacheRepository.findAllByIdIsIn(cashbackPartnerList.stream().map(Cashback::getPurchase).collect(Collectors.toList()));
                                BigDecimal sum = purchaseList.stream().map(Purchase::getSum).reduce(BigDecimal.ZERO, BigDecimal::add);

//                    BigDecimal sum =  cashbackRepository.purchaseSumByUserAndShopAndAction_Type(friend.getId(), partnerShop.getId(), ActionTypeEnum.BASIC_DEFAULT.getValue());

                                int ratePartnerDefault = getRate(
                                        sum,
                                        actionPartnerBasicDefault.getLevelSumList(),
                                        actionPartnerBasicDefault.accessLevelRatePreviousPurchaseList());

                                for (Purchase purchase : purchaseList) {
                                    if (withdrawSum.compareTo(BigDecimal.ZERO) > 0) {

                                        BigDecimal purchaseCashback = purchase.getSum()
                                                .multiply(BigDecimal.valueOf(ratePartnerDefault))
                                                .divide(new BigDecimal(100), 4, RoundingMode.CEILING);

                                        if (withdrawSum.compareTo(purchaseCashback) > 0) {
                                            withdrawSum = withdrawSum.subtract(purchaseCashback);
                                            purchaseCacheRepository.delete(purchase);
//                                    proposedSum = proposedSum.subtract(purchaseCashback);
                                        } else {
                                            BigDecimal withdrawal = withdrawSum
                                                    .multiply(new BigDecimal(100))
                                                    .divide(BigDecimal.valueOf(ratePartnerDefault));

                                            purchase.setSum(purchase.getSum().subtract(withdrawal));
                                            purchaseCacheRepository.save(purchase);
//                                    proposedSum = proposedSum.subtract(withdrawal);
                                            withdrawSum = BigDecimal.ZERO;
                                            break;
                                        }
                                    }
                                }
                                proposedSum = proposedSum.add(withdrawSum);
                            }
                        }
                    }
                }
                System.out.println("++++++++++++++++++++++++proposedSum++++++++++++++++++" + proposedSum);
            }

            BigDecimal subtracted = cashbackWriteOff.getSum().subtract(proposedSum);

            cashbackWriteOffCacheRepository.delete(cashbackWriteOff);

            resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE,
                    false,
                    false);
            Message message = new Message(null, Map.of(LanguageEnum.RU, "@Списано " + subtracted));
            resultLevel.addMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        LevelDTOWrapper finalLevel = resultLevel;
        Long finalBuyerChatId = buyerChatId;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(finalBuyerChatId);
            e.setLevel(finalLevel);
        }));
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalLevel);
        }));

        return new LevelResponse(levelChatDTOList, null, null);
    }

}
