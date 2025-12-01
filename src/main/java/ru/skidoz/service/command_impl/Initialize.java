package ru.skidoz.service.command_impl;


import static ru.skidoz.service.initializers.InitialLevel.SHOP;

import java.util.Collections;
import java.util.Objects;

import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class Initialize implements Command {

    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) {

        System.out.println();
        System.out.println("+++++++++++++++++++++++++Initialize++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();
        System.out.println(users.getChatId());

        if (Objects.equals(users.getCurrentRunnerShop(), SHOP.getId())) {

            if (shopCacheRepository.findById(userCacheRepository.findByChatId(users.getChatId()).getSellerShop()) != null){
                return new LevelResponse(
                        Collections.singletonList(new LevelChat(e -> {
                            e.setChatId(users.getChatId());
                            e.setUser(users);
                            e.setLevel(initialLevel.convertToLevel(
                                    initialLevel.level_ADMIN_ADMIN,
                                    true,
                                    true));
                        })), null, null);
            } else {
                return new LevelResponse(
                        Collections.singletonList(new LevelChat(e -> {
                            e.setChatId(users.getChatId());
                            e.setUser(users);
                            e.setLevel(initialLevel.convertToLevel(
                                    initialLevel.level_ADMIN,
                                    true,
                                    true));
                        })), null, null);
            }
        } else {
            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setUser(users);
                e.setLevel(initialLevel.convertToLevel(initialLevel.level_INITIALIZE,
                        true,
                        true));
            })), null, null);
        }
    }
}
