package ru.skidoz.service.command_impl.search_partner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
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
public class SearchPartnerResp2 implements Command {

    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        String callback = update.getCallbackQuery().getData();
        LevelDTOWrapper resultLevel = null;
        Long chatId = users.getChatId();

        System.out.println();
        System.out.println("*****************SearchPartnerResp2****************");
        System.out.println();
        System.out.println("callback*" + callback);
        System.out.println();

        // приходим с кнопки выбора контрагента
        //if (callback != null && callback.startsWith("@" + SEARCH_PARTNER_RESP.name())) {
        //String code = callback.substring(SEARCH_PARTNER_RESP.name().length() + 1);
        String code = callback.substring(19);
        Shop shopPartner = shopCacheRepository.findById(Integer.valueOf(code));
//        Long shopPartner = Integer.valueOf(code);
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
        shopInitiator.setCurrentConversationShop(shopPartner.getId());
        shopCacheRepository.save(shopInitiator);



        Partner partner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopPartner.getId());

        if (partner == null) {
            partner = new Partner(e -> {
                e.setCreditor(shopInitiator.getId());
                e.setDebtor(shopPartner.getId());
                e.setRate(0);
                e.setSum(BigDecimal.ZERO);
                e.setLim(BigDecimal.ZERO);
            });
        }
        partnerCacheRepository.save(partner);
        //это уже переход на следующий уровень, поэтому текст следующего уровня
        //resultLevel = initialLevel.level_SEARCH_PARTNER_RATE.clone();
        resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_PARTNER_RATE,
                false,
                false);

        //resultLevel.setMessages(new ArrayList<>());

        Message message = new Message(null, Map.of(LanguageEnum.RU,"Введите размер кэшбека в процентах по рекомендациям партнера " + shopPartner.getName()));
        resultLevel.addMessage(message);
        //}

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })), null, null);
    }
}
