package ru.skidoz.service.command_impl.search_partner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER;

/**
 * @author andrey.semenov
 */
@Component
public class WithdrawPartner2_1 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        LevelDTOWrapper resultLevel = null;
        Long chatId = users.getChatId();

        // приходим с кнопки выбора контрагента
        if (level.getCallName().equals(WITHDRAW_PARTNER.name())) {

            String code = update.getCallbackQuery().getData().substring(19);

            System.out.println("code*****" + code);

            Shop shopPartner = shopCacheRepository.findById(Integer.valueOf(code));

            System.out.println("shopPartner+++" + shopPartner);

            Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
            shopInitiator.setCurrentConversationShop(shopPartner.getId());
            shopCacheRepository.save(shopInitiator);
            //это уже переход на следующий уровень, поэтому текст следующего уровня
            //resultLevel = initialLevel.level_WITHDRAW_PARTNER_RESP.clone();
            resultLevel = initialLevel.convertToLevel(initialLevel.level_WITHDRAW_PARTNER_RESP,
                    true,
                    false);

            resultLevel.setMessages(new ArrayList<>());

            Message message = new Message(null, Map.of(LanguageEnum.RU,"Введите размер списания кэшбека для " + shopPartner.getName()));
            resultLevel.addMessage(message);
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}
