package ru.skidoz.service.command_impl.communication.manual.cashback;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2BProposeCashbackResp implements Command {
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private CashbackShopGroupCacheRepository cashbackShopGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();
        Long buyerChatId = null;

        System.out.println();
        System.out.println();
        System.out.println("----------------------------------P2BProposeCashbackResp-----------------------------");
        System.out.println();

        try {
            BigDecimal proposedSum = BigDecimal.valueOf(Integer.parseInt(inputText));
            if (proposedSum.intValue() > 0) {
                Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);
//                Shop shopInitiatorEntity = new Shop(shopInitiator.getId());
                User buyer = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());
//                Users buyerEntity = new Users(buyer.getId());
                Action action = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.BASIC_MANUAL);
                buyerChatId = buyer.getChatId();

                Purchase purchase = new Purchase(e -> {
                    e.setUser(buyer.getId());
                    e.setShop(shopInitiator.getId());
                });
                purchase.setSum(proposedSum);
                purchaseCacheRepository.save(purchase);

                System.out.println("P2BProposeCashbackResp---purchase********" + purchase);

                Cashback cashback = new Cashback(e -> {
                    e.setAction(action.getId());
                    e.setUser(buyer.getId());
                    e.setShop(shopInitiator.getId());
                    e.setPurchase(purchase.getId());
                });
                System.out.println(107);
                cashbackCacheRepository.save(cashback);

                partnerGroupCacheRepository.findAllByCreditor_Id(shopInitiator.getId()).forEach(partnerGroup -> {
                    CashbackShopGroup cashbackShopGroupDefault = new CashbackShopGroup(e -> {
                        e.setShopGroup(partnerGroup.getDebtor());
                        e.setShop(shopInitiator.getId());
                        e.setUser(buyer.getId());
                        e.setPurchase(purchase.getId());
                        e.setManual(true);
                    });
                    cashbackShopGroupCacheRepository.save(cashbackShopGroupDefault);
                });


//                Users friend = recommendationRepository.findFirstByBuyerAndShop(buyer, shopInitiator).getFriend();
                User friend = userCacheRepository.findFirstByShop_IdAndBuyer_Id(shopInitiator.getId(), buyer.getId());
                if (friend == null) {
                    //friend = recommendationRepository.findFirstByBuyerAndShopIsNull(buyer).getFriend();
                    friend = userCacheRepository.findFirstByShopNullAndBuyer_Id(buyer.getId());
                }

                System.out.println("recommendationRepository +++++++++++++++++++++++++ friend***" + friend);

                if (friend != null) {
                    BigDecimal friendProposedSum = proposedSum.multiply(shopInitiator.getSarafanShare()).divide(new BigDecimal(100), 4, RoundingMode.CEILING);

//                    Users friendEntity = new Users(friend.getId());
                    User finalFriend = friend;
                    Purchase purchaseFriend = new Purchase(e -> {
                        e.setUser(finalFriend.getId());
                        e.setShop(shopInitiator.getId());
                    });
                    purchaseFriend.setSum(friendProposedSum);
                    purchaseCacheRepository.save(purchaseFriend);

                    Cashback cashbackFriend = new Cashback(e -> {
                        e.setAction(action.getId());
                        e.setUser(finalFriend.getId());
                        e.setShop(shopInitiator.getId());
                        e.setPurchase(purchaseFriend.getId());
                    });
                    System.out.println(149);
                    cashbackCacheRepository.save(cashbackFriend);

                    partnerGroupCacheRepository.findAllByCreditor_Id(shopInitiator.getId()).forEach(partnerGroup -> {
                        CashbackShopGroup cashbackShopGroupDefault = new CashbackShopGroup(e -> {
                            e.setShopGroup(partnerGroup.getDebtor());
                            e.setShop(shopInitiator.getId());
                            e.setUser(buyer.getId());
                            e.setPurchase(purchase.getId());
                            e.setManual(true);
                        });
                        cashbackShopGroupCacheRepository.save(cashbackShopGroupDefault);
                    });

                    LevelDTOWrapper friendResultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_PROPOSE_CASHBACK_RESP,
                            false,
                            false);

                    Message message = new Message(null, Map.of(LanguageEnum.RU,"Вам начислены " + friendProposedSum
                            + " р. кешбека от партнера " + buyer.getName()
                            + " от магазина " + shopInitiator.getName()));
                    friendResultLevel.addMessage(message);
                    levelChatDTOList.add(new LevelChat(e -> {
                        e.setChatId(finalFriend.getChatId());
                        e.setLevel(friendResultLevel);
                    }));
                }

                //resultLevel = initialLevel.level_P2B_PROPOSE_CASHBACK_RESP;
                resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_PROPOSE_CASHBACK_RESP,
                        false,
                        false);

                Message message = new Message(null, Map.of(LanguageEnum.RU,"Клиенту начислено " + proposedSum + " р. кешбека"));
                resultLevel.addMessage(message);

                //adder.addCashback(buyer);
            } else {
                resultLevel = initialLevel.convertToLevel(initialLevel.level_NEGATIVE_SUM,
                        true,
                        false);
            }

        } catch (NumberFormatException formatException) {

            resultLevel = initialLevel.convertToLevel(level,
                    true,
                    false);
            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Long finalBuyerChatId = buyerChatId;

        LevelDTOWrapper finalResultLevel = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(finalBuyerChatId);
            e.setLevel(finalResultLevel);
        }));
        LevelDTOWrapper finalResultLevel1 = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalResultLevel1);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
