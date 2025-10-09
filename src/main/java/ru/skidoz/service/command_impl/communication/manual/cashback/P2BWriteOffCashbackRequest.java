package ru.skidoz.service.command_impl.communication.manual.cashback;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
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

import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_CASHBACK_APPROVE;

/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2BWriteOffCashbackRequest implements Command {
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
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

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();
        Long buyerChatId = null;

        System.out.println();
        System.out.println();
        System.out.println("----------------------------------P2BWriteOffCashbackRequest-----------------------------");
        System.out.println();
        System.out.println(level.getCallName());
        System.out.println(level.getCallName().equals(P2B_WRITEOFF_CASHBACK_APPROVE.name()));

// сюда похоже вообще не доходит - потому что команда идет на P2BWriteOffCashbackApprove
        if (level.getCallName().equals(P2B_WRITEOFF_CASHBACK_APPROVE.name())) {

            String code = update.getCallbackQuery().getData().substring(19);

            System.out.println("P2B code+++" + code);
            System.out.println("code---------------------" + code);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NOT HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println();
            System.out.println();
            System.out.println();

            Integer substructed = null;

            try {
                CashbackWriteOff cashbackWriteOff = cashbackWriteOffCacheRepository.findById(Integer.valueOf(code));
                Integer proposedSum = cashbackWriteOff.getSum();
                buyerChatId = userCacheRepository.findById(cashbackWriteOff.getUser()).getChatId();

                System.out.println("buyerChatId" + buyerChatId);
                System.out.println("shopChatId" + shopChatId);
                System.out.println("proposedSum***" + proposedSum);

//TODO - тут что то не то proposedSum = cashbackWriteOff.getSum() - может  BigDecimal.valueByCode(Integer.parseInt(inputText)) ??
                if (cashbackWriteOff.getSum() > proposedSum) {
                    cashbackWriteOff.setSum(cashbackWriteOff.getSum() - proposedSum);
                    substructed = proposedSum;
                    cashbackWriteOffCacheRepository.delete(cashbackWriteOff);
                } else {
                    substructed = cashbackWriteOff.getSum();
                    cashbackWriteOff.setSum(0);
                    cashbackWriteOff.setSum(cashbackWriteOff.getSum() - substructed);
                    cashbackWriteOffCacheRepository.save(cashbackWriteOff);
                }

                resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE,
                        false,
                        false);
                Message message = new Message(null, Map.of(LanguageEnum.RU,"не должно показывать!!! #Списано " + substructed));
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

        } else {

            String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");
            LevelDTOWrapper resultBuyerLevel = null;

            System.out.println("P2B inputText*" + inputText);

            try {
                Integer proposedSum = Integer.parseInt(inputText);

                System.out.println("proposedSum.intValue()***" + proposedSum.intValue());

                if (proposedSum.intValue() > 0) {

                    Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);

                    Action action = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.BASIC_MANUAL);

                    User buyer = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());
                    buyerChatId = buyer.getChatId();

                    CashbackWriteOff cashbackWriteOff = new CashbackWriteOff(e -> {
                        e.setApproved(false);
                        e.setUser(buyer.getId());
                        e.setShop(shopInitiator.getId());
                        e.setSum(proposedSum);
//                        e.setAction(action.getId());
                    });

                    System.out.println("cashbackWriteOff***" + cashbackWriteOff);

                    cashbackWriteOffCacheRepository.save(cashbackWriteOff);

                    //resultBuyerLevel = initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST;
                    resultBuyerLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST,
                            false,
                            false);

                    System.out.println("Магазин " + shopInitiator.getName() + " предлагает списать " + proposedSum);

                    Message message = new Message(null, Map.of(LanguageEnum.RU,"Магазин " + shopInitiator.getName() + " предлагает списать " + proposedSum));
                    resultBuyerLevel.addMessage(message);
//                    Button approveButton = resultBuyerLevel.getButtonRows().get(0).getButtonList().get(0);

                    ButtonRow row = new ButtonRow( );
                    Button approveButton = new Button( row, Map.of(LanguageEnum.RU, "Списать@"), initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE.getIdString() + cashbackWriteOff.getId());
                    row.add(approveButton);

                    Button button22_0_0_1 = new Button( row, Map.of(LanguageEnum.RU, "Отклонить"), initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS.getIdString());
                    /*****/row.add(button22_0_0_1);

                    resultBuyerLevel.addRow(row);
//                    approveButton.setCallback();

                    System.out.println(P2B_WRITEOFF_CASHBACK_APPROVE.name() + cashbackWriteOff.getId());

                } else {
                    resultBuyerLevel = initialLevel.convertToLevel(initialLevel.level_NEGATIVE_SUM,
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

            System.out.println("resultBuyerLevel++++++++" + resultBuyerLevel);

            LevelDTOWrapper finalBuyerLevel = resultBuyerLevel;
            LevelDTOWrapper finalShopLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_CASHBACK_RESP,
                    true,
                    false);
            Long finalBuyerChatId = buyerChatId;

            levelChatDTOList.add(new LevelChat(e -> {
                e.setChatId(finalBuyerChatId);
                e.setLevel(finalBuyerLevel);
            }));
            levelChatDTOList.add(new LevelChat(e -> {
                e.setChatId(shopChatId);
                e.setUser(users);
                e.setLevel(finalShopLevel);
            }));
        }
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
