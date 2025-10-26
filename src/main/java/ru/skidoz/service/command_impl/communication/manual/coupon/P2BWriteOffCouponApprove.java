package ru.skidoz.service.command_impl.communication.manual.coupon;

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


/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2BWriteOffCouponApprove implements Command {
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;

    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("+++++++++++++++++++++P2BWriteOffCouponApprove++++++++++++++++++++");
        System.out.println();
        System.out.println();

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;

        Long buyerChatId = users.getChatId();

        Long shopChatId = users.getCurrentConversationShop();
        Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);
        try {
            String code = update.getCallbackQuery().getData().substring(19);

            System.out.println("code***" + code);

            CashbackWriteOff cashbackWriteOff = cashbackWriteOffRepository.findById(Integer.valueOf(code));

            int number = cashbackWriteOff.getNumberCoupon();

            buyerChatId = users.getChatId();

            Purchase purchase = new Purchase(e -> {
                e.setUser(users.getId());
                e.setSum(0);
                e.setShop(shopInitiator.getId());
                e.setNumberCoupon(-number);
            });
            purchaseCacheRepository.save(purchase);

            Action actionDefault = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.COUPON_DEFAULT);

            Cashback cashback = new Cashback(e -> {
                e.setAction(actionDefault.getId());
                e.setUser(users.getId());
                e.setShop(shopInitiator.getId());
                e.setPurchase(purchase.getId());
            });
            System.out.println(101);
            cashbackCacheRepository.save(cashback);

//            cashbackWriteOffRepository.delete(cashbackWriteOff);

            resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST,
                    false,
                    false);

            Message message = new Message(null, Map.of(LanguageEnum.RU,"Списаны " + number + " купонов"));
            resultLevel.addMessage(message);

            //adder.addCashback(users);

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
            e.setUser(userCacheRepository.findById(shopInitiator.getAdminUser()));
            e.setLevel(finalResultLevel1);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
