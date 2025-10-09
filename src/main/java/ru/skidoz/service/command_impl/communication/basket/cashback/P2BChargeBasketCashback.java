package ru.skidoz.service.command_impl.communication.basket.cashback;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.*;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Optimizator;
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
public class P2BChargeBasketCashback implements Command {
    @Autowired
    private BasketProductCacheRepository basketProductCacheRepository;
    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
//    @Autowired
//    private CashbackWriteOffResultPurchaseCacheRepository cashbackWriteOffResultPurchaseCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private Optimizator optimizator;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("+++++++++++++++++++++++++++++++++++++P2BChargeBasketCashback++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();
        Long buyerChatId = null;

        try {
            Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);

            System.out.println();
            System.out.println();
            System.out.println("shopInitiator+++" + shopInitiator);
            System.out.println(shopInitiator.getCurrentConversationShopUserChatId());
            System.out.println();
            System.out.println();

            User buyer = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());

            List<Action> suitableActions = new ArrayList<>();
            List<Product> productDTOS = new ArrayList<>();
            Map<Integer, List<BigDecimal>> actionProductSumByProducts = new HashMap<>();
            Map<Integer, BigDecimal> actionProductSumVariant = new HashMap<>();
            BigDecimal max = BigDecimal.ZERO;

            Action bestAction = optimizator.getOptimal(
                    buyer.getId(),
                    shopInitiator.getId(),
                    productDTOS,
                    actionProductSumVariant,
                    suitableActions,
                    actionProductSumByProducts,
                    max);

            CashbackWriteOff cashbackWriteOff = new CashbackWriteOff(e -> {
                e.setApproved(false);
                e.setUser(buyer.getId());
                e.setShop(shopInitiator.getId());
                e.setSum(max);
                e.setBestAction(bestAction.getId());
//                e.setPreviousPurchaseList(previousPurchaseList);
            });
            cashbackWriteOffCacheRepository.save(cashbackWriteOff);

/*

            final List<BigDecimal> sumByQuantityByProductsList = actionProductSumByProducts.get(bestAction.getId());

            for (var sumByQuantity : sumByQuantityByProductsList) {
                System.out.println("@@@@@cashbackWriteOffResultPurchase***bigDecimal+++" + sumByQuantity);

                CashbackWriteOffResultPurchase cashbackWriteOffResultPurchase = new CashbackWriteOffResultPurchase(e -> {
                    e.setCashbackWriteOff(cashbackWriteOff.getId());
                    e.setSum(sumByQuantity);
                    e.setPreviousPurchase();
                });
                cashbackWriteOffResultPurchaseCacheRepository.save(cashbackWriteOffResultPurchase);

                System.out.println("@@@@@cashbackWriteOffResultPurchase***" + cashbackWriteOffResultPurchase);
            }
*/

            buyerChatId = buyer.getChatId();
            //TODO - применить это
            //resultLevel = initialLevel.level_NEGATIVE_SUM;
            //resultLevel = initialLevel.level_P2B_CHARGE_BASKET_CASHBACK;

            resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_CHARGE_BASKET_CASHBACK,
                    false,
                    false);
            Message message = new Message(null, 0,
                    Map.of(LanguageEnum.RU,"Сумма самого выгодного списания кэшбека по корзине " + max + "р. "));
            resultLevel.addMessage(message);

            ButtonRow row = new ButtonRow();
            Button approveButton = new Button(row, Map.of(LanguageEnum.RU, "Подтвердить начисление"), initialLevel.level_P2B_APPROVE_BASKET_CASHBACK.getIdString() + cashbackWriteOff.getId());
            row.add(approveButton);

            Button button22_2_0_1 = new Button(row, Map.of(LanguageEnum.RU, "Начислить вручную"), initialLevel.level_P2B_PROPOSE_CASHBACK.getIdString());
            /*****/row.add(button22_2_0_1);
            resultLevel.addRow(row);

            System.out.println("approveButton" + approveButton);
            System.out.println(approveButton.getId() + "*approveButton.setCallback*" + initialLevel.level_P2B_APPROVE_BASKET_CASHBACK.getIdString() + cashbackWriteOff.getId());

        } catch (Exception e) {

            e.printStackTrace();
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
