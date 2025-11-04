package ru.skidoz.service.command_impl.communication.manual.cashback;

import java.io.IOException;

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
import ru.skidoz.util.CalculatorPartners;

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
    private CalculatorPartners calculatorPartners;
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

                final int subtracted = calculatorPartners.getMaxFromPartners(
                        shopInitial.getId(),
                        users.getId(),
                        proposedSum,
                        true);

                cashbackWriteOffCacheRepository.delete(cashbackWriteOff);

                resultLevel = initialLevel.convertToLevel(
                        initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE,
                        false,
                        false);
                Message message = new Message(
                        null,
                        Map.of(LanguageEnum.RU, "@Списано " + subtracted));
                resultLevel.addMessage(message);
            }

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
