package ru.skidoz.service.initializers;


import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.command.CommandName.ACTIONS;
import static ru.skidoz.service.command.CommandName.ACTION_RATE_PARTNER;
import static ru.skidoz.service.command.CommandName.ACTION_RATE_WITHDRAW;
import static ru.skidoz.service.command.CommandName.ACTION_TYPE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_COUPON_SOURCE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_COUPON_TARGET;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_LINK_SOURCE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_LINK_TARGET;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_RATE_SOURCE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_RATE_TARGET;
import static ru.skidoz.service.command.CommandName.ADD_BOT;
import static ru.skidoz.service.command.CommandName.ADD_GOODS;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_DESCRIPTION;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_END;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_NAME;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_PHOTO;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_PRICE;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_XLS;
import static ru.skidoz.service.command.CommandName.ADD_PARTNER;
import static ru.skidoz.service.command.CommandName.ADD_SHOP_TO_SHOP_GROUP;
import static ru.skidoz.service.command.CommandName.ADD_TAXI_BOT;
import static ru.skidoz.service.command.CommandName.ADMIN_ADMIN;
import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;
import static ru.skidoz.service.command.CommandName.APPROVE_NEW_GRUPP;
import static ru.skidoz.service.command.CommandName.APPROVE_NEW_PARTNER;
import static ru.skidoz.service.command.CommandName.B2B;
import static ru.skidoz.service.command.CommandName.B2NOB;
import static ru.skidoz.service.command.CommandName.BASIC;
import static ru.skidoz.service.command.CommandName.COUPON;
import static ru.skidoz.service.command.CommandName.COUPON_NUMBER;
import static ru.skidoz.service.command.CommandName.COUPON_RATE_WITHDRAW;
import static ru.skidoz.service.command.CommandName.DISCARD_NEW_GRUPP;
import static ru.skidoz.service.command.CommandName.DISCARD_NEW_PARTNER;
import static ru.skidoz.service.command.CommandName.EDIT_BOT;
import static ru.skidoz.service.command.CommandName.EDIT_BUTTON_NAME;
import static ru.skidoz.service.command.CommandName.EDIT_MESSAGE;
import static ru.skidoz.service.command.CommandName.GEOMAP;
import static ru.skidoz.service.command.CommandName.GOODS_LIST;
import static ru.skidoz.service.command.CommandName.LINK_TO_PRODUCT;
import static ru.skidoz.service.command.CommandName.LINK_TO_PRODUCT_NUMBER;
import static ru.skidoz.service.command.CommandName.MULTI_ACTION_LEVEL;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_QUESTION;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_RATE;
import static ru.skidoz.service.command.CommandName.NEGATIVE_COUNT;
import static ru.skidoz.service.command.CommandName.NEGATIVE_SUM;
import static ru.skidoz.service.command.CommandName.NEW_GRUPP;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_END_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_INPUT_BUTTON;
import static ru.skidoz.service.command.CommandName.NON_RESPONSE;
import static ru.skidoz.service.command.CommandName.ONE_LEVEL_RATE;
import static ru.skidoz.service.command.CommandName.PARTNERS;
import static ru.skidoz.service.command.CommandName.RESPONSE_BUYER_MESSAGE;
import static ru.skidoz.service.command.CommandName.RESPONSE_SHOP_MESSAGE;
import static ru.skidoz.service.command.CommandName.CREATE_GROUP;
import static ru.skidoz.service.command.CommandName.CREATE_GROUP_LIMIT;
import static ru.skidoz.service.command.CommandName.CREATE_GROUP_RESP;
import static ru.skidoz.service.command.CommandName.SELECT_LEVEL_TYPE;
import static ru.skidoz.service.command.CommandName.SELLERS;
import static ru.skidoz.service.command.CommandName.SELLERS_ADD;
import static ru.skidoz.service.command.CommandName.SELLERS_ADD_APPROVE;
import static ru.skidoz.service.command.CommandName.SELLERS_ADD_DISMISS;
import static ru.skidoz.service.command.CommandName.SELLERS_REMOVE;
import static ru.skidoz.service.command.CommandName.SELLERS_REMOVE_APPROVE;
import static ru.skidoz.service.command.CommandName.SELLERS_REMOVE_DISMISS;
import static ru.skidoz.service.command.CommandName.SEND_BUYER_MESSAGE;
import static ru.skidoz.service.command.CommandName.SEND_SHOP_MESSAGE;
import static ru.skidoz.service.command.CommandName.SHOP_BOTS;
import static ru.skidoz.service.command.CommandName.TAXI_LOCATION;
import static ru.skidoz.service.command.CommandName.TAXI_SUBMIT;
import static ru.skidoz.service.command.CommandName.VOTE_ADD_SHOP_GROUP;
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER;
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER_END;
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER_GROUP;
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER_RESP;
import static ru.skidoz.service.initializers.InitialLevel.Users;

import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.service.MenuCreator;
import ru.skidoz.util.MenuTypeEnum;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class AdminLevelInitializer {

    @Autowired
    private MenuCreator menuCreator;
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

    private static final String PHOTO_FILE = "config/photo.jpg";

    public void initLevels() {

            try {

                //////////ДОБАВОЧНОЕ СТАРТОВОЕ МЕНЮ ДЛЯ МАГАЗИНА

                initialLevel.level_ADMIN_ADMIN.updateLevel(Users, ADMIN_ADMIN.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_ADMIN_ADMIN);
                ButtonRow row0_0_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_0_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_0_admin0);
                Button button0_0_0_shop0 = new Button(row0_0_admin0, Map.of(RU, "Акции"), initialLevel.level_ACTIONS.getIdString());
                buttonRepository.cache(button0_0_0_shop0);
//                row0_0_admin0.add(button0_0_0_shop0);
                Button button0_0_1_shop0 = new Button(row0_0_admin0, Map.of(RU, "Корзины"), initialLevel.level_BASKETS_FOR_SHOP.getIdString());
                buttonRepository.cache(button0_0_1_shop0);
//                row0_0_admin0.add(button0_0_1_shop0);
                ButtonRow row0_1_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_1_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_1_admin0);
                Button button0_1_0_shop0 = new Button(row0_1_admin0, Map.of(RU, "Товары"), initialLevel.level_GOODS_LIST.getIdString());
                buttonRepository.cache(button0_1_0_shop0);
//                row0_1_admin0.add(button0_1_0_shop0);
                Button button0_1_1_shop0 = new Button(row0_1_admin0, Map.of(RU, "Партнеры"), initialLevel.level_PARTNERS.getIdString());
                buttonRepository.cache(button0_1_1_shop0);
//                row0_1_admin0.add(button0_1_1_shop0);
                ButtonRow row0_2_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_2_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_2_admin0);
                Button button1_0_1 = new Button(row0_2_admin0, Map.of(RU, "Бот магазина"), initialLevel.level_SHOP_BOTS.getIdString());
                buttonRepository.cache(button1_0_1);
//                row0_2_admin0.add(button1_0_1);
                Button button0_2_1_shop0 = new Button(row0_2_admin0, Map.of(RU, "Продавцы"), initialLevel.level_SELLERS.getIdString());
                buttonRepository.cache(button0_2_1_shop0);
//                row0_2_admin0.add(button0_2_1_shop0);
                ButtonRow row0_3_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_3_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_3_admin0);
                Button button0_3_1_shop0 = new Button(row0_3_admin0, Map.of(RU, "Geo-карта"), initialLevel.level_GEOMAP.getIdString(), "https://t.me/Skido_bot/geomapper");
                buttonRepository.cache(button0_3_1_shop0);
