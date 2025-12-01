package ru.skidoz.service.command_impl.communication.manual.coupon;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
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
public class P2BChargeCouponResp implements Command {
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("+++++++++++++++++++++P2BChargeCouponResp++++++++++++++++++++");
        System.out.println();
        System.out.println();

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();
        Long buyerChatId = null;

        System.out.println("inputText***" + inputText);

        try {
            int proposedNumber = Integer.parseInt(inputText);
            if (proposedNumber > 0) {

                Shop shopInitiator = shopCacheRepository.findById(userCacheRepository.findByChatId(shopChatId).getSellerShop());
                User buyer = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());

                System.out.println(shopInitiator);

//                Action action = actionRepository.findById(shopInitiator.getCurrentChargeAction());
//                Action action = actionRepository.findFirstByShopAndTypeAndActive(shopInitiator, ActionTypeEnum.COUPON_DEFAULT);
                buyerChatId = buyer.getChatId();

                Purchase purchase = new Purchase(e -> {
                    e.setUser(buyer.getId());
                    e.setNumberCoupon(proposedNumber);
                    e.setSum(0);
                    e.setShop(shopInitiator.getId());
                });
                purchaseCacheRepository.save(purchase);

                Cashback cashback = /*cashbackRepository.findFirstByActionAndBuyer(action, buyer);
                if (cashback != null) {
                    cashback =*/ new Cashback(e -> {
                    e.setAction(shopInitiator.getCurrentChargeAction());
                    e.setUser(buyer.getId());
                    e.setShop(shopInitiator.getId());
                    e.setPurchase(purchase.getId());
                    // e.setNumberCoupon(proposedSum);
                });
                /*}*/
                System.out.println(100);
                cashbackCacheRepository.save(cashback);
//                purchase.addCashback(cashback);

/*
                //TODO - добавить уровень, чтобы друг получал сообщение
                Users friend = recommendationRepository.findFirstByShopAndBuyer(shopInitiator, buyer);
                if (friend != null) {
                    Cashback cashbackFriend = new Cashback(e -> {
                        e.setAction(action);
                        e.setUser(friend);
                        e.setShop(shopInitiator);
                    });
                    cashbackRepository.save(cashbackFriend);

                    Purchase purchaseFriend = new Purchase(e -> {
                        e.setUser(friend);
                        e.addCashback(cashbackFriend);
                    });
                    purchaseFriend.setSum(BigDecimal.valueByCode(proposedNumber).multiply(shopInitiator.getSarafanShare()));
                    purchaseRepository.save(purchaseFriend);
                }
*/
                System.out.println("123@@@");

                resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_CHARGE_COUPON_RESP,
                        false,
                        false);

                Message message = new Message(null, Map.of(LanguageEnum.RU,"Клиенту начислено " + proposedNumber));
                resultLevel.addMessage(message);

                //adder.addCashback(buyer);
            } else {
                //resultLevel = initialLevel.level_NEGATIVE_COUNT;
                System.out.println("-123@@@");

                resultLevel = initialLevel.convertToLevel(initialLevel.level_NEGATIVE_COUNT,
                        true,
                        false);
            }

        } catch (NumberFormatException formatException) {

            System.out.println("@@@123@@@");

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

        //TODO - изменить отдачу
        LevelDTOWrapper finalResultLevel1 = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalResultLevel1);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
