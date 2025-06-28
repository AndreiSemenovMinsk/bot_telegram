package ru.skidoz.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.BasketCacheRepository;
import ru.skidoz.aop.repo.BasketProductCacheRepository;
import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.PurchaseCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.BasketProduct;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.side.Product;

import static ru.skidoz.util.ProductChecker.checkProductSource;
import static ru.skidoz.util.Structures.makeEmptyArrayList;

/**
 * @author andrey.semenov
 */
@Component
public class Optimizator {

    @Autowired
    private ActionCacheRepository actionRepository;
    @Autowired
    private CashbackCacheRepository cashbackRepository;
    @Autowired
    private ProductCacheRepository productRepository;
    @Autowired
    private BasketProductCacheRepository basketProductRepository;
    @Autowired
    private BasketCacheRepository basketRepository;
    @Autowired
    private PurchaseCacheRepository purchaseRepository;

    public static Integer getRate(BigDecimal sum,
                                  List<BigDecimal> levelSum,
                                  List<Integer> levelRatePreviousPurchase) {
        if (levelRatePreviousPurchase.size() > 0) {
            int i = 0;
            for (BigDecimal level : levelSum) {

                if (level.compareTo(sum) >= 0) {
                    return levelRatePreviousPurchase.get(i);
                }
                i++;
            }
            return levelRatePreviousPurchase.get(levelRatePreviousPurchase.size() - 1);
        }
        return null;
    }


