package ru.skidoz.service.command_impl.search_partner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.service.InitialLevel;
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
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {

        //Level resultLevel = initialLevel.level_WITHDRAW_PARTNER;
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_WITHDRAW_PARTNER_RESP,
                false,
                false);
        Long chatId = users.getChatId();

        String callback = update.getCallbackQuery().getData();

        // приходим с кнопки выбора контрагента
        if (callback != null && level.getCallName().equals(WITHDRAW_PARTNER_GROUP.name())) {

            String code = callback.substring(19);
            PartnerGroup partnerGroup = partnerGroupRepository.findById(Integer.valueOf(code));
            Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
            shopInitiator.setCurrentConversationShopGroup(partnerGroup.getDebtor());
            shopCacheRepository.save(shopInitiator);


            List<Shop> shopSet = shopGroupCacheRepository.findById(partnerGroup.getDebtor()).getShopSet();
            for (Shop shopPartner : shopSet) {

                Partner debtor = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopPartner.getId());
                Shop shopDebtor = shopCacheRepository.findById(debtor.getDebtor());
                Message messageDebtor = new Message(null, Map.of(LanguageEnum.RU, "Вы должны " + shopDebtor.getName() + " " + debtor.getSum() + " при лимите " + debtor.getLim()));
//                messageCacheRepository.save(messageDebtor);
                resultLevel.addMessage(messageDebtor);

                Partner creditor = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopPartner.getId(), shopInitiator.getId());
                Shop shopCreditor = shopCacheRepository.findById(creditor.getCreditor());
                Message messageCreditor = new Message(null, Map.of(LanguageEnum.RU, "Вам должны " + shopCreditor.getName() + " " + creditor.getSum() + " при лимите " + creditor.getLim()));
//                messageCacheRepository.save(messageCreditor);
                resultLevel.addMessage(messageCreditor);

                ButtonRow row = new ButtonRow();
                Button button = new Button(row, Map.of(LanguageEnum.RU, "Списать"), initialLevel.level_WITHDRAW_PARTNER.getIdString() + creditor.getCreditor());
                row.add(button);
                resultLevel.addRow(row);
            }
        }

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }

/*
    private BigDecimal getSum(Shop shopInitiator, PartnerGroup partnerGroup) {
        Set<Shop> shopSet = partnerGroup.getDebtor().getShopSet();
        BigDecimal resultSum = BigDecimal.ZERO;
        for (Shop shopPartner : shopSet) {
            Partner partner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator, shopPartner);
            resultSum = resultSum.add(partner.getSum());
        }
        return resultSum;
    }*/
}
