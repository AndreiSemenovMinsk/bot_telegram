package ru.skidoz.service.command_impl.sellers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class Sellers implements Command {

    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {


        System.out.println();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++Sellers+++++++++++++++++++++++++++++++++");
        System.out.println();

        //Level resultLevel = initialLevel.level_PARTNERS.clone();
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SELLERS,
                true,
                true);
        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        System.out.println("chatId+++" + chatId);
        System.out.println("shopInitiator+++" + shopInitiator);


        Set<User> sellers = userCacheRepository.findAllByShopId(shopInitiator.getId());

        List<Long> sellerChatIds = new ArrayList<>();
        for (User seller : sellers){

            sellerChatIds.add(seller.getChatId());

            Message message = new Message(null, Map.of(LanguageEnum.RU, seller.getName() + " является проавцом в " + shopInitiator.getName()));
            //messageRepository.save(message);
            resultLevel.addMessage(message);

            ButtonRow rowSeller = new ButtonRow();
            //здесь берется shop id, в отличии от Partner - там берется id для PartnerGroup
            Button buttonseller = new Button(rowSeller, Map.of(LanguageEnum.RU, "Удалить продавца " + seller.getName()), initialLevel.level_SELLERS_REMOVE.getIdString() + seller.getChatId());
            rowSeller.add(buttonseller);
            resultLevel.addRow(rowSeller);
        }

        if (!sellerChatIds.contains(shopInitiator.getCurrentConversationShopUserChatId())){
            User usersCurrentConversation = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());
            System.out.println("usersCurrentConversation+++" + usersCurrentConversation);

            ButtonRow row = new ButtonRow();
            Button button = new Button(row, Map.of(LanguageEnum.RU, "Сделать " + usersCurrentConversation.getName() + " продавцом"),
                    initialLevel.level_SELLERS_ADD.getIdString() + usersCurrentConversation.getChatId());
            row.add(button);
            resultLevel.addRow(row);
        }
//        users.setCurrentSearchAbstractGroupEntity(null);
//        userRepository.saveByMap(users);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