//                row0_3_admin0.add(button0_3_1_shop0);






                initialLevel.level_ADD_GOODS_XLS.updateLevel(Users, ADD_GOODS_XLS.name(), initialLevel.level_ADMIN_ADMIN, false);
                levelRepository.cache(initialLevel.level_ADD_GOODS_XLS);
                Message mess = new Message(initialLevel.level_ADD_GOODS_XLS, Map.of(RU, "Документ Excel будет обработан в течение часа"));
                messageRepository.cache(mess);
                initialLevel.level_ADD_GOODS_XLS.addMessage(mess);

                //////////КАРТА
                initialLevel.level_GEOMAP.updateLevel(Users, GEOMAP.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_GEOMAP);
                Message message00_1__ = new Message(initialLevel.level_GEOMAP, Map.of(RU, "Перейдите на страницу редактрирования местоположения вашего объекта https://skidozona.by/geomapper"));
                messageRepository.cache(message00_1__);
                initialLevel.level_GEOMAP.addMessage(message00_1__);
                ButtonRow row0_0_geo = new ButtonRow(initialLevel.level_GEOMAP);
                buttonRowRepository.cache(row0_0_geo);
                initialLevel.level_GEOMAP.addRow(row0_0_geo);
                Button button0_geo = new Button(row0_0_geo, Map.of(RU, "Акции"), "123");
                button0_geo.setWebApp("https://t.me/Skido_bot/geomapper");
                buttonRepository.cache(button0_geo);
                row0_0_geo.add(button0_geo);



                ///////////////////////// СПИСОК ПРОДАВЦОВ

                initialLevel.level_SELLERS.updateLevel(Users, SELLERS.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_SELLERS);
                Message message460_1 = new Message(initialLevel.level_SELLERS, Map.of(RU, "Список моих продавцов: "));
                messageRepository.cache(message460_1);
                initialLevel.level_SELLERS.addMessage(message460_1);
