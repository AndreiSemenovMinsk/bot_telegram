package ru.skidoz.service.command_impl.communication.manual.coupon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.InitialLevel;
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
public class P2BChargeCoupon implements Command {
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private ActionCacheRepository actionRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("+++++++++++++++++++++P2BChargeCoupon++++++++++++++++++++");
        System.out.println();
        System.out.println();

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();

        try {
            Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);

            resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_CHARGE_COUPON,
                    true,
                    false);

            List<Action> actionCouponList = actionRepository.findAllByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.COUPON);
            Action actionCoupon = actionRepository.findFirstByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.COUPON_DEFAULT);
            actionCouponList.add(actionCoupon);

            for (Action action : actionCouponList) {
                ButtonRow row = new ButtonRow();
                Button approveButton = new Button(row, Map.of(users.getLanguage(), action.getName()), initialLevel.level_P2B_CHARGE_COUPON_REQUEST.getIdString() + action.getId());
                row.add(approveButton);
                resultLevel.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO - изменить отдачу
        LevelDTOWrapper finalResultLevel1 = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalResultLevel1);
        }));
        return levelChatDTOList;
    }
}
