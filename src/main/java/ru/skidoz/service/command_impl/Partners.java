package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ru.skidoz.aop.repo.PartnerCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class Partners implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException, CloneNotSupportedException {

        //Level resultLevel = initialLevel.level_PARTNERS.clone();
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_PARTNERS,
                true,
                true);

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        System.out.println("chatId+++" + chatId);
        System.out.println("shopInitiator+++" + shopInitiator);

        List<Partner> debtorList = partnerCacheRepository.findAllByCreditor_Id(shopInitiator.getId());
        for (Partner debtor : debtorList) {

            Shop debtorShop = shopCacheRepository.findById(debtor.getDebtor());

            Message message = new Message(null, Map.of(LanguageEnum.RU,
                    "Вам должен " + debtorShop.getName() + " " + debtor.getSum() + " при лимите " + debtor.getLim() + " и ставке " + debtor.getRate() + "%"));
            //messageRepository.save(message);
            resultLevel.addMessage(message);

            ButtonRow row = new ButtonRow();
            //здесь берется shop id, в отличии от Partner - там берется id для PartnerGroup
            Button button = new Button(row, Map.of(LanguageEnum.RU,
                    "Списать c " + debtorShop.getName() + " (max " + debtor.getSum() + ")"), initialLevel.level_WITHDRAW_PARTNER.getIdString() + debtor.getDebtor());
            row.add(button);
            resultLevel.addRow(row);
        }

        List<Partner> creditorList = partnerCacheRepository.findAllByDebtor_Id(shopInitiator.getId());
        for (Partner creditor : creditorList) {

            Shop creditorShop = shopCacheRepository.findById(creditor.getDebtor());

            Message message = new Message(null, Map.of(LanguageEnum.RU, "Вы должны " + creditorShop.getName() + " " + creditor.getSum() + " при лимите " + creditor.getLim()));
            //messageRepository.save(message);
            resultLevel.addMessage(message);
        }

        List<PartnerGroup> debtorGroupList = partnerGroupCacheRepository.findAllByCreditor_Id(shopInitiator.getId());
        for (PartnerGroup debtorGroup : debtorGroupList) {

            ButtonRow row = new ButtonRow();
            Set<Shop> shopSet = shopGroupCacheRepository.findById(debtorGroup.getDebtor()).getShopSet();
            BigDecimal sum = getSum(shopInitiator.getId(), shopSet);

            Message message = new Message(null, 0,
                    Map.of(LanguageEnum.RU, "Вам должны "
                            + shopSet.stream().map(Shop::getName).limit(5).collect(Collectors.joining(", "))
                            + " " + sum + " при лимите " + debtorGroup.getLim()));
            //messageRepository.save(message);
            resultLevel.addMessage(message);

            //здесь берется id для PartnerGroup, в отличии от Partner - там берется shop id
            Button button = new Button(row, Map.of(LanguageEnum.RU, "Списать"), initialLevel.level_WITHDRAW_PARTNER_GROUP.getIdString() + debtorGroup.getId());
            row.add(button);
            resultLevel.addRow(row);
        }

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }


    private BigDecimal getSum(Integer/*Shop*/ shopInitiator, Set<Shop> shopSet) {
        BigDecimal resultSum = BigDecimal.ZERO;
        for (Shop shopPartner : shopSet) {
            Partner partner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator, shopPartner.getId());
            resultSum = resultSum.add(partner.getSum());
        }
        return resultSum;
    }
}
