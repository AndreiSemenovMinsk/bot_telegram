package ru.skidoz.service.command_impl.search_goods;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.*;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.util.CalculatorPartners;
import ru.skidoz.util.TelegramElementsUtil;

import static ru.skidoz.util.Optimizator.getRate;

/**
 * @author andrey.semenov
 */
@Component
public class ConnectShop implements Command {

    @Autowired
    private CalculatorPartners calculatorPartners;
    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private BasketProductCacheRepository basketProductCacheRepository;
    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private TelegramElementsUtil telegramElementsUtil;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                false);

        System.out.println();
        System.out.println("-----------------------ConnectShop---------------------");
        System.out.println();
        System.out.println(level.getCallName());
        System.out.println(update.getCallbackQuery() != null);

        if (update.getCallbackQuery() != null && update.getCallbackQuery().getData() != null) {

            System.out.println("update.getCallbackQuery().getData()" + update.getCallbackQuery().getData());

            String code = update.getCallbackQuery().getData().substring(19);

            System.out.println("code***" + code);

            Shop shopGetter = shopCacheRepository.findById(Integer.valueOf(code));

            User shopUsers = userCacheRepository.findByChatId(shopGetter.getChatId());

            return new LevelResponse(getInfoLevel(shopUsers, users, shopGetter), null, null);
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }


    public List<LevelChat> getInfoLevel(User shopUsers, User friend, Shop shopGetter) {

        System.out.println("friend++++++" + friend);
        System.out.println("shopUsers+++++++" + shopUsers);
        System.out.println("shopGetter+" + shopGetter);


        shopGetter.setCurrentConversationShopUserChatId(friend.getChatId());
        shopCacheRepository.save(shopGetter);

        friend.setCurrentConversationShop(shopUsers.getChatId());
        userCacheRepository.save(friend);

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        //chatId не используется, потому что не важно, от кого приходит запрос - продавца или покупателя

        Long shopChatId = shopUsers.getChatId();

//levelCacheRepository.findFirstByUser_ChatIdAndCallName(shopChatId, P2B.name()
        LevelDTOWrapper shopLevel = initialLevel.convertToLevel(initialLevel.level_P2B,
                false,
                true);

/////////////////////////////////////////////////////////////// BASKET

        List<Basket> basketList = basketCacheRepository.findAllByUserIdAndShopIdAndTemp(friend.getId(), shopGetter.getId(), true);
        StringBuilder basketMessage = new StringBuilder("\r\n");
        String shopName = shopGetter.getName();

        System.out.println("basketList.size()***" + basketList.size());

        for (Basket basket : basketList) {

            System.out.println("basket+++" + basket);

            for (BasketProduct basketProduct : basketProductCacheRepository.findAllByBasketId(basket.getId())) {

                Product product = productCacheRepository.findById(basketProduct.getProduct());

                basketMessage.append("\r\n")
                        .append(product.getAlias())
                        .append(" за ")
                        .append(product.getPrice())
                        .append("p.");
            }
            basketMessage.append(basket.getNote());
        }

        System.out.println("friend.getId()***" + friend.getId());

        boolean newUser = true;

        //Message friendBasketMessage = messageRepository.getMessageByLevelAndLevelID(friendLevel, 0);
        if (!basketList.isEmpty()) {
            Message buyerBasketMessage = new Message(null, Map.of(LanguageEnum.RU, "Пользователь " + friend.getName() + "  имеет корзину: " + basketMessage));
            shopLevel.addMessage(buyerBasketMessage);
            newUser = false;

            basketMessage = new StringBuilder("В Вашей корзине ").append(basketMessage);
        }


/////////////////////////////////////////////////////////////// BOOKMARKS

        List<Bookmark> bookmarkList = bookmarkCacheRepository.findAllByUserIdAndShopId(friend.getId(), shopGetter.getId());
        StringBuilder bookmarkMessage = new StringBuilder("\r\n");//""Пользователь " + friend.getName() + "  имеет закладки: ";

        System.out.println("bookmarkList.size()+++" + bookmarkList.size());

        for (Bookmark bookmark : bookmarkList) {

            Product product = productCacheRepository.findById(bookmark.getProduct());

            bookmarkMessage.append("\r\n")
                    .append(product.getName(shopUsers.getLanguage()))
                    .append(" за ")
                    .append(product.getPrice())
                    .append("p.");
            System.out.println("bookmarkMessage***" + bookmarkMessage);
        }
        if (!bookmarkList.isEmpty()) {
            Message buyerBookmarkMessage = new Message(null, Map.of(LanguageEnum.RU, "Пользователь " + friend.getName() + "  имеет закладки: " + bookmarkMessage));
            shopLevel.addMessage(buyerBookmarkMessage);
            newUser = false;

            bookmarkMessage = new StringBuilder("В Ваших закладках ").append(bookmarkMessage);
        }
//        List<Purchase> purchaseList = purchaseRepository.findAllByShopAndUser(shopGetter, friend);

        Integer manualSum = 0;


/////////////////////////////////////////////////////////////// CASHBACKS

        List<Cashback> cashbackBasicDefaultList = new ArrayList<>();//cashbackRepository.findAllByUserAndShopAndAction_Type(friend, shopGetter, ActionTypeEnum.BASIC_DEFAULT);
        List<Cashback> cashbackBasicList = new ArrayList<>();//cashbackRepository.findAllByUserAndShopAndAction_Type(friend, shopGetter, ActionTypeEnum.BASIC);
        List<Cashback> cashbackCouponList = new ArrayList<>();//cashbackRepository.findAllByUserAndShopAndAction_Type(friend, shopGetter, ActionTypeEnum.COUPON);
        List<Cashback> cashbackManualList = new ArrayList<>();//cashbackRepository.findAllByUserAndShopAndAction_Type(friend, shopGetter, ActionTypeEnum.BASIC_MANUAL);

        Set<ActionTypeEnum> actionActiveSet = new HashSet<>();
        List<Cashback> cashbackList = cashbackCacheRepository.findAllByUser_IdAndShop_Id(friend.getId(), shopGetter.getId());
        cashbackList.forEach(cashback -> {

            ActionTypeEnum type = actionCacheRepository.findById(cashback.getAction()).getType();

            if (type.equals(ActionTypeEnum.BASIC)) {
                cashbackBasicList.add(cashback);
            } else if (type.equals(ActionTypeEnum.COUPON) || type.equals(ActionTypeEnum.COUPON_DEFAULT)) {
                cashbackCouponList.add(cashback);
            } else if (type.equals(ActionTypeEnum.BASIC_DEFAULT)) {
                cashbackBasicDefaultList.add(cashback);
            } else if (type.equals(ActionTypeEnum.BASIC_MANUAL)) {
                cashbackManualList.add(cashback);
            }
            actionActiveSet.add(type);
        });

/////////////////////////////////////////////////////////////// CASHBACKS BASIC_DEFAULT

        for (Cashback cashbackBasicDefault : cashbackBasicDefaultList) {
            Purchase purchase = purchaseCacheRepository.findById(cashbackBasicDefault.getPurchase());

            System.out.println(cashbackBasicDefault.getPurchase() + "+-------------+" + purchase);


            if (purchase.getSum() != null
                    && purchase.getSum().intValue() > 0) {
                manualSum = manualSum + purchase.getSum();
            }
        }

        Action actionDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shopGetter.getId(), ActionTypeEnum.BASIC_DEFAULT);

        System.out.println("manualSum+" + manualSum);

        int rateDefault = getRate(
                manualSum,
                actionDefault.getLevelSumList(),
                actionDefault.accessLevelRatePreviousPurchaseList());

        manualSum = manualSum * rateDefault;


