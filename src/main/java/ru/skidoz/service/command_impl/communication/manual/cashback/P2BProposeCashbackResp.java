package ru.skidoz.service.command_impl.communication.manual.cashback;

import java.io.IOException;

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
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private PurchaseShopGroupCacheRepository purchaseShopGroupCacheRepository;
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
            Integer proposedSum = Integer.parseInt(inputText);
            if (proposedSum > 0) {
                Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);
//                Shop shopInitiatorEntity = new Shop(shopInitiator.getId());
                User buyer = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());
//                Users buyerEntity = new Users(buyer.getId());
                Action action = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.BASIC_MANUAL);
                buyerChatId = buyer.getChatId();

                User friend = userCacheRepository.findFirstByShop_IdAndBuyer_Id(shopInitiator.getId(), buyer.getId());
                if (friend == null) {
                    friend = userCacheRepository.findFirstByShopNullAndBuyer_Id(buyer.getId());
                }
                System.out.println("recommendationRepository +++++++++++++++++++++++++ friend***" + friend);

                Purchase purchase = new Purchase(e -> {
                    e.setUser(buyer.getId());
                    e.setShop(shopInitiator.getId());
                    e.setSum(proposedSum);
                });
                purchaseCacheRepository.save(purchase);


                Integer friendProposedSum;
                User finalFriend;
                Purchase purchaseFriend;
                if (friend != null) {
                    friendProposedSum = proposedSum * shopInitiator.getSarafanShare() / 100;

                    finalFriend = friend;
                    purchaseFriend = new Purchase(e -> {
                        e.setUser(finalFriend.getId());
                        e.setShop(shopInitiator.getId());
                    });
                    purchaseFriend.setSum(friendProposedSum);
                    purchaseCacheRepository.save(purchaseFriend);
                } else {
                    purchaseFriend = null;
                    finalFriend = null;
                }

                System.out.println("P2BProposeCashbackResp---purchase********" + purchase);

                Cashback cashback = new Cashback(e -> {
                    e.setAction(action.getId());
                    e.setUser(buyer.getId());
                    e.setShop(shopInitiator.getId());
                    e.setPurchase(purchase.getId());
                });

                cashbackCacheRepository.save(cashback);


                if (friend != null) {
                    Cashback cashbackFriend = new Cashback(e -> {
                        e.setAction(action.getId());
                        e.setUser(finalFriend.getId());
                        e.setShop(shopInitiator.getId());
                        e.setPurchase(purchaseFriend.getId());
                    });
                    System.out.println(149);
                    cashbackCacheRepository.save(cashbackFriend);
                }


                for (var partnerGroup : partnerGroupCacheRepository.findAllByShop_Id(shopInitiator.getId())) {

                    final ShopGroup shopGroup = shopGroupCacheRepository.findById(partnerGroup.getShopGroup());
                    final int freeLimit = shopGroup.getLimit() - partnerGroup.getSum();

                    int sum;
                    if (proposedSum > freeLimit) {
                        sum = freeLimit;
                    } else {
                        sum = proposedSum;
                    }

                    int clientSum;
                    if (finalFriend != null) {
                        clientSum = sum * (100 - shopInitiator.getSarafanShare()) / 100;
                    } else {
                        clientSum = sum;
                    }

                    PurchaseShopGroup purchaseShopGroupDefault = new PurchaseShopGroup(e -> {
                        e.setShopGroup(partnerGroup.getShopGroup());
                        e.setShop(shopInitiator.getId());
                        e.setUser(buyer.getId());
                        e.setPurchase(purchase.getId());
                        e.setSum(sum);
                        e.setManual(true);
                    });
                    purchaseShopGroupCacheRepository.save(purchaseShopGroupDefault);


                    if (finalFriend != null) {

                        friendProposedSum = sum - clientSum;

                        Integer finalFriendProposedSum = friendProposedSum;
                        PurchaseShopGroup purchaseShopGroupDefaultFried = new PurchaseShopGroup(e -> {
                            e.setShopGroup(partnerGroup.getShopGroup());
                            e.setShop(shopInitiator.getId());
                            e.setUser(buyer.getId());
                            e.setPurchase(purchase.getId());
                            e.setSum(finalFriendProposedSum);
                            e.setManual(true);
                        });
                        purchaseShopGroupCacheRepository.save(purchaseShopGroupDefaultFried);


                        LevelDTOWrapper friendResultLevel = initialLevel.convertToLevel(
                                initialLevel.level_P2B_PROPOSE_CASHBACK_RESP,
                                false,
                                false);

                        Message message = new Message(
                                null, Map.of(
                                LanguageEnum.RU, "Вам начислены " + friendProposedSum
                                        + " р. кешбека от партнера " + buyer.getName()
                                        + " от магазина " + shopInitiator.getName()));
                        friendResultLevel.addMessage(message);
                        levelChatDTOList.add(new LevelChat(e -> {
                            e.setChatId(finalFriend.getChatId());
                            e.setLevel(friendResultLevel);
                        }));
                    }
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
