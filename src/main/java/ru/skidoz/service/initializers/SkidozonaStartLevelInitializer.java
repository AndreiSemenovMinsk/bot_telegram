package ru.skidoz.service.initializers;


import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.command.CommandName.ACTION_RATE_WITHDRAW_BASIC;
import static ru.skidoz.service.command.CommandName.ADMIN;
import static ru.skidoz.service.command.CommandName.CONSTRUCT;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_ADD;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_MIN_BILL_SHARE;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_SARAFAN_SHARE;
import static ru.skidoz.service.command.CommandName.MULTI_ACTION_LEVEL_BASIC;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_QUESTION_BASIC;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_RATE_BASIC;
import static ru.skidoz.service.command.CommandName.ONE_LEVEL_RATE_BASIC;
import static ru.skidoz.service.command.CommandName.TOKEN_REQUEST;
import static ru.skidoz.service.initializers.InitialLevel.Users;

import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.CategoryGroupCacheRepository;
import ru.skidoz.aop.repo.CategorySuperGroupCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class SkidozonaStartLevelInitializer {

    @Autowired
    private LevelCacheRepository levelRepository;

    @Autowired
    private ButtonRowCacheRepository buttonRowRepository;

    @Autowired
    private ButtonCacheRepository buttonRepository;
    @Autowired
    private MessageCacheRepository messageRepository;

    @Autowired
    private InitialLevel initialLevel;

    public void initLevels() {


//////////ДОБАВОЧНОЕ СТАРТОВОЕ МЕНЮ ДЛЯ МАГАЗИНА

            initialLevel.level_ADMIN.updateLevel(Users, ADMIN.name(), initialLevel.level_INITIALIZE, false);

            levelRepository.cache(initialLevel.level_ADMIN);

            ButtonRow row0_2_admin = new ButtonRow(initialLevel.level_ADMIN);
            buttonRowRepository.cache(row0_2_admin);
            initialLevel.level_ADMIN.addRow(row0_2_admin);
            Button button0_2_0_admin = new Button(row0_2_admin, Map.of(RU, "Создать магазин/сервис"), initialLevel.level_TOKEN_REQUEST.getIdString());
            buttonRepository.cache(button0_2_0_admin);
            row0_2_admin.add(button0_2_0_admin);
            Button button0_2_1_admin = new Button(row0_2_admin, Map.of(RU, "Админить магазины/сервисы"), initialLevel.level_ADMIN_SHOPS.getIdString());
            buttonRepository.cache(button0_2_1_admin);
            row0_2_admin.add(button0_2_1_admin);






            //////////СОЗДАНИЕ МАГАЗИНА С ТОВАРАМИ


            initialLevel.level_TOKEN_REQUEST.updateLevel(Users, TOKEN_REQUEST.name(), initialLevel.level_TOKEN_REQUEST, false);
            levelRepository.cache(initialLevel.level_TOKEN_REQUEST);
            Message message076_1 = new Message(initialLevel.level_TOKEN_REQUEST, Map.of(RU,
                    "Перейдите в @Bot_father, создайте новый бот и отправьте ссылку сообщением здесь"));
            messageRepository.cache(message076_1);
            initialLevel.level_TOKEN_REQUEST.addMessage(message076_1);





            //////////СОЗДАНИЕ МАГАЗИНА С ТОВАРАМИ

            initialLevel.level_CONSTRUCT.updateLevel(Users, CONSTRUCT.name(), initialLevel.level_TOKEN_REQUEST, false);
            levelRepository.cache(initialLevel.level_CONSTRUCT);
            Message message6_1 = new Message(initialLevel.level_CONSTRUCT, Map.of(RU, "Введите название магазина/сервиса"));
            messageRepository.cache(message6_1);
            initialLevel.level_CONSTRUCT.addMessage(message6_1);


            initialLevel.level_CONSTRUCT_MIN_BILL_SHARE.updateLevel(Users, CONSTRUCT_MIN_BILL_SHARE.name(), initialLevel.level_CONSTRUCT, true);

            levelRepository.cache(initialLevel.level_CONSTRUCT_MIN_BILL_SHARE);
            Message message3100_0 = new Message(initialLevel.level_CONSTRUCT_MIN_BILL_SHARE, Map.of(RU, "Использовать разные размеры скидки для различных уровней суммы покупок за прошлый месяц?"));
            messageRepository.cache(message3100_0);
            initialLevel.level_CONSTRUCT_MIN_BILL_SHARE.addMessage(message3100_0);
            ButtonRow row3100_0 = new ButtonRow(initialLevel.level_CONSTRUCT_MIN_BILL_SHARE);
            buttonRowRepository.cache(row3100_0);
            Button button3100_0_0 = new Button(row3100_0, Map.of(RU, "Нет"), initialLevel.level_ONE_LEVEL_RATE_BASIC.getIdString());
            buttonRepository.cache(button3100_0_0);
            row3100_0.add(button3100_0_0);
            initialLevel.level_CONSTRUCT_MIN_BILL_SHARE.addRow(row3100_0);
            ButtonRow row3100_1 = new ButtonRow(initialLevel.level_CONSTRUCT_MIN_BILL_SHARE);
            buttonRowRepository.cache(row3100_1);
            Button button3100_1_0 = new Button(row3100_0, Map.of(RU, "Да, добавить уровень"), initialLevel.level_MULTI_ACTION_LEVEL_BASIC.getIdString());
            buttonRepository.cache(button3100_1_0);
            row3100_1.add(button3100_1_0);
            initialLevel.level_CONSTRUCT_MIN_BILL_SHARE.addRow(row3100_1);

            initialLevel.level_MULTI_ACTION_LEVEL_BASIC.updateLevel(Users, MULTI_ACTION_LEVEL_BASIC.name(), initialLevel.level_CONSTRUCT_MIN_BILL_SHARE, true);

            levelRepository.cache(initialLevel.level_MULTI_ACTION_LEVEL_BASIC);
            Message message3200_0 = new Message(initialLevel.level_MULTI_ACTION_LEVEL_BASIC, Map.of(RU, "Введите размер уровня суммы"));
            messageRepository.cache(message3200_0);
            initialLevel.level_MULTI_ACTION_LEVEL_BASIC.addMessage(message3200_0);

            initialLevel.level_MULTI_LEVEL_RATE_BASIC.updateLevel(Users, MULTI_LEVEL_RATE_BASIC.name(), initialLevel.level_MULTI_ACTION_LEVEL_BASIC, true);

            levelRepository.cache(initialLevel.level_MULTI_LEVEL_RATE_BASIC);
            Message message2800_0 = new Message(initialLevel.level_MULTI_LEVEL_RATE_BASIC, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
            messageRepository.cache(message2800_0);
            initialLevel.level_MULTI_LEVEL_RATE_BASIC.addMessage(message2800_0);

            initialLevel.level_MULTI_LEVEL_QUESTION_BASIC.updateLevel(Users, MULTI_LEVEL_QUESTION_BASIC.name(), initialLevel.level_MULTI_LEVEL_RATE_BASIC, true);

            levelRepository.cache(initialLevel.level_MULTI_LEVEL_QUESTION_BASIC);
            Message message3100_2_0 = new Message(initialLevel.level_MULTI_LEVEL_QUESTION_BASIC, Map.of(RU, "Добавить уровень суммы покупок за прошлый месяц?"));
            messageRepository.cache(message3100_2_0);
            initialLevel.level_MULTI_LEVEL_QUESTION_BASIC.addMessage(message3100_2_0);
            ButtonRow row3100_2_0 = new ButtonRow(initialLevel.level_MULTI_LEVEL_QUESTION_BASIC);
            buttonRowRepository.cache(row3100_2_0);
            Button button3100_2_0_0 = new Button(row3100_2_0, Map.of(RU, "Нет"), initialLevel.level_ACTION_RATE_WITHDRAW_BASIC.getIdString());
            buttonRepository.cache(button3100_2_0_0);
            row3100_2_0.add(button3100_2_0_0);
            initialLevel.level_MULTI_LEVEL_QUESTION_BASIC.addRow(row3100_2_0);
            ButtonRow row3100_2_1 = new ButtonRow(initialLevel.level_MULTI_LEVEL_QUESTION_BASIC);
            buttonRowRepository.cache(row3100_2_1);
            Button button31_200_1_0 = new Button(row3100_2_0, Map.of(RU, "Да"), initialLevel.level_MULTI_ACTION_LEVEL_BASIC.getIdString());
            buttonRepository.cache(button31_200_1_0);
            row3100_2_1.add(button31_200_1_0);
            initialLevel.level_MULTI_LEVEL_QUESTION_BASIC.addRow(row3100_2_1);

            initialLevel.level_ONE_LEVEL_RATE_BASIC.updateLevel(Users, ONE_LEVEL_RATE_BASIC.name(), initialLevel.level_CONSTRUCT_MIN_BILL_SHARE, true);

            levelRepository.cache(initialLevel.level_ONE_LEVEL_RATE_BASIC);
            Message message2800_2_0 = new Message(initialLevel.level_ONE_LEVEL_RATE_BASIC, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
            messageRepository.cache(message2800_2_0);
            initialLevel.level_ONE_LEVEL_RATE_BASIC.addMessage(message2800_2_0);

            initialLevel.level_ACTION_RATE_WITHDRAW_BASIC.updateLevel(Users, ACTION_RATE_WITHDRAW_BASIC.name(), initialLevel.level_ONE_LEVEL_RATE_BASIC, true);

            levelRepository.cache(initialLevel.level_ACTION_RATE_WITHDRAW_BASIC);
            Message message2900_0 = new Message(initialLevel.level_ACTION_RATE_WITHDRAW_BASIC, Map.of(RU, "Введите в % максимальную долю списания кэшбека в стоимости последуюшей покупке"));
            messageRepository.cache(message2900_0);
            initialLevel.level_ACTION_RATE_WITHDRAW_BASIC.addMessage(message2900_0);


            initialLevel.level_CONSTRUCT_SARAFAN_SHARE.updateLevel(Users, CONSTRUCT_SARAFAN_SHARE.name(), initialLevel.level_ACTION_RATE_WITHDRAW_BASIC, true);
            levelRepository.cache(initialLevel.level_CONSTRUCT_SARAFAN_SHARE);
            Message message6_1_1 = new Message(initialLevel.level_CONSTRUCT_SARAFAN_SHARE, Map.of(RU, "Введите размер скидки клиенту, который посоветовал другу Ваш магазин, %"));
            messageRepository.cache(message6_1_1);
            initialLevel.level_CONSTRUCT_SARAFAN_SHARE.addMessage(message6_1_1);

            initialLevel.level_CONSTRUCT_ADD.updateLevel(Users, CONSTRUCT_ADD.name(), initialLevel.level_CONSTRUCT_SARAFAN_SHARE, true);
            levelRepository.cache(initialLevel.level_CONSTRUCT_ADD);
            Message message6_2_1 = new Message(initialLevel.level_CONSTRUCT_ADD, 0,
                    Map.of(RU, "Вы можете загрузить товары файлом Excel, " +
                            "для этого скачайте по ссылке http://skidozona.by, " +
                            "отредактируйте и загрузите на сервер, прикрепив документ"));
            messageRepository.cache(message6_2_1);
            initialLevel.level_CONSTRUCT_ADD.addMessage(message6_2_1);
            ButtonRow row5_0 = new ButtonRow(initialLevel.level_CONSTRUCT_ADD);
            buttonRowRepository.cache(row5_0);
            Button button5_0_0 = new Button(row5_0, Map.of(RU, "Добавить товары через бот"), initialLevel.level_ADD_GOODS.getIdString());
            buttonRepository.cache(button5_0_0);
            row5_0.add(button5_0_0);
            Button button5_0_1 = new Button(row5_0, Map.of(RU, "Добавить бот"), initialLevel.level_ADD_BOT.getIdString());
            buttonRepository.cache(button5_0_1);
            row5_0.add(button5_0_1);
            initialLevel.level_CONSTRUCT_ADD.addRow(row5_0);


            System.out.println("PRE addFinalButton");

                initialLevel.addFinalButton(initialLevel.level_ADMIN, initialLevel.level_ADMIN);

                System.out.println("POST addFinalButton");
    }

}
