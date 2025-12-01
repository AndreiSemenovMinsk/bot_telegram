package ru.skidoz.service.command_impl.sellers;

import java.util.Collections;

import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Structures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class SellersAddApprove implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {


        System.out.println();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++SellersAddApprove+++++++++++++++++++++++++++++++++");
        System.out.println();

        //Level resultLevel = initialLevel.level_PARTNERS.clone();
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SELLERS_ADD_APPROVE,
                true,
                false);

        String code = update.getCallbackQuery().getData().substring(19);

        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);
        Long/*UsersDTO*/ usersCurrentConversation = Structures.parseLong(code);//userCacheRepository.findByChatId(Structures.parseLong(code));


        User newSeller = userCacheRepository.findByChatId(usersCurrentConversation);
        newSeller.setSellerShop(shopInitiator.getId());
        userCacheRepository.save(newSeller);
//        shopInitiator.getSellerSet().add(userCacheRepository.findByChatId(usersCurrentConversation).getId());
//        shopCacheRepository.save(shopInitiator);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