/////////////////////////////////////////////////////////////// CASHBACKS BASIC_MANUAL


        for (Cashback cashbackManual : cashbackManualList) {
            Purchase purchase = purchaseCacheRepository.findById(cashbackManual.getPurchase());
            if (purchase.getSum() != null
                    && purchase.getSum().intValue() > 0) {
                manualSum = manualSum + purchase.getSum();
            }
        }

        System.out.println("BASIC_MANUAL manualSum***" + manualSum);

        StringBuilder cashbackMessage = new StringBuilder(" скидка для списания вручную ")
                .append(manualSum / 100)
                .append("р.\r\n");

/////////////////////////////////////////////////////////////// CASHBACKS BASIC

        for (Cashback cashback : cashbackBasicList) {

            Purchase purchase = purchaseCacheRepository.findById(cashback.getPurchase());
            Action action = actionCacheRepository.findById(cashback.getAction());

            if (purchase.getSum() != null
                    && purchase.getSum().intValue() > 0) {
                cashbackMessage.append(" по акции ")
                        .append(action.getDescription())
                        .append(" скидка ")
                        .append(action.getFuturePurchaseRate())
                        .append("% сумма ")
                        .append(purchase.getSum());
            }
            cashbackMessage.append("\r\n");
        }


/////////////////////////////////////////////////////////////// COUPON COUPON_DEFAULT

        HashMap<Action, Integer> actionIntegerMap = new HashMap<>();
        for (Cashback cashback : cashbackCouponList) {

            Purchase purchase = purchaseCacheRepository.findById(cashback.getPurchase());
            Action action = actionCacheRepository.findById(cashback.getAction());

            if (purchase.getNumberCoupon() != null) {

                if (actionIntegerMap.containsKey(action)) {
                    actionIntegerMap.put(action, actionIntegerMap.get(action) + purchase.getNumberCoupon());
                } else {
                    actionIntegerMap.put(action, purchase.getNumberCoupon());
                }
            }
            cashbackMessage.append("\r\n");
        }

        for (Map.Entry<Action, Integer> entry : actionIntegerMap.entrySet()) {
            Action action = entry.getKey();
            Integer numberCoupon = entry.getValue();

            System.out.println();
            System.out.println("action.getType()" + action.getType());
            System.out.println();

            cashbackMessage.append(" по акции ")
                    .append(action.getDescription())
                    .append(" собрано ")
                    .append(numberCoupon)
                    .append(" из ")
                    .append(action.getNumberCoupon())
                    .append(" купонов");
            cashbackMessage.append("\r\n");
        }

        if (!cashbackCouponList.isEmpty()
                || !cashbackBasicList.isEmpty()
                || !cashbackBasicDefaultList.isEmpty()
                || !cashbackManualList.isEmpty()) {

            Message buyerCashbackMessage = new Message(null, Map.of(LanguageEnum.RU, "Пользователь " + friend.getName() + "  имеет купоны: " + cashbackMessage));
            shopLevel.addMessage(buyerCashbackMessage);
            newUser = false;

            cashbackMessage = new StringBuilder("В Ваших кэшбеках ").append(cashbackMessage);
        }
        System.out.println("cashbackMessage***" + cashbackMessage);


