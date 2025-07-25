package ru.skidoz.service.command_impl.communication.manual.coupon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2BWriteOffCouponRequest implements Command {

    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
    @Autowired
    private ActionCacheRepository actionRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("+++++++++++++++++++++P2BWriteOffCouponRequest++++++++++++++++++++");
        System.out.println();
        System.out.println();


        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();

        try {
            String code = update.getCallbackQuery().getData().substring(19);

            Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);

            System.out.println("code***" + code);

            Action action = actionRepository.findById(Integer.valueOf(code));

            shopInitiator.setCurrentChargeAction(Integer.valueOf(code));
            shopCacheRepository.save(shopInitiator);

            resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_COUPON_RESP,
                    false,
                    false);

            Message message = new Message(null, Map.of(LanguageEnum.RU,"Введите количество списываемых купонов по акции " + action.getName()));
            resultLevel.addMessage(message);

        }  catch (Exception e) {
            e.printStackTrace();
        }

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
