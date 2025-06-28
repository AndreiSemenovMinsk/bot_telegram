package ru.skidoz.service.command_impl.action.coupon;

import java.util.Collections;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class CouponRateWithdraw implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        //Level resultLevel = initialLevel.level_COUPON_RATE_WITHDRAW;

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_COUPON_RATE_WITHDRAW,
                true,
                false);

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        System.out.println();
        System.out.println("**********************CouponRateWithdraw**********************");
        System.out.println();
        System.out.println();
        System.out.println("inputText*******" + inputText);

        try {
            Action action = actionCacheRepository.findById(shopInitiator.getCurrentCreatingAction());
            action.setNumberCoupon(Integer.parseInt(inputText));
            actionCacheRepository.save(action);
        } catch (NumberFormatException formatException){

            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
