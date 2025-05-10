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
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class WithdrawPartnerResp3_1 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");
        LevelDTOWrapper resultLevel = null;
        Long chatId = users.getChatId();

        System.out.println();
        System.out.println("*****************WithdrawPartnerResp3_1****************");
        System.out.println();
        System.out.println("inputText*" + inputText);
        System.out.println();

        LevelDTOWrapper buyerLevel = null;
        LevelDTOWrapper friendLevel = null;
        List<LevelChat> levelChatDTOList = new ArrayList<>();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
        Shop shopPartner = shopCacheRepository.findById(shopInitiator.getCurrentConversationShop());
        Long friendChatId = shopPartner.getChatId();//.getAdminUser().getChatId();
        try {
            BigDecimal withdrawal = BigDecimal.valueOf(Integer.parseInt(inputText));

            Partner partner = partnerCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopPartner.getId());
            partner.setSum(partner.getSum().subtract(withdrawal));
            partnerCacheRepository.save(partner);

            buyerLevel = initialLevel.convertToLevel(initialLevel.level_WITHDRAW_PARTNER_RESP,
                    false,
                    false);

            friendLevel = initialLevel.convertToLevel(initialLevel.level_WITHDRAW_PARTNER_RESP,
                    false,
                    false);

            buyerLevel.setMessages(new ArrayList<>());
            friendLevel.setMessages(new ArrayList<>());
            friendLevel.setButtonRows(new ArrayList<>());

            Message message = new Message(null, Map.of(LanguageEnum.RU,"Вы списали " + withdrawal + " долга для " + shopPartner.getName()));
            buyerLevel.addMessage(message);


            Message message2 = new Message(null, Map.of(LanguageEnum.RU,shopPartner.getName() + " списал " + withdrawal + " Вашего долга"));
            friendLevel.addMessage(message2);

        } catch (NumberFormatException formatException){

            resultLevel = initialLevel.convertToLevel(level,
                    true,
                    false);
            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
        } catch (Exception e){
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
        return levelChatDTOList;
    }
}
