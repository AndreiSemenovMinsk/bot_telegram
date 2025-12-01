package ru.skidoz.service.command_impl.shop_bot;

import static ru.skidoz.service.command.CommandName.DELETE_BUTTON;
import static ru.skidoz.service.command.CommandName.DELETE_LEVEL;
import static ru.skidoz.service.command.CommandName.EDIT_BOT;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_END_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_INPUT_BUTTON;
import static ru.skidoz.service.command.CommandName.UPDATE_BUTTON;
import static ru.skidoz.service.command.CommandName.UPDATE_MESSAGE;
import static ru.skidoz.util.Structures.parseInt;

import com.google.zxing.WriterException;
import ru.skidoz.aop.repo.BotCacheRepository;
import ru.skidoz.aop.repo.BotTypeCacheRepository;
import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.Bot;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.command.Command;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.util.Structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class EditBot implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ButtonCacheRepository buttonCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private InitialLevel initialLevel;


    LevelDTOWrapper showEditInterface(LevelDTOWrapper cloneEditBotLevel, LanguageEnum language) {
        // учитываем правило - одно соообщение от чат-бота - всегда один level

        System.out.println("showEditInterface*****" + cloneEditBotLevel);

        final Collection<ButtonRow> botButtonRows = buttonRowCacheRepository.findAllByLevel_Id(
                cloneEditBotLevel.getLevel().getId());

        final Collection<Message> botMessages = messageCacheRepository.findAllByLevel_Id(
                cloneEditBotLevel.getLevel().getId());


        System.out.println("botButtonRows---" + botButtonRows.size());


        List<ButtonRow> buttonRows = new ArrayList<>();
        for (ButtonRow row : botButtonRows) {
            for (Button button : row.getButtonList()) {

                System.out.println("button 85****---" + button);

                if (!button.getCallback().equals(initialLevel.level_INITIALIZE.getIdString())) {
                    ButtonRow editRowForButton = new ButtonRow();
                    Button editButton = new Button(
                            editRowForButton,
                            Map.of(LanguageEnum.RU, "Изм. " + button.getName(language)),
                            "@" + UPDATE_BUTTON.name() + "*" + button.getCallback());
                    editRowForButton.add(editButton);
                    buttonRows.add(editRowForButton);

                    ButtonRow editRowForButton2 = new ButtonRow();
                    Button deleteButton = new Button(
                            editRowForButton2,
                            Map.of(LanguageEnum.RU, "Удал. " + button.getName(language)),
                            "@" + DELETE_BUTTON.name() + "*" + button.getCallback());
                    editRowForButton2.add(deleteButton);
                    buttonRows.add(editRowForButton2);

                    ButtonRow editRowForButton3 = new ButtonRow();
                    Button goToButton = new Button(
                            editRowForButton3,
                            Map.of(LanguageEnum.RU, "Перейти по " + button.getName(language)),
                            button.getCallback());
                    editRowForButton3.add(goToButton);
                    buttonRows.add(editRowForButton3);
                }
            }
        }
        buttonRows.forEach(cloneEditBotLevel::addRow);

        System.out.println("botMessages---" + botMessages.size());

        for (Message message : botMessages) {
            ButtonRow editRowForMessage = new ButtonRow();

            System.out.println("message.getText()" + message.getText(language));

            if (message.getText(language) != null) {
                Button editButton = new Button(editRowForMessage, Map.of(LanguageEnum.RU, "Изм. сообщ. " + message.getText(language)
                                        .substring(0, Math.min(10, message.getText(language).length()))),
                        "@" + UPDATE_MESSAGE.name() + "*" + message.getId());
                //buttonRepository.save(editButton);
                editRowForMessage.add(editButton);
            }
            cloneEditBotLevel.addRow(editRowForMessage);
        }

        ButtonRow rowForNewLevelButtons1 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons1);
        Button newLevelButton = new Button(
                rowForNewLevelButtons1,
                Map.of(LanguageEnum.RU, "Добавить новое сообщение"),
                "@" + NEW_LEVEL_BUTTON.name() + "*" + cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons1.add(newLevelButton);

        ButtonRow rowForNewLevelButtons2 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons2);
        Button newLevelInputButton = new Button(
                rowForNewLevelButtons2,
                Map.of(LanguageEnum.RU, "+сообщ. с вводом клиента"),
                "@" + NEW_LEVEL_INPUT_BUTTON.name() + "*" + cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons2.add(newLevelInputButton);

        ButtonRow rowForNewLevelButtons3 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons3);
        Button endMessageButton = new Button(
                rowForNewLevelButtons3,
                Map.of(LanguageEnum.RU, "+сообщ. и подтв. заявки клиентом"),
                "@" + NEW_LEVEL_END_BUTTON.name() + "*" + cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons3.add(endMessageButton);



        final Level childMessageLevel = levelCacheRepository
                .findBySourceIsMessageAndParentLevelId(true, cloneEditBotLevel.getLevel().getId());

        System.out.println("childMessageLevel---" + childMessageLevel);

        if (childMessageLevel != null) {

            ButtonRow goToMessageLevel = new ButtonRow();
            cloneEditBotLevel.addRow(goToMessageLevel);
            Button goToMessageButton = new Button(
                    goToMessageLevel,
                    Map.of(LanguageEnum.RU, "Перейти на уровень после ввода клиентского сообщения"),
                    childMessageLevel.getIdString());
            goToMessageLevel.add(goToMessageButton);
        }


        ButtonRow rowForNewLevelButtons4 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons4);
        Button deleteButton = new Button(
                rowForNewLevelButtons4,
                Map.of(LanguageEnum.RU, "Удалить уровень"),
                "@" + DELETE_LEVEL.name() + "*" + cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons4.add(deleteButton);

        return cloneEditBotLevel;
    }

    @Override
    public LevelResponse runCommand(Update update, Level level, User users)
            throws IOException, WriterException {

        System.out.println();
        System.out.println(
                "++++++++++++++++++++++++++++++++++EditBot+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevel = null;

        LanguageEnum language = users.getLanguage();

        if (update.getCallbackQuery() != null) {

            String callback = update.getCallbackQuery().getData();


            System.out.println("callback***" + callback);
            System.out.println("initialLevel.level_SHOP_BOTS.getIdString()*" + initialLevel.level_SHOP_BOTS.getIdString());
            System.out.println("callback.equals*" + callback.equals(initialLevel.level_SHOP_BOTS.getIdString()));
            System.out.println(levelCacheRepository.findById(parseInt(callback)));




            if (callback.startsWith("@")) {

                final String[] callbackArr = callback.substring(1).split("\\*");
                String command = callbackArr[0];
                Integer callbackId = parseInt(callbackArr[1]);


                System.out.println("level " + level);
                System.out.println("command " + command);
                System.out.println("callbackId*" + callbackId);

                if (UPDATE_BUTTON.name().equals(command)) {

                    Button button = buttonCacheRepository.findById(callbackId);
                    users.setCurrentChangingButton(button.getId());
                    users.setCurrentLevelBeforeConfigId(level.getId());

                    userCacheRepository.save(users);

                    System.out.println("***************button**********************" + button);

                    resultLevel = initialLevel.convertToLevel(
                            initialLevel.level_EDIT_BUTTON_NAME,
                            true,
                            false);

                } else if (UPDATE_MESSAGE.name().equals(command)) {

                    users.setCurrentChangingMessage(callbackId);
                    users.setCurrentLevelBeforeConfigId(level.getId());

                    userCacheRepository.save(users);

                    resultLevel = initialLevel.convertToLevel(
                            initialLevel.level_EDIT_MESSAGE,
                            true,
                            false);
                } else if (NEW_LEVEL_BUTTON.name().equals(command)) {

                    Level changingLevel = levelCacheRepository.findById(callbackId);

                    System.out.println("changingLevel" + changingLevel);
                    System.out.println("level" + level);

                    users.setCurrentLevelBeforeConfigId(level.getId());

                    userCacheRepository.save(users);

                    resultLevel = initialLevel.convertToLevel(
                            initialLevel.level_NEW_LEVEL_BUTTON,
                            true,
                            false);

                } else if (NEW_LEVEL_INPUT_BUTTON.name().equals(command)) {

                    Level changingLevel = levelCacheRepository.findById(callbackId);


                    System.out.println("changingLevel" + changingLevel);
                    System.out.println("level" + level);
                    users.setCurrentLevelBeforeConfigId(level.getId());

                    userCacheRepository.save(users);

                    resultLevel = initialLevel.convertToLevel(
                            initialLevel.level_NEW_LEVEL_INPUT_BUTTON,
                            true,
                            false);

                } else if (NEW_LEVEL_END_BUTTON.name().equals(command)) {

                    Level changingLevel = levelCacheRepository.findById(callbackId);

                    System.out.println("changingLevel" + changingLevel);
                    System.out.println("level" + level);

                    users.setCurrentLevelBeforeConfigId(level.getId());

                    userCacheRepository.save(users);

                    resultLevel = initialLevel.convertToLevel(
                            initialLevel.level_NEW_LEVEL_END_BUTTON,
                            true,
                            false);
                } else if (DELETE_BUTTON.name().equals(command)) {

                    Button button = buttonCacheRepository.findById(callbackId);

                    System.out.println("***************button**********************" + button);

                    buttonCacheRepository.delete(button);

                    System.out.println("***************level**********************" + level);

                    //не удаляем сам уровень, а удаляем "ссылку" на него - кнопку или переход по сообщению
//                Level deletedLevel = levelCacheRepository.findById(Structures.parseLong(buttonCallback));
//                deletedLevel.setSourceIsMessage(false);
//                levelCacheRepository.save(deletedLevel);

                    resultLevel = initialLevel.convertToLevel(
                            level,
                            true,
                            true);

                    resultLevel = showEditInterface(resultLevel, language);
                } else if (DELETE_LEVEL.name().equals(command)) {

                    //не удаляем сам уровень, а удаляем "ссылку" на него - кнопку или переход по сообщению
                    Level deletedLevel = levelCacheRepository.findById(callbackId);
                    deletedLevel.setSourceIsMessage(false);
                    levelCacheRepository.save(deletedLevel);

                    System.out.println("deletedLevel***" + deletedLevel);

                    resultLevel = initialLevel.convertToLevel(
                            level,
                            true,
                            true);

                    resultLevel = showEditInterface(resultLevel, language);
                }
            } else {
                System.out.println("showEditInterface***else " + level);
                System.out.println("Parent level" + level.getId() + "---+---" + callback);

                Level editedLevel = levelCacheRepository.findByParentLevelAndCallName(level.getId(), callback);

                System.out.println("editedLevel****" + editedLevel);

                if (editedLevel != null) {
                    resultLevel = initialLevel.convertToLevel(
                            editedLevel,
                            true,
                            true);
                } else  {

                    Bot bot = botCacheRepository.findByShopId(users.getCurrentAdminShop());
                    System.out.println("bot+++++" + bot);

                    bot.setEdited(true);

                    Level initialBotLevel = levelCacheRepository.findById(bot.getInitialLevel());
                    System.out.println("initialBotLevel+++++" + initialBotLevel);

                    final Level levelByCallback = levelCacheRepository.findById(parseInt(callback));


                    System.out.println("levelByCallback+++++" + levelByCallback);

                    if (levelByCallback == null || EDIT_BOT.name().equals(levelByCallback.getCallName())) {
                        resultLevel = initialLevel.convertToLevel(
                                initialBotLevel,
                                true,
                                true);
                    } else {
                        resultLevel = initialLevel.convertToLevel(
                                levelByCallback,
                                true,
                                true);
                    }
                }

                resultLevel = showEditInterface(resultLevel, language);
            }

            System.out.println("resultLevel---" + resultLevel);
        } else if (update.getMessage() != null) {
            System.out.println();
            System.out.println(
                    "------------------------------Get Message-------------------------------");
            System.out.println();
            System.out.println();

            System.out.println("showEditInterface Message" + level.getId());

            System.out.println("level+++" + level);

            System.out.println(levelCacheRepository.findBySourceIsMessageAndParentLevelId(
                    true,
                    level.getId()));

            Level messageLevel = levelCacheRepository.findBySourceIsMessageAndParentLevelId(
                    true,
                    level.getId());

            resultLevel = initialLevel.convertToLevel(
                    messageLevel,
                    true,
                    true);

            System.out.println("resultLevel Message" + resultLevel);

            if (resultLevel != null) {

                /*if (!editedBots.containsKey(messageLevel.getId())) {

                    editedBots.put(messageLevel.getId(), new Object());
                    resultLevel = showEditInterface(resultLevel);
                }*/
                resultLevel = showEditInterface(resultLevel, language);
            }
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(finalResultLevel);
                })), null, null);

    }
}