//                ButtonRow row460_0 = new ButtonRow(initialLevel.level_SELLERS);
//                buttonRowRepository.cache(row460_0);
//                Button button460_0_0 = new Button(row460_0, Map.of(RU, "Добавить нового продавца"), initialLevel.level_SELLERS_ADD.getIdString());
//                buttonRepository.cache(button460_0_0);
//                row460_0.add(button460_0_0);


                initialLevel.level_SELLERS_ADD.updateLevel(Users, SELLERS_ADD.name(), initialLevel.level_SELLERS, false);
                levelRepository.cache(initialLevel.level_SELLERS_ADD);
                ButtonRow row462_0_0 = new ButtonRow(initialLevel.level_SELLERS_ADD);
                buttonRowRepository.cache(row462_0_0);
                Button button462_0_0_0 = new Button(row462_0_0, Map.of(RU, "Добавить продавца"), initialLevel.level_SELLERS_ADD_APPROVE.getIdString());
                buttonRepository.cache(button462_0_0_0);
                row462_0_0.add(button462_0_0_0);
                Button button462_0_0_1 = new Button(row462_0_0, Map.of(RU, "Отмена"), initialLevel.level_SELLERS_ADD_DISMISS.getIdString());
                buttonRepository.cache(button462_0_0_1);
                row462_0_0.add(button462_0_0_1);
                initialLevel.level_SELLERS_ADD.addRow(row462_0_0);


                initialLevel.level_SELLERS_ADD_APPROVE.updateLevel(Users, SELLERS_ADD_APPROVE.name(), initialLevel.level_SELLERS_ADD, false);
                levelRepository.cache(initialLevel.level_SELLERS_ADD_APPROVE);
                Message message4601_1 = new Message(initialLevel.level_SELLERS_ADD_APPROVE, Map.of(RU, "Продавец добавлен"));
                messageRepository.cache(message4601_1);
                initialLevel.level_SELLERS_ADD_APPROVE.addMessage(message4601_1);


                initialLevel.level_SELLERS_ADD_DISMISS.updateLevel(Users, SELLERS_ADD_DISMISS.name(), initialLevel.level_SELLERS_ADD, false);
                levelRepository.cache(initialLevel.level_SELLERS_ADD_DISMISS);
                Message message4602_1 = new Message(initialLevel.level_SELLERS_ADD_DISMISS, Map.of(RU, "Добавление отменено"));
                messageRepository.cache(message4602_1);
                initialLevel.level_SELLERS_ADD_DISMISS.addMessage(message4602_1);

                initialLevel.level_SELLERS_REMOVE.updateLevel(Users, SELLERS_REMOVE.name(), initialLevel.level_SELLERS, false);
                levelRepository.cache(initialLevel.level_SELLERS_REMOVE);
                ButtonRow row463_0_0 = new ButtonRow(initialLevel.level_SELLERS_REMOVE);
                buttonRowRepository.cache(row463_0_0);
                Button button463_0_0_0 = new Button(row463_0_0, Map.of(RU, "Удалить продавца"), initialLevel.level_SELLERS_REMOVE_APPROVE.getIdString());
                buttonRepository.cache(button463_0_0_0);
                row463_0_0.add(button463_0_0_0);
                Button button463_0_0_1 = new Button(row463_0_0, Map.of(RU, "Отмена"), initialLevel.level_SELLERS_REMOVE_DISMISS.getIdString());
                buttonRepository.cache(button463_0_0_1);
                row463_0_0.add(button463_0_0_1);
                initialLevel.level_SELLERS_REMOVE.addRow(row463_0_0);

                initialLevel.level_SELLERS_REMOVE_APPROVE.updateLevel(Users, SELLERS_REMOVE_APPROVE.name(), initialLevel.level_SELLERS_REMOVE, false);
                levelRepository.cache(initialLevel.level_SELLERS_REMOVE_APPROVE);
                Message message4603_1 = new Message(initialLevel.level_SELLERS_REMOVE_APPROVE, Map.of(RU, "Продавец удален"));
                messageRepository.cache(message4603_1);
                initialLevel.level_SELLERS_REMOVE_APPROVE.addMessage(message4603_1);

                initialLevel.level_SELLERS_REMOVE_DISMISS.updateLevel(Users, SELLERS_REMOVE_DISMISS.name(), initialLevel.level_SELLERS_REMOVE, false);
                levelRepository.cache(initialLevel.level_SELLERS_REMOVE_DISMISS);
                Message message4604_1 = new Message(initialLevel.level_SELLERS_REMOVE_DISMISS, Map.of(RU, "Удаление отменено"));
                messageRepository.cache(message4604_1);
                initialLevel.level_SELLERS_REMOVE_DISMISS.addMessage(message4604_1);


                ///////////////////////// СПИСОК ПАРТНЕРОВ

                initialLevel.level_PARTNERS.updateLevel(Users, PARTNERS.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_PARTNERS);
                Message message46_1 = new Message(initialLevel.level_PARTNERS, Map.of(RU, "Список моих групп партнеров: "));
                messageRepository.cache(message46_1);
                initialLevel.level_PARTNERS.addMessage(message46_1);

                ButtonRow row46_0 = new ButtonRow(initialLevel.level_PARTNERS);
                buttonRowRepository.cache(row46_0);
                Button button46_0_0 = new Button(row46_0, Map.of(RU, "Создать новую группу"), initialLevel.level_CREATE_GROUP.getIdString());
                buttonRepository.cache(button46_0_0);
                row46_0.add(button46_0_0);
                initialLevel.level_PARTNERS.addRow(row46_0);



                initialLevel.level_ADD_SHOP_TO_SHOP_GROUP.updateLevel(Users, ADD_SHOP_TO_SHOP_GROUP.name(), initialLevel.level_PARTNERS, false);
                levelRepository.cache(initialLevel.level_VOTE_ADD_SHOP_GROUP);

                initialLevel.level_VOTE_ADD_SHOP_GROUP.updateLevel(Users, VOTE_ADD_SHOP_GROUP.name(), initialLevel.level_ADD_SHOP_TO_SHOP_GROUP, false);
                levelRepository.cache(initialLevel.level_VOTE_ADD_SHOP_GROUP);




                initialLevel.level_WITHDRAW_PARTNER_GROUP.updateLevel(Users, WITHDRAW_PARTNER_GROUP.name(), initialLevel.level_PARTNERS, false);
                levelRepository.cache(initialLevel.level_WITHDRAW_PARTNER_GROUP);


                initialLevel.level_WITHDRAW_PARTNER.updateLevel(Users, WITHDRAW_PARTNER.name(), initialLevel.level_WITHDRAW_PARTNER_GROUP, false);
                levelRepository.cache(initialLevel.level_WITHDRAW_PARTNER);



                initialLevel.level_WITHDRAW_PARTNER_RESP.updateLevel(Users, WITHDRAW_PARTNER_RESP.name(), initialLevel.level_WITHDRAW_PARTNER, false);
                levelRepository.cache(initialLevel.level_WITHDRAW_PARTNER_RESP);
                Message message47_01 = new Message(initialLevel.level_WITHDRAW_PARTNER_RESP, Map.of(RU, "Введите сумму списания"));
                messageRepository.cache(message47_01);
                initialLevel.level_WITHDRAW_PARTNER_RESP.addMessage(message47_01);

                initialLevel.level_WITHDRAW_PARTNER_END.updateLevel(Users, WITHDRAW_PARTNER_END.name(), initialLevel.level_WITHDRAW_PARTNER_RESP, true);
                levelRepository.cache(initialLevel.level_WITHDRAW_PARTNER_END);
                Message message472_3_1 = new Message(initialLevel.level_WITHDRAW_PARTNER_END, Map.of(RU, "Вы списали долг WW для EE"));
                messageRepository.cache(message472_3_1);
                initialLevel.level_WITHDRAW_PARTNER_END.addMessage(message472_3_1);


                /*//SEARCH PARTNER нам незачем искать партнеров - они и так подключаются ссылкой, а не поиском?
                initialLevel.level_SEARCH_PARTNER.updateLevel(Users, SEARCH_PARTNER.name(), initialLevel.level_PARTNERS, false);
                levelRepository.cache(initialLevel.level_SEARCH_PARTNER);
                Message message47_g_1 = new Message(initialLevel.level_SEARCH_PARTNER, Map.of(RU, "Введите название группы"));
                messageRepository.cache(message47_g_1);
                initialLevel.level_SEARCH_PARTNER.addMessage(message47_g_1);

                initialLevel.level_SEARCH_PARTNER_RESP.updateLevel(Users, SEARCH_PARTNER_RESP.name(), initialLevel.level_SEARCH_PARTNER, true);
                levelRepository.cache(initialLevel.level_SEARCH_PARTNER_RESP);
                Message message47_1_1 = new Message(initialLevel.level_SEARCH_PARTNER_RESP, Map.of(RU, "Результаты поиска партнеров"));
                messageRepository.cache(message47_1_1);
                initialLevel.level_SEARCH_PARTNER_RESP.addMessage(message47_1_1);

                initialLevel.level_SEARCH_PARTNER_RATE.updateLevel(Users, SEARCH_PARTNER_RATE.name(), initialLevel.level_SEARCH_PARTNER_RESP, true);
                levelRepository.cache(initialLevel.level_SEARCH_PARTNER_RATE);
                Message message47_2_1 = new Message(initialLevel.level_SEARCH_PARTNER_RATE, Map.of(RU, "Введите размер кэшбека в процентах по рекомендациям партнера"));
                messageRepository.cache(message47_2_1);
                initialLevel.level_SEARCH_PARTNER_RATE.addMessage(message47_2_1);

                initialLevel.level_SEARCH_PARTNER_LIMIT.updateLevel(Users, SEARCH_PARTNER_LIMIT.name(), initialLevel.level_SEARCH_PARTNER_RATE, true);
                levelRepository.cache(initialLevel.level_SEARCH_PARTNER_LIMIT);
                Message message47_3_1 = new Message(initialLevel.level_SEARCH_PARTNER_LIMIT, Map.of(RU, "Введите размер лимита для партнера"));
                messageRepository.cache(message47_3_1);
                initialLevel.level_SEARCH_PARTNER_LIMIT.addMessage(message47_3_1);

                initialLevel.level_SEARCH_PARTNER_END.updateLevel(Users, SEARCH_PARTNER_END.name(), initialLevel.level_SEARCH_PARTNER_LIMIT, true);
                levelRepository.cache(initialLevel.level_SEARCH_PARTNER_END);
                Message message471_3_1 = new Message(initialLevel.level_SEARCH_PARTNER_END, Map.of(RU, "Вы установили лимит WW для  EE"));
                messageRepository.cache(message471_3_1);
                initialLevel.level_SEARCH_PARTNER_END.addMessage(message471_3_1);
*/

                ///////выводится в цикле как результат поиска
                initialLevel.level_ADD_PARTNER.updateLevel(Users, ADD_PARTNER.name(), initialLevel.level_SEARCH_PARTNER_END, false);
                levelRepository.cache(initialLevel.level_ADD_PARTNER);
                ButtonRow row48_0 = new ButtonRow(initialLevel.level_ADD_PARTNER);
                buttonRowRepository.cache(row48_0);
                initialLevel.level_ADD_PARTNER.addRow(row48_0);
                Button button48_0_0 = new Button(row48_0, Map.of(RU, "Добавить партнера"), initialLevel.level_B2NOB.getIdString());
                buttonRepository.cache(button48_0_0);
                row48_0.add(button48_0_0);


                //CREATE GROUP
                initialLevel.level_CREATE_GROUP.updateLevel(Users, CREATE_GROUP.name(), initialLevel.level_PARTNERS, false);
                levelRepository.cache(initialLevel.level_CREATE_GROUP);
                Message message47_g_1 = new Message(initialLevel.level_CREATE_GROUP, Map.of(RU, "Введите название группы"));
                messageRepository.cache(message47_g_1);
                initialLevel.level_CREATE_GROUP.addMessage(message47_g_1);

                initialLevel.level_CREATE_GROUP_RESP.updateLevel(Users, CREATE_GROUP_RESP.name(), initialLevel.level_CREATE_GROUP, true);
                levelRepository.cache(initialLevel.level_CREATE_GROUP_RESP);
                Message message47_1_g_1 = new Message(initialLevel.level_CREATE_GROUP_RESP, Map.of(RU, "Введите размер лимита кэшбека по группе"));
                messageRepository.cache(message47_1_g_1);
                initialLevel.level_CREATE_GROUP_RESP.addMessage(message47_1_g_1);

                initialLevel.level_CREATE_GROUP_LIMIT.updateLevel(Users, CREATE_GROUP_LIMIT.name(), initialLevel.level_CREATE_GROUP_RESP, true);
                levelRepository.cache(initialLevel.level_CREATE_GROUP_LIMIT);
                Message message47_2_g_1 = new Message(initialLevel.level_CREATE_GROUP_LIMIT, Map.of(RU, "Вы создали группу магазинов"));
                messageRepository.cache(message47_2_g_1);
                initialLevel.level_CREATE_GROUP_LIMIT.addMessage(message47_2_g_1);





                ///////////////////////// СПИСОК АКЦИЙ МАГАЗИНА ACTIONS

                initialLevel.level_ACTIONS.updateLevel(Users, ACTIONS.name(), initialLevel.level_ADMIN_ADMIN, false);
                levelRepository.cache(initialLevel.level_ACTIONS);
                Message message25_1 = new Message(initialLevel.level_ACTIONS, Map.of(RU, "Список моих акций"));
                messageRepository.cache(message25_1);
                initialLevel.level_ACTIONS.addMessage(message25_1);
                ButtonRow row25_0 = new ButtonRow(initialLevel.level_ACTIONS);
                buttonRowRepository.cache(row25_0);
                Button button25_0_0 = new Button(row25_0, Map.of(RU, "Создать новую акцию"), initialLevel.level_ACTION_TYPE.getIdString());
                buttonRepository.cache(button25_0_0);
                row25_0.add(button25_0_0);
                initialLevel.level_ACTIONS.addRow(row25_0);


                initialLevel.level_ACTION_TYPE.updateLevel(Users, ACTION_TYPE.name(), initialLevel.level_ACTIONS, false);
                levelRepository.cache(initialLevel.level_ACTION_TYPE);
                Message message26_1 = new Message(initialLevel.level_ACTION_TYPE, Map.of(RU, "Выберите тип акции:"));
                messageRepository.cache(message26_1);
                initialLevel.level_ACTION_TYPE.addMessage(message26_1);
                ButtonRow row26_0 = new ButtonRow(initialLevel.level_ACTION_TYPE);
                buttonRowRepository.cache(row26_0);
                Button button26_0_0 = new Button(row26_0, Map.of(RU, ActionTypeEnum.BASIC_DEFAULT.getValue()), initialLevel.level_BASIC.getIdString());
                buttonRepository.cache(button26_0_0);
                row26_0.add(button26_0_0);
                initialLevel.level_ACTION_TYPE.addRow(row26_0);

                ButtonRow row26_1 = new ButtonRow(initialLevel.level_ACTION_TYPE);
                buttonRowRepository.cache(row26_1);
                Button button26_1_0 = new Button(row26_1, Map.of(RU, ActionTypeEnum.COUPON_DEFAULT.getValue()), initialLevel.level_COUPON.getIdString());
                buttonRepository.cache(button26_1_0);
                row26_1.add(button26_1_0);
                initialLevel.level_ACTION_TYPE.addRow(row26_1);

                ButtonRow row26_2 = new ButtonRow(initialLevel.level_ACTION_TYPE);
                buttonRowRepository.cache(row26_2);
                Button button26_2_0 = new Button(row26_2, Map.of(RU, ActionTypeEnum.LINK_TO_PRODUCT.getValue()), initialLevel.level_LINK_TO_PRODUCT.getIdString());
                buttonRepository.cache(button26_2_0);
                row26_2.add(button26_2_0);
                initialLevel.level_ACTION_TYPE.addRow(row26_2);

                ///BASIC
