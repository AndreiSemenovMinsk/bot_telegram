package ru.skidoz.service.command_impl.search_partner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.*;

/**
 * @author andrey.semenov
 */
@Component
public class WithdrawPartnerGroup implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_WITHDRAW_PARTNER,
                false,
                false);
        Long chatId = users.getChatId();

        String callback = update.getCallbackQuery().getData();

        // приходим с кнопки выбора контрагента
        if (callback != null && level.getCallName().equals(WITHDRAW_PARTNER_GROUP.name())) {

            String code = callback.substring(19);
            PartnerGroup partnerGroup = partnerGroupRepository.findById(Integer.valueOf(code));

            Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
            shopInitiator.setCurrentConversationShopGroup(partnerGroup.getShopGroup());
            shopCacheRepository.save(shopInitiator);

            final ShopGroup shopGroup = shopGroupCacheRepository.findById(partnerGroup.getShopGroup());
            List<Integer> shopSet = shopGroup.getShopSet();
            for (Integer shopPartnerId : shopSet) {
                Shop shopPartner = shopCacheRepository.findById(shopPartnerId);

                PartnerGroup partnerPartnerGroup = partnerGroupRepository.findById(shopPartnerId);
                final Integer sum = partnerPartnerGroup.getSum();
                if (sum < 0) {
                    Message messageCreditor = new Message(null, Map.of(LanguageEnum.RU, "Вы можете списать задолженность " + shopPartner.getName()
                            + " " + sum + " при лимите " + shopGroup.getLimit()));
                    resultLevel.addMessage(messageCreditor);

                    ButtonRow row = new ButtonRow();
                    Button button = new Button(row, Map.of(LanguageEnum.RU, "Списать"), initialLevel.level_WITHDRAW_PARTNER.getIdString() + shopPartnerId);
                    row.add(button);
                    resultLevel.addRow(row);
                }
            }
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
