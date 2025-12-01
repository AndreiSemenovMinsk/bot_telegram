package ru.skidoz.service.command_impl.communication.manual.coupon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
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
public class P2BWriteOffCouponResp implements Command {
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private RecommendationCacheRepository recommendationCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User user) throws IOException, WriterException {

        System.out.println();
        System.out.println("+++++++++++++++++++++P2BWriteOffCouponResp++++++++++++++++++++");
        System.out.println();
        System.out.println();


        List<LevelChat> levelChatDTOList = new ArrayList<>();
        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = user.getChatId();
        Long buyerChatId = null;

        System.out.println("inputText***" + inputText);

        try {
            int proposedNumber = Integer.parseInt(inputText);
            if (proposedNumber > 0) {
                Shop shopInitiator = shopCacheRepository.findById(userCacheRepository.findByChatId(shopChatId).getSellerShop());
                User buyer = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());
//                ActionDTO action = actionRepository.findById(shopInitiator.getCurrentChargeAction());
                buyerChatId = buyer.getChatId();

                CashbackWriteOff cashbackWriteOff = new CashbackWriteOff(e -> {
                    e.setApproved(false);
                    e.setUser(buyer.getId());
                    e.setShop(shopInitiator.getId());
                    e.setNumberCoupon(proposedNumber);
//                    e.setAction(shopInitiator.getCurrentChargeAction());
                });
                cashbackWriteOffCacheRepository.save(cashbackWriteOff);


                resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_COUPON_RESP,
                        false,
                        false);

                Message message = new Message(null, Map.of(LanguageEnum.RU,"Магазин " + shopInitiator.getName() + " предлагает списать " + proposedNumber));
                resultLevel.addMessage(message);

                ButtonRow row = new ButtonRow();
                Button approveButton = new Button(row, Map.of(LanguageEnum.RU, "Подтвердить списание " + proposedNumber + " купонов"), initialLevel.level_P2B_WRITEOFF_COUPON_APPROVE.getIdString() + cashbackWriteOff.getId());
                row.add(approveButton);
                resultLevel.addRow(row);
            } else {
                resultLevel = initialLevel.convertToLevel(initialLevel.level_NEGATIVE_COUNT,
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
/*
        //TODO - изменить отдачу
        LevelDTOWrapper finalResultLevel1 = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalResultLevel1);
        }));*/
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
