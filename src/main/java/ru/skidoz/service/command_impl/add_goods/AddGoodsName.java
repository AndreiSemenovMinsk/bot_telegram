package ru.skidoz.service.command_impl.add_goods;

import java.util.Collections;

import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
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
public class AddGoodsName implements Command {

    @Autowired
    private ProductCacheRepository productRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println("***********AddGoodsName**************");
        //Level resultLevel = level;//initialLevel.level_ADD_GOODS_NAME;

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                false);

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
