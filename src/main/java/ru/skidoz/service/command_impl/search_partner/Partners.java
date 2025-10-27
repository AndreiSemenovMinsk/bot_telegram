package ru.skidoz.service.command_impl.search_partner;

import static ru.skidoz.util.TelegramElementsUtil.qrInputStream;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.LinkStarterKeeperCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;

import org.apache.commons.io.IOUtils;
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
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private LinkStarterKeeperCacheRepository linkStarterKeeperCacheRepository;
    @Autowired
    private InitialLevel initialLevel;


    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException, CloneNotSupportedException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_PARTNERS,
                true,
                true);

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        System.out.println("chatId+++" + chatId);
        System.out.println("shopInitiator+++" + shopInitiator);

        List<PartnerGroup> partnerGroupList = partnerGroupCacheRepository.findAllByShop_Id(shopInitiator.getId());
        for (PartnerGroup partnerGroup : partnerGroupList) {

            ShopGroup shopGroup = shopGroupCacheRepository.findById(partnerGroup.getId());

            if (partnerGroup.getSum() > 0) {

                LinkStarterKeeper linkStarterKeeper = new LinkStarterKeeper();
                linkStarterKeeper.setParameter1(shopGroup.getId());
                linkStarterKeeper.setParameter2(shopInitiator.getId());
                linkStarterKeeper.setParameter3(users.getId());
                linkStarterKeeperCacheRepository.cache(linkStarterKeeper);

                String code = "https://t.me/Skido_Bot?start=SG" + linkStarterKeeper.getSecretCode();

                Message message2_1 = new Message(null, 0, null, IOUtils.toByteArray(qrInputStream(code)), "Чтобы добавить, покажите QR партнеру 2");

                resultLevel.addMessage(message2_1);
                Message message2_2 = new Message(null, 1, Map.of(LanguageEnum.RU, "Или перешлите ссылку:"));

                resultLevel.addMessage(message2_2);
                Message message2_3 = new Message(null, 2, Map.of(LanguageEnum.RU, code));
                resultLevel.addMessage(message2_3);

                Message message = new Message(null, Map.of(LanguageEnum.RU,
                        "Вам должена группа " + partnerGroup.getName() + " " + partnerGroup.getSum() + " при лимите " + shopGroup.getLimitSum()));
                //messageRepository.save(message);
                resultLevel.addMessage(message);

                ButtonRow row = new ButtonRow();
                //здесь берется shop id, в отличии от Partner - там берется id для PartnerGroup
                Button button = new Button(row, Map.of(LanguageEnum.RU,
                        "Списать c " + partnerGroup.getName() + " (max " + partnerGroup.getSum() + ")"),
                        initialLevel.level_WITHDRAW_PARTNER_GROUP.getIdString() + partnerGroup.getId());
                row.add(button);
                resultLevel.addRow(row);

            } else {
                Message message = new Message(null, Map.of(LanguageEnum.RU,
                        "Вы должены группе " + partnerGroup.getName() + " " + partnerGroup.getSum() + " при лимите " + shopGroup.getLimitSum()));
                //messageRepository.save(message);
                resultLevel.addMessage(message);
            }

        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }

}
