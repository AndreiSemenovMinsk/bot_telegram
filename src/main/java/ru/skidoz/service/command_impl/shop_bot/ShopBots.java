package ru.skidoz.service.command_impl.shop_bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Structures;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.command.CommandName.*;

/**
 * @author andrey.semenov
 */
@Component
public class ShopBots implements Command {

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ButtonCacheRepository buttonCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    ConcurrentMap<Integer, Object> editedBots = new ConcurrentHashMap<>();

    LevelDTOWrapper showEditInterface(LevelDTOWrapper cloneEditBotLevel, LanguageEnum language) {
        // учитываем правило - одно соообщение от чат-бота - всегда один level


        List<ButtonRow> buttonRows = new ArrayList<>();
        for (ButtonRow row : cloneEditBotLevel.getButtonRows()) {
            for (Button button : row.getButtonList()) {

                if (!button.getCallback().equals(initialLevel.level_INITIALIZE.getIdString())) {
                    ButtonRow editRowForButton = new ButtonRow();
                    Button editButton = new Button(editRowForButton, Map.of(LanguageEnum.RU, "Изм. " + button.getName(language) + '"'), "@" + UPDATE_BUTTON.name() + button.getCallback());
                    editRowForButton.add(editButton);
                    buttonRows.add(editRowForButton);

                    ButtonRow editRowForButton2 = new ButtonRow();
                    Button deleteButton = new Button(editRowForButton2, Map.of(LanguageEnum.RU,"Удал. " + button.getName(language) + '"'), "@" + DELETE_BUTTON.name() + button.getCallback());
                    editRowForButton2.add(deleteButton);
                    buttonRows.add(editRowForButton2);
                }

            }
        }
        buttonRows.forEach(cloneEditBotLevel::addRow);

        for (Message message : cloneEditBotLevel.getMessages()) {
            ButtonRow editRowForMessage = new ButtonRow();

            System.out.println("message.getText()" + message.getText(language));

            if (message.getText(language) != null) {
                Button editButton = new Button(editRowForMessage,
                        Map.of(LanguageEnum.RU, "Изм. сообщ. " + message.getText(language).substring(0, Math.min(10, message.getText(language).length()))),
                        "@" + UPDATE_MESSAGE.name() + message.getId());
                //buttonRepository.save(editButton);
                editRowForMessage.add(editButton);
            }
            cloneEditBotLevel.addRow(editRowForMessage);
        }

        ButtonRow rowForNewLevelButtons1 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons1);
        Button newLevelButton = new Button(rowForNewLevelButtons1, Map.of(LanguageEnum.RU,"Добавить новое сообщение"), "@" + NEW_LEVEL_BUTTON.name() +cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons1.add(newLevelButton);

        ButtonRow rowForNewLevelButtons2 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons2);
        Button newLevelInputButton = new Button(rowForNewLevelButtons2, Map.of(LanguageEnum.RU,"+сообщ. с вводом клиента"), "@" + NEW_LEVEL_INPUT_BUTTON.name() +cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons2.add(newLevelInputButton);

        ButtonRow rowForNewLevelButtons3 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons3);
        Button endMessageButton = new Button(rowForNewLevelButtons3, Map.of(LanguageEnum.RU,"+сообщ. и подтв. заявки клиентом"), "@" + NEW_LEVEL_END_BUTTON.name() +cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons3.add(endMessageButton);

        ButtonRow rowForNewLevelButtons4 = new ButtonRow();
        cloneEditBotLevel.addRow(rowForNewLevelButtons4);
        Button deleteButton = new Button(rowForNewLevelButtons4, Map.of(LanguageEnum.RU,"Удалить уровень"), "@" + DELETE_LEVEL.name() +cloneEditBotLevel.getLevel().getIdString());
        rowForNewLevelButtons4.add(deleteButton);


/*
        ButtonRow preLastRow = new ButtonRow();
        Button clientParams = new Button(cloneEditBotLevel, "Параметры клиента ", PARAMS.name());
        buttonRepository.save(clientParams);
        preLastRow.add(clientParams);
        cloneEditBotLevel.addRow(preLastRow);
        levelRepository.save(cloneEditBotLevel);
        */
/* не надо рекурсии - каждый раз при переходе на новый уровень его отрисовываем

        List<Level> childLevels = new ArrayList<>();
        for (Level childLevel : cloneEditBotLevel.getChildLevels()) {
            childLevels.add(showEditInterface(childLevel));
        }
        cloneEditBotLevel.setChildLevels(childLevels);
*/
//        TODO 22.04.2022!! попробую, но не уверен
//   22.04.2022!! попробую, но не уверен     cloneEditBotLevel.getLevel().setCallName(SHOP_BOTS.name());

        return cloneEditBotLevel;
    }

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++ShopBots+++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevel = null;