/////////////////////////////////////////////////////////////// PARTNER
/*
        List<Partner> partnerList = partnerCacheRepository.findAllByCreditor_Id(shopGetter.getId());
        StringBuilder cashbackMessagePartner = new StringBuilder("\r\n");
        BigDecimal sumPartners = BigDecimal.ZERO;

        Map<Shop, BigDecimal> shopNonReachableSum = new HashMap<>();
        Map<Shop, BigDecimal> shopResultSum = new HashMap<>();
        Map<Shop, BigDecimal> shopUserSum = new HashMap<>();
        Set<Shop> partnerShopSet = new HashSet<>();

        PartnerGroup maxPartnerGroup = null;
        BigDecimal maxResultSum = sumPartners;
//        Set<Shop> maxPartners = new HashSet<>();
//        Map<Shop, Partner> shopPartners = new HashMap<>();


        System.out.println("1 partnerList.size*******" + partnerList.size());


        for (Partner partner : partnerList) {

            System.out.println("2 partner **********" + partner);

            BigDecimal limit = partner.getLim();
            Shop partnerShop = shopCacheRepository.findById(partner.getDebtor());
            partnerShopSet.add(partnerShop);
//            shopPartners.put(partnerShop, partner);

            Partner reversePartner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(partnerShop.getId(), shopGetter.getId());

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

            System.out.println("3 freeLimit********" + freeLimit);


            BigDecimal sum = calculator.purchaseSumByUserAndShopAndAction_Type(friend.getId(), partnerShop.getId(), ActionTypeEnum.BASIC_MANUAL);

            System.out.println("4 proposedSum BASIC_MANUAL**********" + sum);


            Action actionPartnerBasicDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(partnerShop.getId(), ActionTypeEnum.BASIC_DEFAULT);
//            List<Cashback> cashbackPartnerList = cashbackRepository.findAllByUserAndAction(friend, actionPartnerBasicDefault);
//            BigDecimal sum = cashbackPartnerList.stream().map(cashback -> cashback.getPurchase().getSum()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalSum = calculator.purchaseSumByUserAndShopAndAction_Type(friend.getId(), partnerShop.getId(), ActionTypeEnum.BASIC_DEFAULT);

            int ratePartnerDefault = getRate(
                    totalSum,
                    actionPartnerBasicDefault.getLevelSumList(),
                    actionPartnerBasicDefault.accessLevelRatePreviousPurchaseList());


            System.out.println("5 totalSum BASIC_DEFAULT**********" + totalSum);


            sum = sum.add(totalSum
                    .multiply(BigDecimal.valueOf(ratePartnerDefault))
                    .divide(new BigDecimal(100), 4, RoundingMode.CEILING));

            shopUserSum.put(partnerShop, sum);

//            List<Cashback> cashbackPartnerShopManualList = cashbackRepository.findAllByUserAndShopAndAction_Type(friend, partnerShop, ActionTypeEnum.BASIC_MANUAL);
//            for (Cashback cashbackPartnerShopManual : cashbackPartnerShopManualList) {
//                Purchase purchase = cashbackPartnerShopManual.getPurchase();
//                if (purchase.getSum() != null
//                        && purchase.getSum().intValue() > 0) {
//                    sum = sum.add(purchase.getSum());
//                }
//            }

            BigDecimal resultSum;
            if (sum.compareTo(freeLimit) > 0) {
                resultSum = freeLimit;
                shopNonReachableSum.put(partnerShop, sum.subtract(freeLimit));
            } else {
                resultSum = sum;
                shopNonReachableSum.put(partnerShop, BigDecimal.ZERO);
            }
            shopResultSum.put(partnerShop, resultSum);

            cashbackMessagePartner
                    .append(" скидка для списания вручную от партнера ")
                    .append(partnerShop.getName())
                    .append(" ")
                    .append(resultSum.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING))
                    .append("р., баланс ")
                    .append(balance)
                    .append("р., свободный лимит составляет ")
                    .append(freeLimit)
                    .append("р.\r\n");

            sumPartners = sumPartners.add(resultSum);
        }

//        maxPartners = new HashSet<>(partnerShopSet);

        if (!partnerList.isEmpty()) {
            cashbackMessagePartner
                    .append(" всего сумма для списания от партнеров на ")
                    .append(sumPartners)
                    .append("р.\r\n");

            Message buyerCashbackMessagePartner = new Message(null, Map.of(LanguageEnum.RU, "Пользователь " + friend.getName() + "  имеет кэшбеки партнеров: " + cashbackMessagePartner));
            shopLevel.addMessage(buyerCashbackMessagePartner);
            newUser = false;

            cashbackMessagePartner = new StringBuilder("В Ваших кэшбеках партнерские кэшбеки ").append(cashbackMessagePartner);
        }
        System.out.println("cashbackMessagePartner***" + cashbackMessagePartner);

        List<Map.Entry<Shop, BigDecimal>> list = new ArrayList<>(shopNonReachableSum.entrySet());
        list.sort(Map.Entry.comparingByValue());

        List<Shop> resultNonReachableSumList = new ArrayList<>();
        for (Map.Entry<Shop, BigDecimal> entry : list) {
            resultNonReachableSumList.add(entry.getKey());
        }*/