    public Action getOptimal(Integer userId,
                             Integer shopId,
                             List<Product>  productDTOS,
                             Map<Integer, BigDecimal> actionProductInitialSum,
                             List<Action>  suitableActions,
                             Map<Integer, List<BigDecimal>> actionProductSumByProducts,
                             BigDecimal max) {


        Map<Integer, BigDecimal> actionProductSum = new HashMap<>();

        List<Basket>  basketList = basketRepository.findAllByUserIdAndShopIdAndTemp(
                userId, shopId, true);
        List<BasketProduct>  basketProducts = new ArrayList<>();
        basketList.forEach(basket -> {

            basketProducts.addAll(basketProductRepository.findAllByBasketId(basket.getId()));

            basket.setTemp(false);
            basketRepository.save(basket);
        });

        Action actionDefault = actionRepository.findFirstByShopAndTypeAndActiveIsTrue(
                shopId, ActionTypeEnum.BASIC_DEFAULT);
        List<Action>  actionBasicList = actionRepository.findAllByShopAndTypeAndActiveIsTrue(
                shopId, ActionTypeEnum.BASIC);

        List<Action>  allActions = new ArrayList<>(actionBasicList);
        allActions.add(actionDefault);

        int basketProductInd = 0;
        for (BasketProduct basketProduct : basketProducts) {

            Product product = productRepository.findById(basketProduct.getProduct());
            BigDecimal sumAdd = product.getPrice()
                    .multiply(BigDecimal.valueOf(basketProduct.getProductAmount()));
            productDTOS.add(product);

            for (Action action : allActions) {

                if (ActionTypeEnum.BASIC_DEFAULT.equals(action.getType())
                        || checkProductSource(action, product)) {

                    var purchaseByActionInitialSum = actionProductInitialSum.get(action.getId());

                    if (purchaseByActionInitialSum == null) {

                        final List<Cashback>  cashbacks = cashbackRepository
                                .findAllByUser_IdAndAction_Id(userId, action.getId());

                        purchaseByActionInitialSum = BigDecimal.ZERO;
                        BigDecimal purchaseByActionSum = BigDecimal.ZERO;
                        for (Cashback cashback : cashbacks) {

                            final Purchase purchaseByAction = purchaseRepository.findById(cashback.getPurchase());

                            purchaseByActionSum = purchaseByActionSum.add(purchaseByAction.getSum());
                            purchaseByActionInitialSum = purchaseByActionInitialSum.add(purchaseByAction.getInitialSum());
                        }

                        actionProductInitialSum.put(action.getId(), purchaseByActionInitialSum);
                        actionProductSum.put(action.getId(), purchaseByActionSum);

                        actionProductSumByProducts.put(action.getId(), makeEmptyArrayList(basketProducts.size()));

                        suitableActions.add(action);
                    }
                    actionProductSumByProducts.get(action.getId()).set(basketProductInd, sumAdd);
                    purchaseByActionInitialSum = purchaseByActionInitialSum.add(sumAdd);
                }
            }
            basketProductInd++;
        }

        Action bestAction = null;
        for (var action : suitableActions) {
            var actionAccumSum = actionProductInitialSum.get(action.getId());
            var actionSum = actionProductSum.get(action.getId());

            int rate = getRate(
                    actionAccumSum,
                    action.getLevelSumList(),
                    action.accessLevelRatePreviousPurchaseList());
            BigDecimal chargedSum = actionAccumSum
                    .multiply(BigDecimal.valueOf(rate))
                    .divide(new BigDecimal(100), 4, RoundingMode.CEILING);


            if (chargedSum.compareTo(actionSum) < 0) {
                chargedSum = actionSum;
            }

//            chargedSum = chargedSum.multiply(BigDecimal.valueOf(action.getRateFuturePurchase())
//                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING));


            if (chargedSum.compareTo(max) > 0) {
                bestAction = action;
                max = chargedSum;
            }
        }

        return bestAction;
    }












//    public List<Object> getOptimalSet(List<Purchase>  previousPurchaseList, List<ProductSum> productSumFromBasket) {
//
//        List<List<Cashback> > matrixVariantPurchaseCashback = new ArrayList<>();
//        // first-time setting of matrixes
//        boolean firstPurchase = true;
//        System.out.println();
//        System.out.println("++++++++++++getOptimalSet++++++++++++");
//        System.out.println();
//        System.out.println("previousPurchaseList+++" + previousPurchaseList.size());
//// проходим по списку старых покупок и заполняем матрицу с вариантами
//        for (Purchase previousPurchase : previousPurchaseList) {
//
//            System.out.println("previousPurchase+++++++++" + previousPurchase);
//
//            cashbackRepository.findAllByPurchase_Id(previousPurchase.getId()).forEach(e -> System.out.println("@@@" + e));
//
//            List<Cashback>  purchaseCashback = cashbackRepository.findAllByPurchase_Id(previousPurchase.getId());
//
//            System.out.println("purchaseCashback.size()+++" + purchaseCashback.size());
////            List<Cashback>  purchaseCashback = previousPurchase.getCashbackList();
//            List<List<Cashback> > copyMatrixVariantPurchaseCashback = new ArrayList<>();
//            if (firstPurchase) {
//
//                for (Cashback cashback : purchaseCashback) {
//
//                    System.out.println("1cashback+++" + cashback);
//
//                    copyMatrixVariantPurchaseCashback.add(new ArrayList<>(List.of(cashback)));
//                }
//
//                System.out.println("1copyMatrixVariantPurchaseCashback+++" + copyMatrixVariantPurchaseCashback.size());
//
//                firstPurchase = false;
//            } else {
//
//                matrixVariantPurchaseCashback.forEach(e -> System.out.println("@" + e));
//
//                for (List<Cashback>  variantPurchaseCashback : matrixVariantPurchaseCashback) {
//
//                    for (Cashback cashback : purchaseCashback) {
//
//                        System.out.println("2cashback+++" + cashback);
//                        variantPurchaseCashback.forEach(e -> System.out.println("@@" + e));
//
//                        List<Cashback>  copyVariantPurchaseCashback = new ArrayList<>(variantPurchaseCashback);
//                        copyVariantPurchaseCashback.add(cashback);
//
//                        copyMatrixVariantPurchaseCashback.add(copyVariantPurchaseCashback);
//                    }
//
//                    System.out.println("2copyMatrixVariantPurchaseCashback+++" + copyMatrixVariantPurchaseCashback.size());
//                }
//            }
//            matrixVariantPurchaseCashback = copyMatrixVariantPurchaseCashback;
//        }
//        /////////////////////начинаем выбирать лучший вариант
//
//        BigDecimal maxVariant = BigDecimal.ZERO;
//        List<List<BigDecimal>> maxMatrixWithdrawActionProduct = new ArrayList<>();
//        List<CashbackSum> maxRateList = new ArrayList<>();
//        int matrixSize = matrixVariantPurchaseCashback.size();
//        int maxPurchaseNumber = 0;
//
//        System.out.println("matrixSize+++" + matrixSize);
//
//        for (int variantIndex = 0; variantIndex < matrixSize; variantIndex++) {
//
//            // разворачиваем в плоскости на группировку по акциям
//            int purchaseNumber = matrixVariantPurchaseCashback.get(variantIndex).size();
//
//            if (purchaseNumber > maxPurchaseNumber) {
//                maxPurchaseNumber = purchaseNumber;
//            }
//
//            System.out.println("variantIndex+++" + variantIndex + "+++purchaseNumber+++" + purchaseNumber);
//
//            //List<BigDecimal> actionSumList = new ArrayList<>();
//            List<Action>  actionList = new ArrayList<>();
//            List<CashbackSum> cashbackSumList = new ArrayList<>();
//            for (int purchaseIndex = 0; purchaseIndex < purchaseNumber; purchaseIndex++) {
//                Cashback cashback = matrixVariantPurchaseCashback.get(variantIndex).get(purchaseIndex);
//                Action action = actionRepository.findById(cashback.getAction());
//
//                System.out.println("purchaseIndex+++" + purchaseIndex + "+++action+++" + action);
//
//                BigDecimal purchaseSum = previousPurchaseList.get(purchaseIndex).getSum();
//                Integer purchaseNumberCoupon = previousPurchaseList.get(purchaseIndex).getNumberCoupon();
//                boolean accommodateSum = action.isAccommodateSum();
//
//                System.out.println("purchaseSum+++" + purchaseSum);
//                System.out.println("purchaseNumberCoupon+++" + purchaseNumberCoupon);
//                System.out.println(accommodateSum);
//
//                if (accommodateSum) {
//                    int actionIndexFound = actionList.indexOf(action);
//                    if (actionIndexFound == -1) {
//                        actionList.add(action);
//                        CashbackSum cashbackSum = new CashbackSum(cashback, action, purchaseSum, purchaseNumberCoupon, action.getRateFuturePurchase());
//                        cashbackSum.addPurchaseIndex(purchaseIndex);
//                        cashbackSumList.add(cashbackSum);
//                    } else {
//                        CashbackSum cashbackSum = cashbackSumList.get(actionIndexFound);
//                        cashbackSum.addPurchaseIndex(purchaseIndex);
//                        if (purchaseSum != null) {
//                            cashbackSum.addActionSum(purchaseSum);
//                        } else {
//                            cashbackSum.addActionCount(purchaseNumberCoupon);
//                        }
//                    }
//                } else {
//                    /*actionSumList.add(cashback.getSum());
//                    actionList.add(action);*/
//                    CashbackSum cashbackSum = new CashbackSum(cashback, action, purchaseSum, purchaseNumberCoupon, action.getRateFuturePurchase());
//                    cashbackSum.addPurchaseIndex(purchaseIndex);
//                    cashbackSumList.add(cashbackSum);
//                }
//            }
//
//            BigDecimal resultSum = BigDecimal.ZERO;
//            //int productSumFromBasketIndex = 0;
//            int i = 0;
//            for (CashbackSum cashbackSum : cashbackSumList) {
//
//                System.out.println("cashbackSum+++" + cashbackSum);
//
//                Action action = cashbackSum.action;
//                BigDecimal actionSum = cashbackSum.actionSum;
//                Integer actionCount = cashbackSum.actionCount;
//
//                System.out.println("actionSum***" + actionSum);
//                System.out.println("action******" + action);
//                System.out.println("action.accessLevelSumList()***" + action.accessLevelSumList());
//
//                action.accessLevelSumList().forEach(e -> System.out.println("action.accessLevelSumList()***" + e));
//                System.out.println("size***" + action.accessLevelSumList().size());
//
//                Integer discount = getRate(
//                        actionSum,
//                        action.accessLevelSumList(),
//                        action.getLevelRatePreviousPurchaseList());
//
//                System.out.println("discount-++" + discount);
//                System.out.println("action-++" + action);
//
//                if ((action.getType().equals(ActionTypeEnum.COUPON_DEFAULT)
//                        || action.getType().equals(ActionTypeEnum.COUPON)) && actionCount >= action.getNumberCoupon()) {
//                    cashbackSumList.get(i).setRateAndDiscount(100 / action.getNumberCoupon());
//                } else {
//
//                    System.out.println("738-++" + cashbackSumList);
//                    System.out.println("739-++" + cashbackSumList.get(i));
//
//
//                    System.out.println("742-++" + action.getRateFuturePurchase());
//
//                    cashbackSumList.get(i).setRateAndDiscount(action.getRateFuturePurchase() * discount / 100);
//                }
//                i++;
//            }
//
//            List<CashbackSum> rateList = cashbackSumList.stream().sorted(Comparator.comparing(CashbackSum::getRateAndDiscount)).collect(Collectors.toList());
//
//            System.out.println("rateList.size()***" + rateList.size());
//            rateList.forEach(e -> System.out.println("CashbackSum***" + e));
//
//            System.out.println("productSumFromBasket.size()***" + productSumFromBasket.size());
//            productSumFromBasket.forEach(e -> System.out.println("ProductSum***" + e));
//
//            //отсортировали по рейтам
//            // [cashbackSum ][ сумма скидки в  порядке, как приходят productSumFromBasket]
//            List<List<BigDecimal>> matrixWithdrawActionProduct = new ArrayList<>();
//            int cashbackSumIndex = 0;
//            for (CashbackSum cashbackSum : rateList) {
//
//                List<BigDecimal> matrixWithdrawAction = new ArrayList<>();
//
//                BigDecimal actionSum = cashbackSum.getActionSum();
//                Integer discount = cashbackSum.getRate();
//                Integer rateAndDiscount = cashbackSum.getRateAndDiscount();
//                Action action = cashbackSum.getAction();
//
//                System.out.println("@actionSum+++" + actionSum);
//                System.out.println("@discount++++" + discount);
//                System.out.println("@rateAndDiscount++++" + rateAndDiscount);
//
//                BigDecimal charging = actionSum.multiply(BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100), 4));
//
////                int productSumIndex = 0;
//                for (ProductSum productSum : productSumFromBasket) {
//
//                    Product product = productSum.getProduct();
//                    BigDecimal sum = productSum.getSum();
//
//                    System.out.println("productSum.getProduct*" + product);
//                    System.out.println("productSum.getSum*" + sum);
//
//                    if (checkProductDTOTarget(action.getProductTarget(), product)) { /*action.getProductTarget().contains(product)*/
//
//                        System.out.println("action.getType()+++++" + action.getType());
//
//                        if (action.getType().equals(ActionTypeEnum.BASIC_DEFAULT)
//                                || action.getType().equals(ActionTypeEnum.BASIC)) {
//
//                            System.out.println("action.getRateFuturePurchase()+++" + action.getRateFuturePurchase());
//
//                            System.out.println("round+++" + BigDecimal.valueOf(action.getRateFuturePurchase()).divide(BigDecimal.valueOf(100), 4));
//
//                            BigDecimal possibleBaseProductBasket = sum.multiply(BigDecimal.valueOf(action.getRateFuturePurchase()).divide(BigDecimal.valueOf(100), 4));
//
//                            System.out.println("possibleBaseProductBasket***" + possibleBaseProductBasket);
//                            System.out.println("charging***" + charging + "*****" + charging.compareTo(possibleBaseProductBasket));
//
//                            if (charging.compareTo(possibleBaseProductBasket) > 0) {
//
//                                matrixWithdrawAction.add(possibleBaseProductBasket);
////                                matrixWithdrawAction.set(productSumIndex, possibleBaseProductBasket);
//
//                                charging = charging.subtract(possibleBaseProductBasket);
//                                rateList.get(cashbackSumIndex).addSubstraction(possibleBaseProductBasket);
//                                resultSum = resultSum.add(possibleBaseProductBasket.multiply(BigDecimal.valueOf(rateAndDiscount)).divide(new BigDecimal(100), 4));
//                                //no need to productSumFromBasket.get(productSumIndex).setSum(BigDecimal.valueByCode(0));
//                            } else {
//                                matrixWithdrawAction.add(charging);
////                                matrixWithdrawAction.set(productSumIndex, charging);
//
//                                //no need to productSumFromBasket.get(productSumIndex).decreaseSum(charging);
//                                resultSum = resultSum.add(charging.multiply(BigDecimal.valueOf(rateAndDiscount)).divide(new BigDecimal(100), 4));
//                                rateList.get(cashbackSumIndex).addSubstraction(charging);
//                                charging = BigDecimal.valueOf(0);
//                            }
//                        } else if (action.getType().equals(ActionTypeEnum.COUPON_DEFAULT)
//                                || action.getType().equals(ActionTypeEnum.COUPON)) {
//                            //TODO тут надо перепроверить
//                            BigDecimal cashbackFromProductBasket = product.getPrice().multiply(BigDecimal.valueOf(cashbackSum.actionCount / action.getNumberCoupon()));
//                            resultSum = resultSum.add(cashbackFromProductBasket);
//                        }
//                    }
//                    //productSumFromBasket.get(productSumFromBasketIndex);
//
////                    productSumIndex++;
//                }
//                matrixWithdrawActionProduct.add(matrixWithdrawAction);
//                cashbackSumIndex++;
//            }
//
//            System.out.println("resultSum***" + resultSum + "*@@@*" + maxVariant);
//
//            if (resultSum.compareTo(maxVariant) > 0) {
//                maxVariant = resultSum;
//                //maxVariantId = variantIndex;
//                maxMatrixWithdrawActionProduct = matrixWithdrawActionProduct;
//                maxRateList = rateList;
//            }
//        }
//
//        //List<Cashback> cashbackList = matrixVariantPurchaseCashback.get(maxVariantId);
//
//
//        List<BigDecimal> withdrawProduct = new ArrayList<>();
//        List<Cashback>  restedCashback = new ArrayList<>();
//        List<BigDecimal> resultPurchaseList = new ArrayList<>();
//        for (int i = 0; i < maxPurchaseNumber; i++) {
//            resultPurchaseList.add(BigDecimal.ZERO);
//        }
//
//        System.out.println("maxRateList.size()***" + maxRateList.size());
//
//        for (CashbackSum cashbackSum : maxRateList) {
//
//            System.out.println("cashbackSum***" + cashbackSum);
//
//            BigDecimal substraction = cashbackSum.getSubstraction();
//            List<Integer> purchaseIndexList = cashbackSum.getPurchaseIndexList();
//
//            System.out.println("purchaseIndexList.size()***" + purchaseIndexList.size());
//
//            for (Integer purchaseIndex : purchaseIndexList) {
//
//                System.out.println(substraction + "purchaseIndexList.size()*" + purchaseIndexList.size());
//
//                BigDecimal withdrawal = substraction.divide(BigDecimal.valueOf(purchaseIndexList.size()));
//
//                System.out.println("purchaseIndex*" + purchaseIndex + " withdrawal*" + withdrawal);
//
//                resultPurchaseList.set(purchaseIndex, withdrawal);
//            }
//            /*Cashback cashback = cashbackSum.getCashback();
//            int j = 0;
//            for (ProductSum pair : productSumFromBasket) {
//                BigDecimal withdrawal = maxMatrixWithdrawActionProduct.get(i).get(j);
//                if (cashback.getSum().compareTo(withdrawal) < 1) {
//                    withdrawProduct.get(j).add(cashback.getSum());
//                    cashback.setSum(BigDecimal.valueByCode(0L));
//                } else {
//                    withdrawProduct.get(j).add(withdrawal);
//                    cashback.decreaseSum(withdrawal);
//                }
//                j++;
//            }
//            restedCashback.add(cashback);*/
//        }
//        //return new LevelResponse(List.of(withdrawProduct, restedCashback));
//        return new LevelResponse(List.of(resultPurchaseList, maxVariant));
//    }
//
//    private static class CashbackSum {
//
//        private BigDecimal actionSum;
//
//        private Integer actionCount;
//
//        private Action action;
//
//        private Cashback cashback;
//
//        private Integer rate;
//
//        private Integer rateAndDiscount;
//
//        private BigDecimal substraction = BigDecimal.valueOf(0);
//
//        private List<Integer> purchaseIndexList = new ArrayList<>();
//
//        CashbackSum(Cashback cashback, Action action, BigDecimal actionSum, Integer actionCount, Integer rate) {
//            this.cashback = cashback;
//            this.action = action;
//            this.actionSum = actionSum;
//            this.actionCount = actionCount;
//            this.rate = rate;
//        }
//
//        void addPurchaseIndex(Integer purchaseIndex) {
//            purchaseIndexList.add(purchaseIndex);
//        }
//
//        List<Integer> getPurchaseIndexList() {
//            return purchaseIndexList;
//        }
//
//        Action getAction() {
//            return action;
//        }
//
//        BigDecimal getActionSum() {
//            return actionSum;
//        }
//
//        void addActionSum(BigDecimal addSum) {
//            actionSum = actionSum.add(addSum);
//        }
//
//        void addSubstraction(BigDecimal substract) {
//            substraction = substraction.add(substract);
//        }
//
//        BigDecimal getSubstraction() {
//            return substraction;
//        }
//
//        Integer getActionCount() {
//            return actionCount;
//        }
//
//        void addActionCount(Integer i) {
//            actionCount += i;
//        }
//
//        Integer getRate() {
//            return rate;
//        }
//
//        Integer getRateAndDiscount() {
//            return rateAndDiscount;
//        }
//
//        Cashback getCashback() {
//            return cashback;
//        }
//
//        void setRateAndDiscount(Integer rateAndDiscount) {
//            this.rateAndDiscount = rateAndDiscount;
//        }
//
//        @Override
//        public String toString() {
//            return "CashbackSum{" +
//                    "actionSum=" + actionSum +
//                    ", actionCount=" + actionCount +
//                    ", action=" + action +
//                    ", cashback=" + cashback +
//                    ", rate=" + rate +
//                    ", rateAndDiscount=" + rateAndDiscount +
//                    ", substraction=" + substraction +
//                    '}';
//        }
//    }
//
//    public static class ProductSum {
//
//        private Product product;
//
//        private BigDecimal sum;
//
//        public ProductSum(Product product, BigDecimal sum) {
//            this.product = product;
//            this.sum = sum;
//        }
//
//        public BigDecimal getSum() {
//            return sum;
//        }
//
//        public Product getProduct() {
//            return product;
//        }
//
//        public void setSum(BigDecimal sum) {
//            this.sum = sum;
//        }
//
//        public void decreaseSum(BigDecimal decreaseSum) {
//            sum = sum.subtract(decreaseSum);
//        }
//    }
}