        LanguageEnum language = users.getLanguage();

        if (update.getCallbackQuery() != null) {

            String callback = update.getCallbackQuery().getData();

            System.out.println("callback***" + callback);

        /*if (callback.startsWith("@" + BOT_LEVEL.name())) {
        String code = callback.substring(BOT_LEVEL.name().length() + 1);
        Bot bot = botRepository.findById(Integer.valueByCode(code));

        resultLevel = showEditInterface(bot.getInitialLevel());
        } else*/

            System.out.println("initialLevel.level_SHOP_BOTS.getIdString()*" + initialLevel.level_SHOP_BOTS.getIdString());
            System.out.println("callback.equals*" + callback.equals(initialLevel.level_SHOP_BOTS.getIdString()));

        /*Shop shopInitiator = shopRepository.findById(users.getCurrentAdminShop());
        Bot bot = botRepository.findByShopId(shopInitiator.getId());*/

            if (callback.equals(initialLevel.level_SHOP_BOTS.getIdString())) { // для магазина - список всех ботов

            /*Level userLevel = levelRepository.findByUser_IdAndCallName(users.getId(), SHOP_BOTS.name());
            System.out.println("userLevel*" + userLevel.getCallName());
            System.out.println("userLevel*" + userLevel.getIdString());
            System.out.println("userLevel*" + userLevel);
*/
                //resultLevel = showEditInterface(levelRepository.findById(bot.getInitialLevel()));
                System.out.println("users.getCurrentChangingBot()*" + users.getCurrentChangingBot());

                if (users.getCurrentChangingBot() == null) {
                    resultLevel = initialLevel.convertToLevel(initialLevel.level_ADD_BOT,
                            true,
                            true);
                } else {

                    System.out.println("169+++" + users);
                    System.out.println("users.getCurrentChangingBot()" + users.getCurrentChangingBot());

                    Bot bot = botCacheRepository.findById(users.getCurrentChangingBot());

                    System.out.println("bot+++++" + bot);

                    Level initialBotLevel = levelCacheRepository.findById(bot.getInitialLevel());

                    System.out.println("initialBotLevel+++++" + initialBotLevel);

                    if (editedBots.containsKey(initialBotLevel.getId())) {

                        System.out.println("containsKey*" + bot.getInitialLevel());

                        resultLevel = showEditInterface(initialLevel.convertToLevel(initialBotLevel,
                                true,
                                true),
                                language);
                    } else {

                        System.out.println("not containsKey*" + bot.getInitialLevel());

                        editedBots.put(initialBotLevel.getId(), new Object());
                        resultLevel = showEditInterface(initialLevel.convertToLevel(initialBotLevel,
                                true,
                                true),
                                language);
                    }
                }
/*
            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setLevel(resultLevel);
            })));*/
            } else if (callback.startsWith("@" + UPDATE_BUTTON.name())) {

                String buttonCallback = callback.substring(UPDATE_BUTTON.name().length() + 1);
                //Level taxiBotLevel = levelRepository.findByUser_IdAndCallName(users.getId(), ADD_TAXI_BOT.name());
                System.out.println("***************level**********************" + level);

                Button button = buttonCacheRepository.findByButtonRow_LevelAndCallback(level, buttonCallback);
                users.setCurrentChangingButton(button.getId());
                users.setCurrentLevelBeforeConfigId(level.getId());

                userCacheRepository.save(users);

                System.out.println("***************button**********************" + button);
                System.out.println("buttonCallback*" + buttonCallback);

                //resultLevel = initialLevel.level_EDIT_BUTTON_NAME;
                resultLevel = initialLevel.convertToLevel(initialLevel.level_EDIT_BUTTON_NAME,
                        true,
                        false);
            } else if (callback.startsWith("@" + UPDATE_MESSAGE.name())) {

                String messageId = callback.substring(UPDATE_MESSAGE.name().length() + 1);
//                Message message = messageRepository.findById(Structures.parseLong(messageId));

                System.out.println("messageId++++" + messageId);
                System.out.println(Structures.parseLong(messageId));


                users.setCurrentChangingMessage(Structures.parseInt(messageId));
                users.setCurrentLevelBeforeConfigId(level.getId());

                userCacheRepository.save(users);

                System.out.println("messageId*" + messageId);
                //resultLevel = initialLevel.level_EDIT_MESSAGE;
                resultLevel = initialLevel.convertToLevel(initialLevel.level_EDIT_MESSAGE,
                        true,
                        false);
            } else if (callback.startsWith("@" + NEW_LEVEL_BUTTON.name())) {

                String levelId = callback.substring(NEW_LEVEL_BUTTON.name().length() + 1);
                Level changingLevel = levelCacheRepository.findById(Structures.parseInt(levelId));

                System.out.println("changingLevel" + changingLevel);
                System.out.println("level" + level);

                //users.setCurrentChangingLevel(changingLevel);
                users.setCurrentLevelBeforeConfigId(level.getId());

                userCacheRepository.save(users);

                //resultLevel = initialLevel.level_EDIT_MESSAGE;
                resultLevel = initialLevel.convertToLevel(initialLevel.level_NEW_LEVEL_BUTTON,
                        true,
                        false);
            } else if (callback.startsWith("@" + NEW_LEVEL_INPUT_BUTTON.name())) {

                String levelId = callback.substring(NEW_LEVEL_INPUT_BUTTON.name().length() + 1);
                Level changingLevel = levelCacheRepository.findById(Structures.parseInt(levelId));


                System.out.println("changingLevel" + changingLevel);
                System.out.println("level" + level);

                //users.setCurrentChangingLevel(changingLevel);
                users.setCurrentLevelBeforeConfigId(level.getId());

                userCacheRepository.save(users);

                //resultLevel = initialLevel.level_EDIT_MESSAGE;
                resultLevel = initialLevel.convertToLevel(initialLevel.level_NEW_LEVEL_INPUT_BUTTON,
                        true,
                        false);
            } else if (callback.startsWith("@" + NEW_LEVEL_END_BUTTON.name())) {

                String levelId = callback.substring(NEW_LEVEL_END_BUTTON.name().length() + 1);
                Level changingLevel = levelCacheRepository.findById(Structures.parseInt(levelId));

                System.out.println("changingLevel" + changingLevel);
                System.out.println("level" + level);

                //users.setCurrentChangingLevel(changingLevel);
                users.setCurrentLevelBeforeConfigId(level.getId());

                userCacheRepository.save(users);

                //resultLevel = initialLevel.level_EDIT_MESSAGE;
                resultLevel = initialLevel.convertToLevel(initialLevel.level_NEW_LEVEL_END_BUTTON,
                        true,
                        false);
            } else if (callback.startsWith("@" + DELETE_BUTTON.name())) {
                String buttonCallback = callback.substring(DELETE_BUTTON.name().length() + 1);

                System.out.println(buttonCallback);
                //Level taxiBotLevel = levelRepository.findByUser_IdAndCallName(users.getId(), ADD_TAXI_BOT.name());
                Button button = buttonCacheRepository.findByButtonRow_LevelAndCallback(level, buttonCallback);

                System.out.println("***************button**********************" + button);

                buttonCacheRepository.delete(button);

                System.out.println("***************level**********************" + level);

                //не удаляем сам уровень, а удаляем "ссылку" на него - кнопку или переход по сообщению
//                Level deletedLevel = levelCacheRepository.findById(Structures.parseLong(buttonCallback));
//                deletedLevel.setSourceIsMessage(false);
//                levelCacheRepository.save(deletedLevel);
//
//                System.out.println("buttonCallback*" + buttonCallback);

                //resultLevel = level;
                resultLevel = initialLevel.convertToLevel(level,
                        true,
                        true);
                /*if (!editedBots.containsKey(level.getId())) {

                    editedBots.put(level.getId(), new Object());
                    resultLevel = showEditInterface(resultLevel);
                }*/
                resultLevel = showEditInterface(resultLevel, language);
            } else if (callback.startsWith("@" + DELETE_LEVEL.name())) {
                String levelId = callback.substring(DELETE_LEVEL.name().length() + 1);

                System.out.println(levelId);

                System.out.println("***************level**********************" + level);

                //не удаляем сам уровень, а удаляем "ссылку" на него - кнопку или переход по сообщению
                Level deletedLevel = levelCacheRepository.findById(Structures.parseInt(levelId));
                deletedLevel.setSourceIsMessage(false);
                levelCacheRepository.save(deletedLevel);

                System.out.println("deletedLevel***"+deletedLevel);
                System.out.println("levelId*" + levelId);

                //resultLevel = level;
                resultLevel = initialLevel.convertToLevel(level,
                        true,
                        true);
                /*if (!editedBots.containsKey(level.getId())) {

                    editedBots.put(level.getId(), new Object());
                    resultLevel = showEditInterface(resultLevel);
                }*/
                resultLevel = showEditInterface(resultLevel, language);
            } else {
                System.out.println("showEditInterface***else");
                Level editedLevel = levelCacheRepository.getChildLevel(level.getId(), Structures.parseInt(update.getCallbackQuery().getData().substring(1)));

                if (editedLevel != null){
                    resultLevel = initialLevel.convertToLevel(editedLevel,
                            true,
                            true);
                    /*if (!editedBots.containsKey(editedLevel.getId())) {

                        editedBots.put(editedLevel.getId(), new Object());
                        resultLevel = showEditInterface(resultLevel);
                    }*/
                    resultLevel = showEditInterface(resultLevel, language);
                } else {
                    resultLevel = initialLevel.convertToLevel(level,
                            true,
                            true);
                    /*if (!editedBots.containsKey(level.getId())) {

                        editedBots.put(level.getId(), new Object());
                        resultLevel = showEditInterface(resultLevel);
                    }*/
                    resultLevel = showEditInterface(resultLevel, language);
                }
            }

            System.out.println("resultLevel---" + resultLevel);
        } else if (update.getMessage() != null) {
            System.out.println();
            System.out.println("------------------------------Get Message-------------------------------");
            System.out.println();
            System.out.println();

            System.out.println("showEditInterface Message" + level.getId());

            System.out.println("level+++" + level);

            System.out.println(levelCacheRepository.findFirstBySourceIsMessageIsTrueAndParentLevel_Id(level.getId()));

            Level messageLevel = levelCacheRepository.findFirstBySourceIsMessageIsTrueAndParentLevel_Id(level.getId());

            resultLevel = initialLevel.convertToLevel(messageLevel,
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
            return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
                e.setChatId(users.getChatId());
                e.setUser(users);
                e.setLevel(finalResultLevel);
            })), null, null);

    }
}