// проверил отсюда 12.05
                initialLevel.level_BASIC.updateLevel(Users, BASIC.name(), initialLevel.level_ACTION_TYPE, false);

                levelRepository.cache(initialLevel.level_BASIC);
                Message message27_0 = new Message(initialLevel.level_BASIC, Map.of(RU, "Введите название"));
                messageRepository.cache(message27_0);
                initialLevel.level_BASIC.addMessage(message27_0);


                initialLevel.level_SELECT_LEVEL_TYPE.updateLevel(Users, SELECT_LEVEL_TYPE.name(), initialLevel.level_BASIC, true);

                levelRepository.cache(initialLevel.level_SELECT_LEVEL_TYPE);
                Message message31_0 = new Message(initialLevel.level_SELECT_LEVEL_TYPE, Map.of(RU, "Использовать разные размеры скидки для различных уровней суммы покупок за прошлый месяц?"));
                messageRepository.cache(message31_0);
                initialLevel.level_SELECT_LEVEL_TYPE.addMessage(message31_0);
                ButtonRow row31_0 = new ButtonRow(initialLevel.level_SELECT_LEVEL_TYPE);
                buttonRowRepository.cache(row31_0);
                Button button31_0_0 = new Button(row31_0, Map.of(RU, "Нет"), initialLevel.level_ONE_LEVEL_RATE.getIdString());
                buttonRepository.cache(button31_0_0);
                row31_0.add(button31_0_0);
                initialLevel.level_SELECT_LEVEL_TYPE.addRow(row31_0);
                ButtonRow row31_1 = new ButtonRow(initialLevel.level_SELECT_LEVEL_TYPE);
                buttonRowRepository.cache(row31_1);
                Button button31_1_0 = new Button(row31_0, Map.of(RU, "Да, добавить уровень"), initialLevel.level_MULTI_ACTION_LEVEL.getIdString());
                buttonRepository.cache(button31_1_0);
                row31_1.add(button31_1_0);
                initialLevel.level_SELECT_LEVEL_TYPE.addRow(row31_1);

////////////////////////////////////////////////
                initialLevel.level_MULTI_ACTION_LEVEL.updateLevel(Users, MULTI_ACTION_LEVEL.name(), initialLevel.level_SELECT_LEVEL_TYPE, true);

                levelRepository.cache(initialLevel.level_MULTI_ACTION_LEVEL);
                Message message32_0 = new Message(initialLevel.level_MULTI_ACTION_LEVEL, Map.of(RU, "Введите размер уровня суммы"));
                messageRepository.cache(message32_0);
                initialLevel.level_MULTI_ACTION_LEVEL.addMessage(message32_0);

