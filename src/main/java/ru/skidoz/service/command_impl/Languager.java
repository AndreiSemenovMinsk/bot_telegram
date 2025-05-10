package ru.skidoz.service.command_impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.model.entity.category.LanguageEnum.DE;
import static ru.skidoz.model.entity.category.LanguageEnum.EN;
import static ru.skidoz.service.command.CommandName.*;

/**
 * @author andrey.semenov
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Languager implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws IOException, WriterException {


        String callback = update.getCallbackQuery().getData();
        LevelDTOWrapper resultLevel = null;

        // приходим с кнопки выбора контрагента
        if (callback != null && level.getCallName().equals(LANGUAGER.name())) {

            String code = callback.substring(19);

            System.out.println("code+++++" + code);
            System.out.println("LanguageEnum.valueByCode(code)+++++" + LanguageEnum.valueByCode(code));

            users.setLanguage(LanguageEnum.valueByCode(code));

            resultLevel = initialLevel.convertToLevel(initialLevel.level_LANGUAGER,
                    false,
                    false);

            resultLevel.addMessage(new Message(null,
                    Map.of(LanguageEnum.RU, "Выбран язык Русский", EN, "Selected language English", DE, "Gewählte Sprache Deutsch")));
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(users.getChatId());
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}
