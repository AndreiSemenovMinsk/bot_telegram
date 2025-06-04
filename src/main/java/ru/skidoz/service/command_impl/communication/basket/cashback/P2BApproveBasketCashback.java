package ru.skidoz.service.command_impl.communication.basket.cashback;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.*;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Optimizator;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.P2B_APPROVE_BASKET_CASHBACK;

/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2BApproveBasketCashback implements Command {
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private BasketProductCacheRepository basketProductCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private CashbackShopGroupCacheRepository cashbackShopGroupCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
    @Autowired
    private RecommendationCacheRepository recommendationCacheRepository;
//    @Autowired
//    private CashbackWriteOffResultPurchaseCacheRepository cashbackWriteOffResultPurchaseCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private Optimizator optimizator;

    //@Transactional
    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();
        Long buyerChatId = null;

        System.out.println();
        System.out.println();
        System.out.println("----------------------------------P2BApproveBasketCashback-----------------------------");
        System.out.println();
        System.out.println(level.getCallName());
        System.out.println(level.getCallName().equals(P2B_APPROVE_BASKET_CASHBACK.name()));

        try {
            Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);
            User usersCurrentConversation = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());

            List<Action> suitableActions = new ArrayList<>();
            //мы разбиваем все товары по акциям

            List<Product> productDTOS = new ArrayList<>();
            Map<Integer, List<BigDecimal>> actionProductSumByProducts = new HashMap<>();
            Map<Integer, BigDecimal> actionProductSumVariant = new HashMap<>();
            BigDecimal max = BigDecimal.ZERO;


            Action bestAction = optimizator.getOptimal(
                    usersCurrentConversation.getId(),
                    shopInitiator.getId(),
                    productDTOS,
                    actionProductSumVariant,
                    suitableActions,
                    actionProductSumByProducts,
                    max);


            List<BigDecimal> sums = actionProductSumByProducts.get(bestAction.getId());
            BigDecimal sum = actionProductSumVariant.get(bestAction.getId());
            User finalFriend = null;
            BigDecimal accumulatedFriendSum = BigDecimal.ZERO.stripTrailingZeros();

            for (int i = 0; i < productDTOS.size(); i++) {
                System.out.println();
                System.out.println("bestAction " + bestAction);
                System.out.println();

                int finalI = i;
                Purchase purchase = new Purchase(e -> {
                    e.setShop(shopInitiator.getId());
                    e.setUser(usersCurrentConversation.getId());
                    e.setSum(sums.get(finalI));
                });
                purchaseCacheRepository.save(purchase);

                Cashback cashbackDefault = new Cashback(e -> {
                    e.setAction(bestAction.getId());
                    e.setShop(shopInitiator.getId());
                    e.setUser(usersCurrentConversation.getId());
                    e.setPurchase(purchase.getId());
                });

                cashbackCacheRepository.save(cashbackDefault);

                partnerGroupCacheRepository.findAllByCreditor_Id(shopInitiator.getId()).forEach(partnerGroup -> {
                    CashbackShopGroup cashbackShopGroupDefault = new CashbackShopGroup(e -> {
                        e.setShopGroup(partnerGroup.getDebtor());
                        e.setShop(shopInitiator.getId());
                        e.setUser(usersCurrentConversation.getId());
                        e.setPurchase(purchase.getId());
                        e.setManual(false);
                    });
                    cashbackShopGroupCacheRepository.save(cashbackShopGroupDefault);
                });

                User friendA = userCacheRepository.findFirstByShop_IdAndBuyer_Id(shopInitiator.getId(), usersCurrentConversation.getId());
                if (friendA == null) {
                    friendA = userCacheRepository.findFirstByShopNullAndBuyer_Id(usersCurrentConversation.getId());
                }

                final User friend = friendA;

                if (friend != null) {
                    BigDecimal friendSum = sum.multiply(shopInitiator.getSarafanShare()).divide(new BigDecimal(100), 4, RoundingMode.CEILING);

                    accumulatedFriendSum = accumulatedFriendSum.add(friendSum);

                    finalFriend = friend;
//                    Users friendEntity = new Users(friend.getId());
                    final Purchase purchaseFriend = new Purchase(e -> {
                        e.setShop(shopInitiator.getId());
                        e.setUser(friend.getId());
                        e.setSum(friendSum);
                    });
                    purchaseCacheRepository.save(purchaseFriend);

                    Cashback cashbackFriend = new Cashback(e -> {
                        e.setAction(bestAction.getId());
                        e.setUser(friend.getId());
                        e.setShop(shopInitiator.getId());
                        e.setPurchase(purchaseFriend.getId());
                    });

                    cashbackCacheRepository.save(cashbackFriend);

                    partnerGroupCacheRepository.findAllByCreditor_Id(shopInitiator.getId()).forEach(partnerGroup -> {
                        CashbackShopGroup cashbackShopGroupDefault = new CashbackShopGroup(e -> {
                            e.setShopGroup(partnerGroup.getDebtor());
                            e.setShop(shopInitiator.getId());
                            e.setUser(usersCurrentConversation.getId());
                            e.setPurchase(purchase.getId());
                            e.setManual(false);
                        });
                        cashbackShopGroupCacheRepository.save(cashbackShopGroupDefault);
                    });
                }

                for (Action action : suitableActions) {
                    Cashback cashback = new Cashback(e -> {
                        e.setAction(action.getId());
                        e.setShop(shopInitiator.getId());
                        e.setUser(usersCurrentConversation.getId());
                        e.setPurchase(purchase.getId());
                    });
                    cashbackCacheRepository.save(cashback);
                }
                System.out.println("friend+++" + friend);
                //TODO - добавить уровень, чтобы друг получал сообщение
            }


            if (finalFriend != null) {
                LevelDTOWrapper friendResultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_PROPOSE_CASHBACK_RESP,
                        false,
                        false);

                Message friendMessage = new Message(null, Map.of(LanguageEnum.RU, "Вам начислены " + accumulatedFriendSum
                        + " р. кешбека от #партнера " + usersCurrentConversation.getName()
                        + " от магазина " + shopInitiator.getName()));
                friendResultLevel.addMessage(friendMessage);

                final User friend = finalFriend;

                levelChatDTOList.add(new LevelChat(e -> {
                    e.setChatId(friend.getChatId());
                    e.setLevel(friendResultLevel);
                }));
            }


            //adder.addCashback(usersCurrentConversation);


            resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_PROPOSE_CASHBACK_RESP,
                    false,
                    false);

            if (level.getCallName().equals(P2B_APPROVE_BASKET_CASHBACK.name())) {

                String code = update.getCallbackQuery().getData().substring(19);

                CashbackWriteOff cashbackWriteOff = cashbackWriteOffCacheRepository.findById(Integer.valueOf(code));

                BigDecimal sum1 = cashbackWriteOff.getSum();

                final List<Cashback> cashbacks = cashbackCacheRepository
                        .findAllByUser_IdAndAction_Id(usersCurrentConversation.getId(), cashbackWriteOff.getBestAction());


                for (Cashback cashback : cashbacks) {

                    final Purchase purchaseByAction = purchaseCacheRepository.findById(cashback.getPurchase());

                    if (sum1.compareTo(purchaseByAction.getSum()) > 0) {

                        purchaseCacheRepository.delete(purchaseByAction);
                        sum1 = sum1.subtract(purchaseByAction.getSum());
                    } else {
                        purchaseByAction.setSum(purchaseByAction.getSum().subtract(sum1));
                        break;
                    }
                    purchaseCacheRepository.save(purchaseByAction);

                }

                /*List<CashbackWriteOffResultPurchase> resultPurchaseWriteOffs =
                        cashbackWriteOffResultPurchaseCacheRepository.findAllByCashbackWriteOff_Id(cashbackWriteOff.getId());

                for (CashbackWriteOffResultPurchase resultPurchaseWriteOff : resultPurchaseWriteOffs) {

                    Purchase previousPurchase = purchaseCacheRepository.findById(resultPurchaseWriteOff.getPreviousPurchase());

                    previousPurchase.decreaseSum(resultPurchaseWr'\;,iteOff.getSum());

                    if (previousPurchase.getSum().equals(BigDecimal.ZERO)) {
                        purchaseCacheRepository.delete(previousPurchase);
                    } else {
                        purchaseCacheRepository.save(previousPurchase);
                    }
                }*/

                Message message = new Message(null, Map.of(LanguageEnum.RU,
                        "Списан кэшбек по корзине " + cashbackWriteOff.getSum() + ", сумма нового базового начисления кэшбека " + max + "p."));
                resultLevel.addMessage(message);
            }

            buyerChatId = usersCurrentConversation.getChatId();
            //resultLevel = levelRepository.findFirstByChatAndCallName(TelegramProcessor.CHAT, NEGATIVE_SUM.name());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Long finalBuyerChatId = buyerChatId;

        LevelDTOWrapper finalResultLevel = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(finalBuyerChatId);
            e.setLevel(finalResultLevel);
        }));

        //TODO - заменить finalResultLevel
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        }));
        return levelChatDTOList;
    }

    /* пока не будем дельать так чтобы в акцию попадали те товары, которые куплены до начала акции
    private void getOldPurchaseToCashbackForNewAction(Integer userId, Integer shopId) {

        final List<Purchase> purchaseList = purchaseCacheRepository.findAllByShop_IdAndBuyer_Id(userId, shopId);
        purchaseList.forEach(purchase -> {

            purchase.get

        });
    }*/
}
