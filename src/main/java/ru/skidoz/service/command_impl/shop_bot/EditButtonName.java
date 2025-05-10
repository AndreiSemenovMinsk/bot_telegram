package ru.skidoz.service.command_impl.shop_bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class EditButtonName implements Command {

    @Autowired
    private ButtonCacheRepository buttonRepository;
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private InitialLevel initialLevel;


    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++EditButtonName+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();
        //Level resultLevel = levelRepository.findFirstByChatAndCallName(TelegramProcessor.CHAT, EDIT_BUTTON_NAME.name());
        String inputText = update.getMessage().getText();

        System.out.println("inputText***" + inputText);
        System.out.println(level);

        Integer buttonId = users.getCurrentChangingButton();
        Button button = buttonRepository.findById(buttonId);
        button.addName(inputText, users.getLanguage());
        buttonRepository.save(button);

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(levelRepository.findById(users.getCurrentLevelBeforeConfigId()),
                true,
                false);

        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(resultLevel);
        })));
    }
}
