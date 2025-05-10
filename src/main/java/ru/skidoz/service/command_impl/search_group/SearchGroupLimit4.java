package ru.skidoz.service.command_impl.search_group;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
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
public class SearchGroupLimit4 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        String inputText = update.getMessage().getText();
        LevelDTOWrapper buyerLevel = null;
        LevelDTOWrapper friendLevel = null;
        Long chatId = users.getChatId();
        List<LevelChat> levelChatDTOList = new ArrayList<>();

        try {
            int limit = Integer.parseInt(inputText.trim());
            Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
            Integer/*ShopGroup*/ shopGroupPartner = shopInitiator.getCurrentConversationShopGroup();
            PartnerGroup partnerGroup = partnerGroupCacheRepository.findFirstByCreditor_IdAndDebtor_Id(shopInitiator.getId(), shopGroupPartner);

            partnerGroup.setLim(new BigDecimal(limit));
            partnerGroupCacheRepository.save(partnerGroup);

            buyerLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_GROUP_END,
                    false,
                    false);
            buyerLevel.setMessages(new ArrayList<>());

            ShopGroup shopGroup = shopGroupCacheRepository.findById(partnerGroup.getDebtor());

            String shopNames = shopGroup.getShopSet().stream().map(e -> e.getName().substring(0, 5)).collect(Collectors.joining(", "));

            Message message = new Message(null, Map.of(LanguageEnum.RU,"Вы установили лимит " + partnerGroup.getLim() + " для " + shopGroup.getName()));
            buyerLevel.addMessage(message);

            for (Shop shopPartner : shopGroup.getShopSet()) {

                //friendLevel = initialLevel.level_ADD_PARTNER.clone(users);
                friendLevel = initialLevel.convertToLevel(initialLevel.level_ADD_GROUP,
                        false,
                        false);
                //friendLevel.setMessages(new ArrayList<>());

                Long friendChatId = shopPartner.getChatId();

                Message message2 = new Message(null, Map.of(LanguageEnum.RU,"На Вас установили лимит " + partnerGroup.getLim() + " для " + shopGroup.getName()));
                ButtonRow row = new ButtonRow();

                Button button = new Button(row, Map.of(LanguageEnum.RU, "Редактировать " + shopNames), initialLevel.level_SEARCH_GROUP_RESP.getIdString() + partnerGroup.getId());
                row.add(button);
                friendLevel.addRow(row);
                friendLevel.addMessage(message2);

                LevelDTOWrapper finalFriendLevel = friendLevel;
                levelChatDTOList.add(new LevelChat(e -> {
                    e.setChatId(friendChatId);
                    e.setLevel(finalFriendLevel);
                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LevelDTOWrapper finalBuyertLevel = buyerLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalBuyertLevel);
        }));
        return levelChatDTOList;
    }
}