// используется как для перехода после кнопки ONE_ACTION_LEVEL, так и после отправки сообщения от MULTI_ACTION_LEVEL
                initialLevel.level_MULTI_LEVEL_RATE.updateLevel(Users, MULTI_LEVEL_RATE.name(), initialLevel.level_MULTI_ACTION_LEVEL, true);

                levelRepository.cache(initialLevel.level_MULTI_LEVEL_RATE);
                Message message28_0 = new Message(initialLevel.level_MULTI_LEVEL_RATE, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
                messageRepository.cache(message28_0);
                initialLevel.level_MULTI_LEVEL_RATE.addMessage(message28_0);
/////////////////////////////////////////////////
// Дублируется для зацикливания

                initialLevel.level_MULTI_LEVEL_QUESTION.updateLevel(Users, MULTI_LEVEL_QUESTION.name(), initialLevel.level_MULTI_LEVEL_RATE, true);

                levelRepository.cache(initialLevel.level_MULTI_LEVEL_QUESTION);
                Message message31_2_0 = new Message(initialLevel.level_MULTI_LEVEL_QUESTION, Map.of(RU, "Добавить уровень суммы покупок за прошлый месяц?"));
                messageRepository.cache(message31_2_0);
                initialLevel.level_MULTI_LEVEL_QUESTION.addMessage(message31_2_0);
                ButtonRow row31_2_0 = new ButtonRow(initialLevel.level_MULTI_LEVEL_QUESTION);
                buttonRowRepository.cache(row31_2_0);
                Button button31_2_0_0 = new Button(row31_2_0, Map.of(RU, "Нет"), initialLevel.level_ACTION_RATE_WITHDRAW.getIdString());
                buttonRepository.cache(button31_2_0_0);
                row31_2_0.add(button31_2_0_0);
                initialLevel.level_MULTI_LEVEL_QUESTION.addRow(row31_2_0);
                ButtonRow row31_2_1 = new ButtonRow(initialLevel.level_MULTI_LEVEL_QUESTION);
                buttonRowRepository.cache(row31_2_1);
                Button button31_2_1_0 = new Button(row31_2_0, Map.of(RU, "Да"), initialLevel.level_MULTI_ACTION_LEVEL.getIdString());
                buttonRepository.cache(button31_2_1_0);
                row31_2_1.add(button31_2_1_0);
                initialLevel.level_MULTI_LEVEL_QUESTION.addRow(row31_2_1);
// проверил досюда 12.05
///////////////////////////////////////////////////////////////////////////
                initialLevel.level_ONE_LEVEL_RATE.updateLevel(Users, ONE_LEVEL_RATE.name(), initialLevel.level_SELECT_LEVEL_TYPE, true);

                levelRepository.cache(initialLevel.level_ONE_LEVEL_RATE);
                Message message28_2_0 = new Message(initialLevel.level_ONE_LEVEL_RATE, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
                messageRepository.cache(message28_2_0);
                initialLevel.level_ONE_LEVEL_RATE.addMessage(message28_2_0);

                initialLevel.level_ACTION_RATE_WITHDRAW.updateLevel(Users, ACTION_RATE_WITHDRAW.name(), initialLevel.level_ONE_LEVEL_RATE, true);

                levelRepository.cache(initialLevel.level_ACTION_RATE_WITHDRAW);
                Message message29_0 = new Message(initialLevel.level_ACTION_RATE_WITHDRAW, Map.of(RU, "Введите в % максимальную долю списания кэшбека в стоимости последуюшей покупке"));
                messageRepository.cache(message29_0);
                initialLevel.level_ACTION_RATE_WITHDRAW.addMessage(message29_0);


                initialLevel.level_ACTION_RATE_PARTNER.updateLevel(Users, ACTION_RATE_PARTNER.name(), initialLevel.level_ACTION_RATE_WITHDRAW, true);

                levelRepository.cache(initialLevel.level_ACTION_RATE_PARTNER);
                Message message30_0 = new Message(initialLevel.level_ACTION_RATE_PARTNER, Map.of(RU, "Введите в % размер кешбека для партнера"));
                messageRepository.cache(message30_0);
                initialLevel.level_ACTION_RATE_PARTNER.addMessage(message30_0);

                initialLevel.level_ADD_ACTION_RATE_SOURCE.updateLevel(Users, ADD_ACTION_RATE_SOURCE.name(), initialLevel.level_ACTION_RATE_PARTNER, true);

                levelRepository.cache(initialLevel.level_ADD_ACTION_RATE_SOURCE);
                Message message38_0 = new Message(initialLevel.level_ADD_ACTION_RATE_SOURCE, Map.of(RU, "Выберите категорию либо товар для акции"));
                messageRepository.cache(message38_0);
                initialLevel.level_ADD_ACTION_RATE_SOURCE.addMessage(message38_0);

                menuCreator.createMenu(initialLevel.level_ADD_ACTION_RATE_SOURCE, MenuTypeEnum.LEVEL_CHOICER, Users);


                initialLevel.level_ADD_ACTION_RATE_TARGET.updateLevel(Users, ADD_ACTION_RATE_TARGET.name(), initialLevel.level_ADD_ACTION_RATE_SOURCE, true);

                levelRepository.cache(initialLevel.level_ADD_ACTION_RATE_TARGET);
                Message message39_0 = new Message(initialLevel.level_ADD_ACTION_RATE_TARGET, Map.of(RU, "Выберите категорию либо товар, на который можно тратить кешбек"));
                messageRepository.cache(message39_0);
                initialLevel.level_ADD_ACTION_RATE_TARGET.addMessage(message39_0);

                menuCreator.createMenu(initialLevel.level_ADD_ACTION_RATE_TARGET, MenuTypeEnum.LEVEL_CHOICER, Users);

                ////COUPON

                initialLevel.level_COUPON.updateLevel(Users, COUPON.name(), initialLevel.level_ACTION_TYPE, false);

                levelRepository.cache(initialLevel.level_COUPON);
                Message message33_0 = new Message(initialLevel.level_COUPON, Map.of(RU, "Введите название"));
                messageRepository.cache(message33_0);
                initialLevel.level_COUPON.addMessage(message33_0);


                initialLevel.level_COUPON_NUMBER.updateLevel(Users, COUPON_NUMBER.name(), initialLevel.level_COUPON, true);

                levelRepository.cache(initialLevel.level_COUPON_NUMBER);
                Message message34_0 = new Message(initialLevel.level_COUPON_NUMBER, Map.of(RU, "Введите количество товара, после покупки которого начнет действовать скидка"));
                messageRepository.cache(message34_0);
                initialLevel.level_COUPON_NUMBER.addMessage(message34_0);


                initialLevel.level_COUPON_RATE_WITHDRAW.updateLevel(Users, COUPON_RATE_WITHDRAW.name(), initialLevel.level_COUPON_NUMBER, true);

                levelRepository.cache(initialLevel.level_COUPON_RATE_WITHDRAW);
                Message message35_0 = new Message(initialLevel.level_COUPON_RATE_WITHDRAW, Map.of(RU, "Введите в % размер скидки, например 100"));
                messageRepository.cache(message35_0);
                initialLevel.level_COUPON_RATE_WITHDRAW.addMessage(message35_0);


                initialLevel.level_ADD_ACTION_COUPON_SOURCE.updateLevel(Users, ADD_ACTION_COUPON_SOURCE.name(), initialLevel.level_COUPON_RATE_WITHDRAW, true);

                levelRepository.cache(initialLevel.level_ADD_ACTION_COUPON_SOURCE);
                Message message40_0 = new Message(initialLevel.level_ADD_ACTION_COUPON_SOURCE, Map.of(RU, "Выберите категорию либо товар для акции"));
                messageRepository.cache(message40_0);
                initialLevel.level_ADD_ACTION_COUPON_SOURCE.addMessage(message40_0);


                menuCreator.createMenu(initialLevel.level_ADD_ACTION_COUPON_SOURCE, MenuTypeEnum.LEVEL_CHOICER, Users);

                initialLevel.level_ADD_ACTION_COUPON_TARGET.updateLevel(Users, ADD_ACTION_COUPON_TARGET.name(), initialLevel.level_ADD_ACTION_COUPON_SOURCE, true);

                levelRepository.cache(initialLevel.level_ADD_ACTION_COUPON_TARGET);
                Message message41_0 = new Message(initialLevel.level_ADD_ACTION_COUPON_TARGET, Map.of(RU, "Выберите категорию либо товар, который можно получить по купону"));
                messageRepository.cache(message41_0);
                initialLevel.level_ADD_ACTION_COUPON_TARGET.addMessage(message41_0);

                menuCreator.createMenu(initialLevel.level_ADD_ACTION_COUPON_TARGET, MenuTypeEnum.LEVEL_CHOICER, Users);


                ////LINK_TO_PRODUCT

                initialLevel.level_LINK_TO_PRODUCT.updateLevel(Users, LINK_TO_PRODUCT.name(), initialLevel.level_ACTION_TYPE, true);

                levelRepository.cache(initialLevel.level_LINK_TO_PRODUCT);
                Message message36_0 = new Message(initialLevel.level_LINK_TO_PRODUCT, Map.of(RU, "Введите название"));
                messageRepository.cache(message36_0);
                initialLevel.level_LINK_TO_PRODUCT.addMessage(message36_0);


                initialLevel.level_LINK_TO_PRODUCT_NUMBER.updateLevel(Users, LINK_TO_PRODUCT_NUMBER.name(), initialLevel.level_LINK_TO_PRODUCT, true);

                levelRepository.cache(initialLevel.level_LINK_TO_PRODUCT_NUMBER);
                Message message37_0 = new Message(initialLevel.level_LINK_TO_PRODUCT_NUMBER, Map.of(RU, "Введите количество товара, после покупки которого начнет действовать скидка"));
                messageRepository.cache(message37_0);
                initialLevel.level_LINK_TO_PRODUCT_NUMBER.addMessage(message37_0);


                initialLevel.level_ADD_ACTION_LINK_SOURCE.updateLevel(Users, ADD_ACTION_LINK_SOURCE.name(), initialLevel.level_LINK_TO_PRODUCT_NUMBER, true);

                levelRepository.cache(initialLevel.level_ADD_ACTION_LINK_SOURCE);
                Message message42_0 = new Message(initialLevel.level_ADD_ACTION_LINK_SOURCE, Map.of(RU, "Выберите категорию либо товар - главный"));
                messageRepository.cache(message42_0);
                initialLevel.level_ADD_ACTION_LINK_SOURCE.addMessage(message42_0);

                menuCreator.createMenu(initialLevel.level_ADD_ACTION_LINK_SOURCE, MenuTypeEnum.LEVEL_CHOICER, Users);

                initialLevel.level_ADD_ACTION_LINK_TARGET.updateLevel(Users, ADD_ACTION_LINK_TARGET.name(), initialLevel.level_ADD_ACTION_LINK_SOURCE, true);

                levelRepository.cache(initialLevel.level_ADD_ACTION_LINK_TARGET);
                Message message43_0 = new Message(initialLevel.level_ADD_ACTION_LINK_TARGET, Map.of(RU, "Выберите категорию либо товар, который можно получить по скидке в паре"));
                messageRepository.cache(message43_0);
                initialLevel.level_ADD_ACTION_LINK_TARGET.addMessage(message43_0);

                menuCreator.createMenu(initialLevel.level_ADD_ACTION_LINK_TARGET, MenuTypeEnum.LEVEL_CHOICER, Users);

                //////////МАГАЗИНЫ ДЛЯ ПРОДАВЦА

                initialLevel.level_ADMIN_SHOPS.updateLevel(Users, ADMIN_SHOPS.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_ADMIN_SHOPS);
                Message message01_1 = new Message(initialLevel.level_ADMIN_SHOPS, Map.of(RU, "Список моих магазинов"));
                messageRepository.cache(message01_1);
                initialLevel.level_ADMIN_SHOPS.addMessage(message01_1);

                ///////////////////////// СПИСОК ТОВАРОВ

                initialLevel.level_GOODS_LIST.updateLevel(Users, GOODS_LIST.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_GOODS_LIST);
                Message message52_1 = new Message(initialLevel.level_GOODS_LIST, Map.of(RU, "Список моих товаров"));
                messageRepository.cache(message52_1);
                initialLevel.level_GOODS_LIST.addMessage(message52_1);
                Message message52_2_1 = new Message(initialLevel.level_GOODS_LIST, 0,
                        Map.of(RU, "Вы можете загрузить список товаров файл Excel, " +
                                "для этого скачайте по ссылке http://skidozona.by, " +
                                "отредактируйте и загрузите на сервер, прикрепив документ"));
                messageRepository.cache(message52_2_1);
                initialLevel.level_GOODS_LIST.addMessage(message52_2_1);
                ButtonRow row52_0 = new ButtonRow(initialLevel.level_GOODS_LIST);
                buttonRowRepository.cache(row52_0);
                Button button52_0_0 = new Button(row52_0, Map.of(RU, "Добавить товары через бот"), initialLevel.level_ADD_GOODS.getIdString());
                buttonRepository.cache(button52_0_0);
                row52_0.add(button52_0_0);
                initialLevel.level_GOODS_LIST.addRow(row52_0);

                /////menuCreator.createMenu(initialLevel.level_GOODS_LIST, MenuTypeEnum.SEARCH);


                initialLevel.level_SHOP_BOTS.updateLevel(Users, SHOP_BOTS.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_SHOP_BOTS);
                Message message52_12 = new Message(initialLevel.level_SHOP_BOTS, Map.of(RU, "Бот: "));
                messageRepository.cache(message52_12);
                initialLevel.level_SHOP_BOTS.addMessage(message52_12);

                ButtonRow row0_11_0 = new ButtonRow(initialLevel.level_SHOP_BOTS);
                buttonRowRepository.cache(row0_11_0);
                Button button0_11_0 = new Button(row0_11_0, Map.of(RU, "Редактировать"), initialLevel.level_EDIT_BOT.getIdString());
                buttonRepository.cache(button0_11_0);

                ButtonRow row0_11_1 = new ButtonRow(initialLevel.level_SHOP_BOTS);
                buttonRowRepository.cache(row0_11_1);
                Button button0_11_1 = new Button(row0_11_1, Map.of(RU, "Просмотр бота"), initialLevel.level_LOOK_BOT.getIdString());
                buttonRepository.cache(button0_11_1);

                ButtonRow row0_11_2 = new ButtonRow(initialLevel.level_SHOP_BOTS);
                buttonRowRepository.cache(row0_11_2);
                Button button0_11_2 = new Button(row0_11_2, Map.of(RU, "Выбрать другой шаблон бота"), initialLevel.level_ADD_BOT.getIdString());
                buttonRepository.cache(button0_11_2);


                initialLevel.level_EDIT_BOT.updateLevel(Users, EDIT_BOT.name(), initialLevel.level_SHOP_BOTS, false);

                levelRepository.cache(initialLevel.level_EDIT_BOT);



                initialLevel.level_LOOK_BOT.updateLevel(Users, EDIT_BOT.name(), initialLevel.level_SHOP_BOTS, false);

                levelRepository.cache(initialLevel.level_LOOK_BOT);




                //ЗДЕСЬ ВЫХОДИТ СПИСОК БОТОВ
                initialLevel.level0_1_1.updateLevel(Users, "level0_1_1", initialLevel.level_INITIALIZE, false);
//                level.addMessage(message);
//                ButtonRow row = new ArrayList<>();
//                Button button = new Button(level, "Ответить " + buyerChat.getOwner().getBuyer().getName(), RESPONSE_BUYER_MESSAGE.name() + buyerChatId);
//                row.add(button);
//                level.addRow(row);

                initialLevel.level0_1_3.updateLevel(Users, "level0_1_3", initialLevel.level_INITIALIZE, false);//true
                initialLevel.level0_1_3.addMessage(new Message(null, Map.of(RU, "Отправлено!")));

                initialLevel.level_RESPONSE_BUYER_MESSAGE.updateLevel(Users, RESPONSE_BUYER_MESSAGE.name(), initialLevel.level_INITIALIZE, false);
                levelRepository.cache(initialLevel.level_RESPONSE_BUYER_MESSAGE);

                initialLevel.level_RESPONSE_SHOP_MESSAGE.updateLevel(Users, RESPONSE_SHOP_MESSAGE.name(), initialLevel.level_INITIALIZE, false);
                levelRepository.cache(initialLevel.level_RESPONSE_SHOP_MESSAGE);

                initialLevel.level_NON_RESPONSE.updateLevel(Users, NON_RESPONSE.name(), initialLevel.level_INITIALIZE, false);
                levelRepository.cache(initialLevel.level_NON_RESPONSE);

                initialLevel.level0_1_4.updateLevel(Users, SEND_SHOP_MESSAGE.name(), initialLevel.level0_1_4, true);
                levelRepository.cache(initialLevel.level0_1_4);

                initialLevel.level0_1_5.updateLevel(Users, SEND_BUYER_MESSAGE.name(), initialLevel.level0_1_5, true);
                levelRepository.cache(initialLevel.level0_1_5);


                //////////СОЗДАНИЕ ВВОДА ТОВАРОВ
// проверил 12.05 отсюда
                initialLevel.level_ADD_GOODS.updateLevel(Users, ADD_GOODS.name(), initialLevel.level_GOODS_LIST, false);

                levelRepository.cache(initialLevel.level_ADD_GOODS);
                Message message7_0 = new Message(initialLevel.level_ADD_GOODS, 0,
                        Map.of(RU,
                                "Вы можете добавлять товары, просто загрузив в бот файлом Excel http://skidozona.by, а также" +
                                        " в панели управления на сайте, перейдя по этой ссылке,"));
                messageRepository.cache(message7_0);
                initialLevel.level_ADD_GOODS.addMessage(message7_0);

                Message message7_3 = new Message(initialLevel.level_ADD_GOODS, 1, Map.of(RU, "https://skidozona.by/admin/id=22222"));
                messageRepository.cache(message7_3);
                initialLevel.level_ADD_GOODS.addMessage(message7_3);

                ButtonRow row7_0 = new ButtonRow(initialLevel.level_ADD_GOODS);
                buttonRowRepository.cache(row7_0);

                initialLevel.level_ADD_GOODS.addRow(row7_0);
                Button button7_0_2 = new Button(row7_0, Map.of(RU, "Добавить ещё"), initialLevel.level_GOODS_LIST.getIdString());
                buttonRepository.cache(button7_0_2);
                row7_0.add(button7_0_2);

                menuCreator.createMenu(initialLevel.level_ADD_GOODS, MenuTypeEnum.ADD_GOODS, Users);

                initialLevel.level_ADD_GOODS_NAME.updateLevel(Users, ADD_GOODS_NAME.name(), initialLevel.level_ADD_GOODS, false);

                levelRepository.cache(initialLevel.level_ADD_GOODS_NAME);
                Message message14_2 = new Message(initialLevel.level_ADD_GOODS_NAME, Map.of(RU, "Введите название"));
                messageRepository.cache(message14_2);
                initialLevel.level_ADD_GOODS_NAME.addMessage(message14_2);

                initialLevel.level_ADD_GOODS_PHOTO.updateLevel(Users, ADD_GOODS_PHOTO.name(), initialLevel.level_ADD_GOODS_NAME, true);

                levelRepository.cache(initialLevel.level_ADD_GOODS_PHOTO);
                Message message8_1 = new Message(initialLevel.level_ADD_GOODS_PHOTO, Map.of(RU, "Пришлите фото"));
                messageRepository.cache(message8_1);
                initialLevel.level_ADD_GOODS_PHOTO.addMessage(message8_1);

                initialLevel.level_ADD_GOODS_DESCRIPTION.updateLevel(Users, ADD_GOODS_DESCRIPTION.name(), initialLevel.level_ADD_GOODS_PHOTO, true);

                levelRepository.cache(initialLevel.level_ADD_GOODS_DESCRIPTION);
                Message message9_1 = new Message(initialLevel.level_ADD_GOODS_DESCRIPTION, Map.of(RU, "Пришлите описание"));
                messageRepository.cache(message9_1);
                initialLevel.level_ADD_GOODS_DESCRIPTION.addMessage(message9_1);

                initialLevel.level_ADD_GOODS_PRICE.updateLevel(Users, ADD_GOODS_PRICE.name(), initialLevel.level_ADD_GOODS_DESCRIPTION, true);

                levelRepository.cache(initialLevel.level_ADD_GOODS_PRICE);
                Message message10_1 = new Message(initialLevel.level_ADD_GOODS_PRICE, Map.of(RU, "Пришлите цену"));
                messageRepository.cache(message10_1);
                initialLevel.level_ADD_GOODS_PRICE.addMessage(message10_1);


                initialLevel.level_ADD_GOODS_END.updateLevel(Users, ADD_GOODS_END.name(), initialLevel.level_ADD_GOODS_PRICE, true);

                levelRepository.cache(initialLevel.level_ADD_GOODS_END);
                Message message010_1 = new Message(initialLevel.level_ADD_GOODS_END, Map.of(RU, "Сохранено"));
                messageRepository.cache(message010_1);
                initialLevel.level_ADD_GOODS_END.addMessage(message010_1);
// проверил досюда 12.05
                //////////ДОБАВИТЬ БОТ

                initialLevel.level_ADD_BOT.updateLevel(Users, ADD_BOT.name(), initialLevel.level_CONSTRUCT_ADD, false);

                levelRepository.cache(initialLevel.level_ADD_BOT);
                Message message16_1 = new Message(initialLevel.level_ADD_BOT, Map.of(RU, "Выберите шаблон бота:"));
                messageRepository.cache(message16_1);
                initialLevel.level_ADD_BOT.addMessage(message16_1);
                ButtonRow row16_0 = new ButtonRow(initialLevel.level_ADD_BOT);
                buttonRowRepository.cache(row16_0);
                initialLevel.level_ADD_BOT.addRow(row16_0);
                Button button16_0_0 = new Button(row16_0, Map.of(RU, "Такси бот"), initialLevel.level_ADD_TAXI_BOT.getIdString());
                buttonRepository.cache(button16_0_0);
//                row16_0.add(button16_0_0);

//                Button button16_0_01 = new Button(row16_0, "Выбрать шаблон и перейти к редактированию", initialLevel.level_ADD_TAXI_BOT.getIdString());
//                buttonRepository.cache(button16_0_01);
//                row16_0.add(button16_0_01);
//
//
//                    Button button16_0_1 = new Button(initialLevel.level_ADD_BOT, "Парикмахер бот", initialLevel.level_PARIK_BOT.getIdString());
//                    buttonRepository.cache(button16_0_1);
//                    row16_0.add(button16_0_1);
//                    initialLevel.level_ADD_BOT.addRow(row16_0);
//                    ButtonRow row16_1 = new ArrayList<>();
//                    Button button16_1_0 = new Button(initialLevel.level_ADD_BOT, "Магазин одежды", initialLevel.level_CLOTHES_BOT.getIdString());
//                    buttonRepository.cache(button16_1_0);
//                    row16_1.add(button16_1_0);
//                    Button button16_1_1 = new Button(initialLevel.level_ADD_BOT, "Онлайн-курсы", initialLevel.level_COURSES_BOT.getIdString());
//                    buttonRepository.cache(button16_1_1);
//                    row16_1.add(button16_1_1);
//                    initialLevel.level_ADD_BOT.addRow(row16_1);
                //////////ШАБЛОН БОТА ТАКСИ

                initialLevel.level_ADD_TAXI_BOT.updateLevel(Users, ADD_TAXI_BOT.name(), initialLevel.level_ADD_BOT, false, false, true);

                levelRepository.cache(initialLevel.level_ADD_TAXI_BOT);
                Message message17_1 = new Message(initialLevel.level_ADD_TAXI_BOT, Map.of(RU, "Здравствуйте! Пришлите геолокацию, куда необходимо подать машину:"));
                messageRepository.cache(message17_1);
                initialLevel.level_ADD_TAXI_BOT.addMessage(message17_1);
                Message message17_2 = new Message(initialLevel.level_ADD_TAXI_BOT, 1, null,
                        IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream(PHOTO_FILE)),
                        "как отправить геолокацию");
                messageRepository.cache(message17_2);
                initialLevel.level_ADD_TAXI_BOT.addMessage(message17_2);


                initialLevel.level_TAXI_LOCATION.updateLevel(Users, TAXI_LOCATION.name(), initialLevel.level_ADD_TAXI_BOT, true, false, true);

                levelRepository.cache(initialLevel.level_TAXI_LOCATION);
                Message message18_1 = new Message(initialLevel.level_TAXI_LOCATION, Map.of(RU, "Отлично! машина может прибыть через 11 минут"));
                messageRepository.cache(message18_1);
                initialLevel.level_TAXI_LOCATION.addMessage(message18_1);
                ButtonRow row191_0 = new ButtonRow(initialLevel.level_TAXI_LOCATION);
                buttonRowRepository.cache(row191_0);
                initialLevel.level_TAXI_LOCATION.addRow(row191_0);
                Button button191_0_0 = new Button(row191_0, Map.of(RU, "Подтвердите заказ такси!"), initialLevel.level_TAXI_LOCATION.getIdString());
                buttonRepository.cache(button191_0_0);
                row191_0.add(button191_0_0);


                initialLevel.level_TAXI_SUBMIT.updateLevel(Users, TAXI_SUBMIT.name(), initialLevel.level_TAXI_LOCATION, true, true, true);

                levelRepository.cache(initialLevel.level_TAXI_SUBMIT);
                Message message191_1 = new Message(initialLevel.level_TAXI_SUBMIT, Map.of(RU, "Ваша заявка принята!"));
                messageRepository.cache(message191_1);
                initialLevel.level_TAXI_SUBMIT.addMessage(message191_1);

//                        ButtonRow row18_0 = new ArrayList<>();
//                        Button button18_0_0 = new Button(initialLevel.level_ADD_TAXI_BOT, "Отправить заявку", initialLevel.level_SUBMIT_BOT.getIdString());
//                        buttonRepository.cache(button18_0_0);
//                        row18_0.add(button18_0_0);
//                        initialLevel.level_ADD_TAXI_BOT.addRow(row18_0);
//
//                        ButtonRow row18_0 = new ArrayList<>();
//                        Button button18_0_0 = new Button(initialLevel.level_TAXI_LOCATION, "Выбрать шаблон и перейти к редактированию", ADD_TAXI_BOT.name());
//                        buttonRepository.cache(button18_0_0);
//                        row18_0.add(button18_0_0);
//                        initialLevel.level_TAXI_LOCATION.addRow(row18_0);
//                        ButtonRow row18_1 = new ArrayList<>();
//                        Button button18_1_0 = new Button(initialLevel.level_TAXI_LOCATION, "Вернуться к выбору шаблона", ADD_BOT.name());
//                        buttonRepository.cache(button18_1_0);
//                        row18_1.add(button18_1_0);
//                        initialLevel.level_TAXI_LOCATION.addRow(row18_1);


                initialLevel.level_EDIT_BUTTON_NAME.updateLevel(Users, EDIT_BUTTON_NAME.name(), initialLevel.level_TAXI_LOCATION, false);
                //initialLevel.level_EDIT_BUTTON_NAME.setBotLevel(true);
                levelRepository.cache(initialLevel.level_EDIT_BUTTON_NAME);
                Message message = new Message(initialLevel.level_EDIT_BUTTON_NAME, Map.of(RU, "Введите новое название кнопки"));
                messageRepository.cache(message);
                initialLevel.level_EDIT_BUTTON_NAME.addMessage(message);

                initialLevel.level_EDIT_MESSAGE.updateLevel(Users, EDIT_MESSAGE.name(), initialLevel.level_TAXI_LOCATION, false);
                //initialLevel.level_EDIT_MESSAGE.setBotLevel(true);
                levelRepository.cache(initialLevel.level_EDIT_MESSAGE);
                Message addMessage = new Message(initialLevel.level_EDIT_MESSAGE, Map.of(RU, "Введите новый текст сообщения"));
                messageRepository.cache(addMessage);
                initialLevel.level_EDIT_MESSAGE.addMessage(addMessage);

                initialLevel.level_NEW_LEVEL_BUTTON.updateLevel(Users, NEW_LEVEL_BUTTON.name(), initialLevel.level_TAXI_LOCATION, false);
                //initialLevel.level_NEW_LEVEL_BUTTON.setBotLevel(true);
                levelRepository.cache(initialLevel.level_NEW_LEVEL_BUTTON);
                Message addMessage2 = new Message(initialLevel.level_NEW_LEVEL_BUTTON, Map.of(RU, "Введите название кнопки для перехода"));
                messageRepository.cache(addMessage2);
                initialLevel.level_NEW_LEVEL_BUTTON.addMessage(addMessage2);

                initialLevel.level_NEW_LEVEL_INPUT_BUTTON.updateLevel(Users, NEW_LEVEL_INPUT_BUTTON.name(), initialLevel.level_TAXI_LOCATION, false);
                //initialLevel.level_NEW_LEVEL_INPUT_BUTTON.setBotLevel(true);
                levelRepository.cache(initialLevel.level_NEW_LEVEL_INPUT_BUTTON);
                Message addMessage3 = new Message(initialLevel.level_NEW_LEVEL_INPUT_BUTTON, Map.of(RU, "Введите название кнопки для перехода"));
                messageRepository.cache(addMessage3);
                initialLevel.level_NEW_LEVEL_INPUT_BUTTON.addMessage(addMessage3);

                initialLevel.level_NEW_LEVEL_END_BUTTON.updateLevel(Users, NEW_LEVEL_END_BUTTON.name(), initialLevel.level_TAXI_LOCATION, false);
                //initialLevel.level_NEW_LEVEL_END_BUTTON.setBotLevel(true);
                levelRepository.cache(initialLevel.level_NEW_LEVEL_END_BUTTON);
                Message addMessage4 = new Message(initialLevel.level_NEW_LEVEL_END_BUTTON, Map.of(RU, "Введите название кнопки для перехода"));
                messageRepository.cache(addMessage4);
                initialLevel.level_NEW_LEVEL_END_BUTTON.addMessage(addMessage4);



                initialLevel.level_NEGATIVE_SUM.updateLevel(Users, NEGATIVE_SUM.name(), initialLevel.level_P2B, true);

                levelRepository.cache(initialLevel.level_NEGATIVE_SUM);
                Message message22_000 = new Message(initialLevel.level_NEGATIVE_SUM, Map.of(RU, "Сумма должна быть положительной"));
                messageRepository.cache(message22_000);
                initialLevel.level_NEGATIVE_SUM.addMessage(message22_000);

                initialLevel.level_NEGATIVE_COUNT.updateLevel(Users, NEGATIVE_COUNT.name(), initialLevel.level_P2B, true);

                levelRepository.cache(initialLevel.level_NEGATIVE_COUNT);
                Message message22_111 = new Message(initialLevel.level_NEGATIVE_COUNT, Map.of(RU, "Количество должно быть положительным"));
                messageRepository.cache(message22_111);
                initialLevel.level_NEGATIVE_COUNT.addMessage(message22_111);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ BI -И ПОЛУЧАТЕЛь BI
                //ЕСЛИ УЖЕ ЕСТЬ ПАРТНЕР
                initialLevel.level_B2B.updateLevel(Users, B2B.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_B2B);
                Message message24_1_1 = new Message(initialLevel.level_B2B, Map.of(RU, "Баланс кешбека с партнером X - вы/он должен Y. Списать кэбек?"));
                messageRepository.cache(message24_1_1);
                initialLevel.level_B2B.addMessage(message24_1_1);

                ButtonRow row24_1_0 = new ButtonRow(initialLevel.level_B2B);
                buttonRowRepository.cache(row24_1_0);
                Button button24_1_0_0 = new Button(row24_1_0, Map.of(RU, "Списать кэбек партнера?/Списать Ваш кэбек?"), initialLevel.level_APPROVE_NEW_PARTNER.getIdString());
                buttonRepository.cache(button24_1_0_0);
                row24_1_0.add(button24_1_0_0);
                Button button24_1_0_1 = new Button(row24_1_0, Map.of(RU, "Нет"), initialLevel.level_DISCARD_NEW_PARTNER.getIdString());
                buttonRepository.cache(button24_1_0_1);
                row24_1_0.add(button24_1_0_1);
                initialLevel.level_B2B.addRow(row24_1_0);


                //ЕСЛИ НОВЫЙ ПАРТНЕР
                initialLevel.level_B2NOB.updateLevel(Users, B2NOB.name(), initialLevel.level_INITIALIZE, false);
                levelRepository.cache(initialLevel.level_B2NOB);
                Message message24_1 = new Message(initialLevel.level_B2NOB, Map.of(RU, "Добавить X  партнёром?"));
                messageRepository.cache(message24_1);
                initialLevel.level_B2NOB.addMessage(message24_1);


                ButtonRow row24_0 = new ButtonRow(initialLevel.level_B2NOB);
                buttonRowRepository.cache(row24_0);
                Button button24_0_0 = new Button(row24_0, Map.of(RU, "Да"), initialLevel.level_APPROVE_NEW_PARTNER.getIdString());
                buttonRepository.cache(button24_0_0);
                row24_0.add(button24_0_0);
                initialLevel.level_B2NOB.addRow(row24_0);
                Button button24_0_1 = new Button(row24_0, Map.of(RU, "Нет"), initialLevel.level_DISCARD_NEW_PARTNER.getIdString());
                buttonRepository.cache(button24_0_1);
                row24_0.add(button24_0_1);
                initialLevel.level_B2NOB.addRow(row24_0);

                initialLevel.level_DISCARD_NEW_PARTNER.updateLevel(Users, DISCARD_NEW_PARTNER.name(), initialLevel.level_B2NOB, false);
                levelRepository.cache(initialLevel.level_DISCARD_NEW_PARTNER);
                Message message44_1 = new Message(initialLevel.level_DISCARD_NEW_PARTNER, Map.of(RU, "Запрос отклонен"));
                messageRepository.cache(message44_1);
                initialLevel.level_DISCARD_NEW_PARTNER.addMessage(message44_1);


                initialLevel.level_APPROVE_NEW_PARTNER.updateLevel(Users, APPROVE_NEW_PARTNER.name(), initialLevel.level_B2NOB, false);

                levelRepository.cache(initialLevel.level_APPROVE_NEW_PARTNER);
                Message message45_1 = new Message(initialLevel.level_APPROVE_NEW_PARTNER, Map.of(RU, "Введите размер скидки для партнера при переходе по рекомендации"));
                messageRepository.cache(message45_1);
                initialLevel.level_APPROVE_NEW_PARTNER.addMessage(message45_1);


//                initialLevel.level_APPROVE_NEW_PARTNER.updateLevel(Users, APPROVE_NEW_PARTNER.name(), initialLevel.level_B2NOB, false);
//
//                levelRepository.cache(initialLevel.level_APPROVE_NEW_PARTNER);
//                Message message45_1 = new Message(initialLevel.level_APPROVE_NEW_PARTNER, Map.of(LanguageEnum.ru, "Введите размер скидки для партнера при переходе по рекомендации"));
//                messageRepository.cache(message45_1);
//                initialLevel.level_APPROVE_NEW_PARTNER.addMessage(message45_1);


                //ЕСЛИ УЖЕ НОВАЯ ГРУППА
                initialLevel.level_NEW_GRUPP.updateLevel(Users, NEW_GRUPP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_NEW_GRUPP);
                Message message49_1 = new Message(initialLevel.level_NEW_GRUPP, Map.of(RU, "Добавить Группу W?"));
                messageRepository.cache(message49_1);
                initialLevel.level_NEW_GRUPP.addMessage(message49_1);

                ButtonRow row49_0 = new ButtonRow(initialLevel.level_NEW_GRUPP);
                buttonRowRepository.cache(row49_0);
                Button button49_0_0 = new Button(row49_0, Map.of(RU, "Да"), initialLevel.level_APPROVE_NEW_GRUPP.getIdString());
                buttonRepository.cache(button49_0_0);
                row49_0.add(button49_0_0);
                initialLevel.level_NEW_GRUPP.addRow(row49_0);
                Button button49_0_1 = new Button(row49_0, Map.of(RU, "Нет"), initialLevel.level_DISCARD_NEW_GRUPP.getIdString());
                buttonRepository.cache(button49_0_1);
                row49_0.add(button49_0_1);
                initialLevel.level_NEW_GRUPP.addRow(row49_0);

                initialLevel.level_DISCARD_NEW_GRUPP.updateLevel(Users, DISCARD_NEW_GRUPP.name(), initialLevel.level_NEW_GRUPP, false);

                levelRepository.cache(initialLevel.level_DISCARD_NEW_GRUPP);
                Message message50_1 = new Message(initialLevel.level_DISCARD_NEW_GRUPP, Map.of(RU, "Запрос отклонен"));
                messageRepository.cache(message50_1);
                initialLevel.level_DISCARD_NEW_GRUPP.addMessage(message50_1);


                initialLevel.level_APPROVE_NEW_GRUPP.updateLevel(Users, APPROVE_NEW_GRUPP.name(), initialLevel.level_NEW_GRUPP, false);

                levelRepository.cache(initialLevel.level_APPROVE_NEW_GRUPP);
                Message message51_1 = new Message(initialLevel.level_APPROVE_NEW_GRUPP, Map.of(RU, "Введите лимит выплаты кэшбека группы"));
                messageRepository.cache(message51_1);
                initialLevel.level_APPROVE_NEW_GRUPP.addMessage(message51_1);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private boolean isBotLevel = false;

}
