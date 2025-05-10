package ru.skidoz.service.command_impl.search_partner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * @author andrey.semenov
 */
@Component
public class SearchPartnerRate3 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        String inputText = update.getMessage().getText();
        LevelDTOWrapper resultLevel = null;
        Long chatId = users.getChatId();

        System.out.println();
        System.out.println("*****************SearchPartnerRate3****************");
        System.out.println();
        System.out.println("inputText*" + inputText);
        System.out.println();

        try {
            Integer rate = Integer.valueOf(inputText.trim());
            Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
            Shop shopPartner = shopCacheRepository.findById(shopInitiator.getCurrentConversationShop());

            System.out.println("shopInitiator++++++" + shopInitiator);
            System.out.println("shopPartner--------" + shopPartner);

            Partner partner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopPartner.getId());

            System.out.println("partner--------" + partner);

            partner.setRate(rate);
            partnerCacheRepository.save(partner);
// это переход на следующий уровень
            //resultLevel = initialLevel.level_SEARCH_PARTNER_LIMIT.clone();
            resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_PARTNER_LIMIT,
                    false,
                    false);
            //resultLevel.setMessages(new ArrayList<>());

            Message message = new Message(null, Map.of(LanguageEnum.RU,"Введите размер лимита для партнера " + shopPartner.getName()));
            resultLevel.addMessage(message);

        } catch (Exception e){
e.printStackTrace();
            System.out.println("Exception in SearchPartnerRate3 ++++++++ " + e);
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}
