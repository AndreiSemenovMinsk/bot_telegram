package ru.skidoz.service.command_impl.sellers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Structures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class SellersAdd implements Command {

    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {


        System.out.println();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++SellersAdd+++++++++++++++++++++++++++++++++");
        System.out.println();

        //Level resultLevel = initialLevel.level_PARTNERS.clone();
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SELLERS_ADD,
                false,
                false);

        String code = update.getCallbackQuery().getData().substring(19);

        Long chatId = users.getChatId();

        User usersCurrentConversation = userCacheRepository.findByChatId(Structures.parseLong(code));

        System.out.println("usersCurrentConversation++++++++++++++++++" + usersCurrentConversation);

        ButtonRow row = new ButtonRow();
        Button button0 = new Button(row, Map.of(LanguageEnum.RU, "Подтвердить добавление продавца " + usersCurrentConversation.getName()), initialLevel.level_SELLERS_ADD_APPROVE.getIdString() + code);
        /*****/row.add(button0);
        Button button1 = new Button(row, Map.of(LanguageEnum.RU, "Отмена"), initialLevel.level_SELLERS_ADD_DISMISS.getIdString());
        /*****/row.add(button1);
        resultLevel.addRow(row);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
