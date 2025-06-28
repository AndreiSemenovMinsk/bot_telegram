package ru.skidoz.service.command_impl.search_partner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
public class SearchPartnerLimit4 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");
        LevelDTOWrapper buyerLevel = null;
        LevelDTOWrapper friendLevel = null;

        System.out.println();
        System.out.println("*****************SearchPartnerLimit4****************");
        System.out.println();
        System.out.println("inputText*" + inputText);
        System.out.println();

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
        Shop shopPartner = shopCacheRepository.findById(shopInitiator.getCurrentConversationShop());
        Long friendChatId = shopPartner.getChatId();//.getAdminUser().getChatId();
        List<LevelChat> levelChatDTOList = new ArrayList<>();

        try {
            int limit = Integer.parseInt(inputText);

            Partner partner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopPartner.getId());
            partner.setLim(new BigDecimal(limit));
            partnerCacheRepository.save(partner);

            //buyerLevel = initialLevel.level_SEARCH_PARTNER_END.clone();
            buyerLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_PARTNER_END,
                    false,
                    false);
            //friendLevel = initialLevel.level_ADD_PARTNER.clone();
            friendLevel = initialLevel.convertToLevel(initialLevel.level_ADD_PARTNER,
                    false,
                    false);

            buyerLevel.setMessages(new ArrayList<>());
            friendLevel.setMessages(new ArrayList<>());
            friendLevel.setButtonRows(new ArrayList<>());

            Message message = new Message(null, Map.of(LanguageEnum.RU,"Вы установили размер начисления кэшбека " + partner.getRate()
                    + "% и лимит " + partner.getLim() + " для " + shopCacheRepository.findById(partner.getDebtor()).getName()));
            buyerLevel.addMessage(message);


            Message message2 = new Message(null, Map.of(LanguageEnum.RU,"На Вас установили размер начисления кэшбека " + partner.getRate()
                    + "% и лимит " + partner.getLim() + " для " + shopCacheRepository.findById(partner.getCreditor()).getName()));
            friendLevel.addMessage(message2);
            ButtonRow row = new ButtonRow();
            List<Partner> partnerList = partnerCacheRepository.findAllByDebtor_Id(shopPartner.getId());

            String nameButton;
            if (partnerList.stream().map(Partner::getCreditor).anyMatch(shopInitiator.getId()::equals)){
                nameButton = "Редактировать " + shopInitiator.getName();
            } else {
                nameButton = "Добавить " + shopInitiator.getName();
            }
            Button button = new Button(row, Map.of(LanguageEnum.RU, nameButton), initialLevel.level_SEARCH_PARTNER_RESP_BUTTON.getIdString() + shopInitiator.getId());
            /***/row.add(button);
            friendLevel.addRow(row);
        } catch (NumberFormatException formatException){

            buyerLevel = initialLevel.convertToLevel(level,
                    false,
                    true);
            buyerLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
        } catch (Exception e) {
e.printStackTrace();
        }

        LevelDTOWrapper finalBuyertLevel = buyerLevel;
        LevelDTOWrapper finalFriendLevel = friendLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalBuyertLevel);
        }));
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(friendChatId);
            e.setLevel(finalFriendLevel);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
