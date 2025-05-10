package ru.skidoz.service.command_impl.search_group;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;


/**
 * @author andrey.semenov
 */
@Component
public class SearchGroupResp2 implements Command {

    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        String callback = update.getCallbackQuery().getData();
        LevelDTOWrapper resultLevel = null;
        Long chatId = users.getChatId();

        // приходим с кнопки выбора контрагента
        //if (levelName.startsWith("@" + SEARCH_PARTNER_GROUP_RESP.name())) {
        //String code = levelName.substring(SEARCH_PARTNER_GROUP_RESP.name().length() + 1);
        String code = callback.substring(19);
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
        ShopGroup shopGroupPartner = shopGroupCacheRepository.findById(Integer.valueOf(code));
//        Long shopGroupPartner = Integer.valueOf(code);
        shopInitiator.setCurrentConversationShopGroup(shopGroupPartner.getId());
        shopCacheRepository.save(shopInitiator);

        PartnerGroup partnerGroup = partnerGroupCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopGroupPartner.getId());

        if (partnerGroup == null) {
            partnerGroup = new PartnerGroup(e -> {
                e.setCreditor(shopInitiator.getId());
                e.setDebtor(shopGroupPartner.getId());
                e.setSum(BigDecimal.ZERO);
                e.setLim(BigDecimal.ZERO);
            });
        }

        partnerGroupCacheRepository.save(partnerGroup);

        //resultLevel = initialLevel.level_SEARCH_GROUP_LIMIT.clone();
        resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_GROUP_LIMIT,
                false,
                false);

        resultLevel.setMessages(new ArrayList<>());

        Message message = new Message(null, Map.of(RU, "Введите размер лимита для партнера " + shopGroupPartner.getName()));
        resultLevel.addMessage(message);
        //}

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}