/////////////////////////////////////////////////////////////// PARTNER GROUP

//        List<PartnerGroup> partnerGroupList = partnerGroupCacheRepository.findAllByCreditor_Id(shopGetter.getId());
        StringBuilder cashbackMessagePartnerGroup = new StringBuilder("\r\n");
        Integer sumPartnerGroups = 0;
/*
        List<PurchaseShopGroup> purchaseShopGroupList = purchaseShopGroupRepository.findAllByUserAndManual(friend, false);
        HashMap<ShopGroup, List<PurchaseShopGroup>> userShopGroup_CashbackMap = new HashMap<>();
        HashMap<ShopGroup, BigDecimal> userShopGroup_SumListHashMap = new HashMap<>();
        HashMap<Shop, Map<ShopGroup, BigDecimal>> userShop_ShopGroup_SumMap = new HashMap<>();

        purchaseShopGroupList.forEach(purchaseShopGroup -> {
            if (userShopGroup_CashbackMap.containsKey(purchaseShopGroup.getShopGroup())) {
                userShopGroup_CashbackMap.get(purchaseShopGroup.getShopGroup()).add(purchaseShopGroup);

                BigDecimal sum = userShopGroup_SumListHashMap.get(purchaseShopGroup.getShopGroup());
                userShopGroup_SumListHashMap.put(purchaseShopGroup.getShopGroup(), sum.add(purchaseShopGroup.getPurchase().getSum()));
            } else {
                userShopGroup_CashbackMap.put(purchaseShopGroup.getShopGroup(), new ArrayList<>(List.of(purchaseShopGroup)));

                userShopGroup_SumListHashMap.put(purchaseShopGroup.getShopGroup(), purchaseShopGroup.getPurchase().getSum());
            }

            if (userShop_ShopGroup_SumMap.containsKey(purchaseShopGroup.getShop())) {
                if (userShop_ShopGroup_SumMap.get(purchaseShopGroup.getShop()).containsKey(purchaseShopGroup.getShopGroup())) {

                    BigDecimal sum = userShop_ShopGroup_SumMap.get(purchaseShopGroup.getShop()).get(purchaseShopGroup.getShopGroup());
                    userShop_ShopGroup_SumMap.get(purchaseShopGroup.getShop()).put(purchaseShopGroup.getShopGroup(), sum.add(purchaseShopGroup.getPurchase().getSum()));
                } else {

                    userShop_ShopGroup_SumMap.get(purchaseShopGroup.getShop()).put(purchaseShopGroup.getShopGroup(), purchaseShopGroup.getPurchase().getSum());
                }
            } else {
                Map<ShopGroup, BigDecimal> map = new HashMap<>();
                map.put(purchaseShopGroup.getShopGroup(), purchaseShopGroup.getPurchase().getSum());
                userShop_ShopGroup_SumMap.put(purchaseShopGroup.getShop(), map);
            }
        });*/

        /*for (PartnerGroup partnerGroup : partnerGroupList) {


            System.out.println("6 partnerGroup**************** " + partnerGroup);


            ShopGroup shopGroup = shopGroupCacheRepository.findById(partnerGroup.getShopGroup());
//            if (userShopGroup_SumListHashMap.containsKey(shopGroup)) {
            int balance = partnerGroup.getSum();
            partnerGroup.getShopGroup();
            int limit = shopGroup.getLimit();
            int freeLimit = limit - balance;

            int userSum = 0;//BigDecimal.ZERO;
//                List<PurchaseShopGroup> purchaseShopGroups = userShopGroup_CashbackMap.get(shopGroup);
            Map<Shop, Boolean> shopIsGroupMemberMap = new HashMap<>();
            partnerShopSet.forEach(shop -> shopIsGroupMemberMap.put(shop, false));

            for (Shop shop : shopGroup.getShopSet()) {
                if (shopUserSum.containsKey(shop)) {

                    userSum += shopUserSum.get(shop);
                } else {

                    BigDecimal sum = calculator.purchaseSumByUserAndShopAndAction_Type(friend.getId(), shop.getId(), ActionTypeEnum.BASIC_MANUAL);

                    System.out.println("7 proposedSum BASIC_MANUAL**********" + sum);

                    Action actionPartnerBasicDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shop.getId(), ActionTypeEnum.BASIC_DEFAULT);

                    BigDecimal totalSum = calculator.purchaseSumByUserAndShopAndAction_Type(friend.getId(), shop.getId(), ActionTypeEnum.BASIC_DEFAULT);


                    System.out.println("8 totalSum **********" + totalSum);


                    int ratePartnerDefault = getRate(
                            totalSum,
                            actionPartnerBasicDefault.getLevelSumList(),
                            actionPartnerBasicDefault.accessLevelRatePreviousPurchaseList());

                    sum = sum.add(totalSum
                            .multiply(BigDecimal.valueOf(ratePartnerDefault))
                            .divide(new BigDecimal(100), 4, RoundingMode.CEILING));


                    System.out.println("9 sum **********" + sum);


                    shopUserSum.put(shop, sum);

                    userSum = userSum.add(shopUserSum.get(shop));
                }

                if (shopIsGroupMemberMap.containsKey(shop)) {
                    shopIsGroupMemberMap.put(shop, true);
                }
            }
//            BigDecimal sum = purchaseShopGroupList.stream().map(cashback -> cashback.getPurchase().getSum()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //BigDecimal sum = purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), false);
//sum = sum.add(purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), true));
            int basicGroupResultSum;
            if (userSum > freeLimit) {
                basicGroupResultSum = freeLimit;
            } else {
                basicGroupResultSum = userSum;
            }

            for (Map.Entry<Shop, Boolean> entry : shopIsGroupMemberMap.entrySet()) {
                if (!entry.getValue()) {
                    basicGroupResultSum = basicGroupResultSum.add(shopResultSum.get(entry.getKey()));
                }
            }

            if (basicGroupResultSum.compareTo(maxResultSum) > 0) {
                maxResultSum = basicGroupResultSum;
                maxPartnerGroup = partnerGroup;
//                maxPartners = new HashSet<>();
            }


            Set<Shop> resultNonReachableSumTrySet = new HashSet<>();
            for (Shop resultNonReachableSum : resultNonReachableSumList) {

                resultNonReachableSumTrySet.add(resultNonReachableSum);

                Map<Shop, Boolean> tryShopIsGroupMemberMap = new HashMap<>();
                partnerShopSet.forEach(shop -> tryShopIsGroupMemberMap.put(shop, false));

                BigDecimal tryUserSum = BigDecimal.ZERO;
                for (Shop shop : shopGroup.getShopSet()) {
                    // что магазин из группы не содержится в списке замещаемых партнеров
                    if (!resultNonReachableSumTrySet.contains(shop)) {
                        if (shopUserSum.containsKey(shop)) {
                            tryUserSum = tryUserSum.add(shopUserSum.get(shop));
                        }

                        if (tryShopIsGroupMemberMap.containsKey(shop)) {
                            tryShopIsGroupMemberMap.put(shop, true);
                        }
                    }
                }
//            BigDecimal sum = purchaseShopGroupList.stream().map(cashback -> cashback.getPurchase().getSum()).reduce(BigDecimal.ZERO, BigDecimal::add);
                //BigDecimal sum = purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), false);
//sum = sum.add(purchaseShopGroupRepository.purchaseSumByUserAndShop(friend.getId(), partnerGroup.getDebtor().getId(), true));
                BigDecimal tryResultSum;
                if (tryUserSum.compareTo(freeLimit) > 0) {
                    tryResultSum = freeLimit;
                } else {
                    tryResultSum = tryUserSum;
                }

                for (Map.Entry<Shop, Boolean> isGroupMember : tryShopIsGroupMemberMap.entrySet()) {
                    if (!isGroupMember.getValue()) {
                        tryResultSum = tryResultSum.add(shopResultSum.get(isGroupMember.getKey()));
                    }
                }

                if (tryResultSum.compareTo(maxResultSum) > 0) {
                    maxResultSum = tryResultSum;
                    maxPartnerGroup = partnerGroup;
//                    maxPartners = resultNonReachableSumTrySet;
                }
            }
//            }
        }*/

        int maxResultSum = calculatorPartners.getMaxFromPartners(shopGetter.getId(), friend.getId(), 1000_000, false);

        if (maxResultSum > 0) {
            cashbackMessagePartnerGroup
                    .append(" всего сумма для списания от партнеров на ")
                    .append(maxResultSum)
                    .append("р.\r\n");

            Message buyerCashbackMessagePartner = new Message(null, 0,
                    Map.of(LanguageEnum.RU, "Пользователь " + friend.getName() + "  имеет кэшбеки партнеров: " + cashbackMessagePartnerGroup));
            shopLevel.addMessage(buyerCashbackMessagePartner);
            newUser = false;

            cashbackMessagePartnerGroup = new StringBuilder("В Ваших кэшбеках партнерские кэшбеки ").append(cashbackMessagePartnerGroup);
        }
        System.out.println("cashbackMessagePartner***" + cashbackMessagePartnerGroup);


        Long friendChatId = friend.getChatId();

        LevelDTOWrapper friendLevel = initialLevel.convertToLevel(initialLevel.level_P2B_RESP,
//                levelCacheRepository.findFirstByUser_ChatIdAndCallName(friendChatId, P2B_RESP.name()),
                false,
                false);

        StringBuilder friendText;
        if (newUser) {
            friendText = new StringBuilder("Вы еще не являетесь клиентом магазина " + shopName);

            Message buyerCashbackMessage = new Message(null, Map.of(LanguageEnum.RU, "Пользователь " + friend.getName() + " еще не является клиентом"));
            shopLevel.addMessage(buyerCashbackMessage);
        } else {
            friendText = new StringBuilder("В магазине " + shopName + " \r\n " + basketMessage + bookmarkMessage + cashbackMessage + cashbackMessagePartnerGroup);
        }

        LanguageEnum language = friend.getLanguage();

        System.out.println("language++++++" + language);

        if (!actionActiveSet.contains(ActionTypeEnum.BASIC)
                && !actionActiveSet.contains(ActionTypeEnum.BASIC_DEFAULT)
                && !actionActiveSet.contains(ActionTypeEnum.BASIC_MANUAL)
                && sumPartnerGroups  <= 0) {

            System.out.println();
            System.out.println("buttonDisabler Списать кэшбек");
            System.out.println();

            telegramElementsUtil.buttonDisabler(shopLevel, "Списать кэшбек вручную", language);
//            buttonDisabler(shopLevel, "Подтвердить кэшбек по корзине");
        }
        if (!actionActiveSet.contains(ActionTypeEnum.COUPON)
                && !actionActiveSet.contains(ActionTypeEnum.COUPON_DEFAULT)) {

            System.out.println();
            System.out.println("buttonDisabler Списать купон");
            System.out.println();

            telegramElementsUtil.buttonDisabler(shopLevel, "Списать купон вручную", language);
            telegramElementsUtil.buttonDisabler(shopLevel, "Списать купон по корзине", language);
        }


        System.out.println("basketList.isEmpty()++++++" + basketList.isEmpty());

        if (basketList.isEmpty()) {

            System.out.println();
            System.out.println("buttonDisabler  по корзине");
            System.out.println();

            telegramElementsUtil.buttonDisabler(shopLevel, "Начислить купон по корзине", language);
            telegramElementsUtil.buttonDisabler(shopLevel, "Списать купон по корзине", language);
            telegramElementsUtil.buttonDisabler(shopLevel, "Подтвердить кэшбек по корзине", language);
        }

        Message friendMessage = new Message(null, Map.of(LanguageEnum.RU, new String(friendText)));
        friendLevel.addMessage(friendMessage);

        System.out.println();
        System.out.println("shopChatId+++++++" + shopChatId);
        System.out.println("friendChatId+++++" + friendChatId);
        System.out.println();

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(shopUsers);
            e.setLevel(shopLevel);
        }));

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(friendChatId);
            e.setUser(friend);
            e.setLevel(friendLevel);
        }));
        return levelChatDTOList;
    }
}
