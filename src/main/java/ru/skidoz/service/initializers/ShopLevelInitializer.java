package ru.skidoz.service.initializers;


import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.command.CommandName.ACTIONS;
import static ru.skidoz.service.command.CommandName.ACTION_RATE_PARTNER;
import static ru.skidoz.service.command.CommandName.ACTION_RATE_WITHDRAW;
import static ru.skidoz.service.command.CommandName.ACTION_RATE_WITHDRAW_BASIC;
import static ru.skidoz.service.command.CommandName.ACTION_TYPE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_COUPON_SOURCE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_COUPON_TARGET;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_LINK_SOURCE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_LINK_TARGET;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_RATE_SOURCE;
import static ru.skidoz.service.command.CommandName.ADD_ACTION_RATE_TARGET;
import static ru.skidoz.service.command.CommandName.ADD_BASKET;
import static ru.skidoz.service.command.CommandName.ADD_BOOKMARK;
import static ru.skidoz.service.command.CommandName.ADD_BOT;
import static ru.skidoz.service.command.CommandName.ADD_GOODS;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_DESCRIPTION;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_END;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_NAME;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_PHOTO;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_PRICE;
import static ru.skidoz.service.command.CommandName.ADD_PARTNER;
import static ru.skidoz.service.command.CommandName.ADD_TAXI_BOT;
import static ru.skidoz.service.command.CommandName.ADMIN;
import static ru.skidoz.service.command.CommandName.ADMIN_ADMIN;
import static ru.skidoz.service.command.CommandName.ADMIN_SHOPS;
import static ru.skidoz.service.command.CommandName.APPROVE_NEW_GRUPP;
import static ru.skidoz.service.command.CommandName.APPROVE_NEW_PARTNER;
import static ru.skidoz.service.command.CommandName.B2B;
import static ru.skidoz.service.command.CommandName.B2NOB;
import static ru.skidoz.service.command.CommandName.BASIC;
import static ru.skidoz.service.command.CommandName.BASKET;
import static ru.skidoz.service.command.CommandName.BASKET_ARCHIVE;
import static ru.skidoz.service.command.CommandName.BOOKMARKS;
import static ru.skidoz.service.command.CommandName.CASHBACKS;
import static ru.skidoz.service.command.CommandName.CONNECT;
import static ru.skidoz.service.command.CommandName.CONNECT_SHOP;
import static ru.skidoz.service.command.CommandName.CONSTRUCT;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_ADD;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_MIN_BILL_SHARE;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_SARAFAN_SHARE;
import static ru.skidoz.service.command.CommandName.COUPON;
import static ru.skidoz.service.command.CommandName.COUPON_NUMBER;
import static ru.skidoz.service.command.CommandName.COUPON_RATE_WITHDRAW;
import static ru.skidoz.service.command.CommandName.DISCARD_NEW_GRUPP;
import static ru.skidoz.service.command.CommandName.DISCARD_NEW_PARTNER;
import static ru.skidoz.service.command.CommandName.EDIT_BUTTON_NAME;
import static ru.skidoz.service.command.CommandName.EDIT_MESSAGE;
import static ru.skidoz.service.command.CommandName.EDIT_PARTNER;
import static ru.skidoz.service.command.CommandName.GEOMAP;
import static ru.skidoz.service.command.CommandName.GOODS_LIST;
import static ru.skidoz.service.command.CommandName.INITIALIZE;
import static ru.skidoz.service.command.CommandName.LINK_TO_PRODUCT;
import static ru.skidoz.service.command.CommandName.LINK_TO_PRODUCT_NUMBER;
import static ru.skidoz.service.command.CommandName.MONITOR;
import static ru.skidoz.service.command.CommandName.MONITOR_PRICE;
import static ru.skidoz.service.command.CommandName.MONITOR_RESP;
import static ru.skidoz.service.command.CommandName.MULTI_ACTION_LEVEL;
import static ru.skidoz.service.command.CommandName.MULTI_ACTION_LEVEL_BASIC;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_QUESTION;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_QUESTION_BASIC;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_RATE;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_RATE_BASIC;
import static ru.skidoz.service.command.CommandName.MY_SHOPS;
import static ru.skidoz.service.command.CommandName.NEGATIVE_COUNT;
import static ru.skidoz.service.command.CommandName.NEGATIVE_SUM;
import static ru.skidoz.service.command.CommandName.NEW_GRUPP;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_END_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_INPUT_BUTTON;
import static ru.skidoz.service.command.CommandName.NON_RESPONSE;
import static ru.skidoz.service.command.CommandName.ONE_LEVEL_RATE;
import static ru.skidoz.service.command.CommandName.ONE_LEVEL_RATE_BASIC;
import static ru.skidoz.service.command.CommandName.P2B;
import static ru.skidoz.service.command.CommandName.P2B_APPROVE_BASKET_CASHBACK;
import static ru.skidoz.service.command.CommandName.P2B_CHARGE_BASKET_CASHBACK;
import static ru.skidoz.service.command.CommandName.P2B_CHARGE_COUPON;
import static ru.skidoz.service.command.CommandName.P2B_CHARGE_COUPON_BASKET;
import static ru.skidoz.service.command.CommandName.P2B_CHARGE_COUPON_REQUEST;
import static ru.skidoz.service.command.CommandName.P2B_CHARGE_COUPON_RESP;
import static ru.skidoz.service.command.CommandName.P2B_PROPOSE_CASHBACK;
import static ru.skidoz.service.command.CommandName.P2B_PROPOSE_CASHBACK_RESP;
import static ru.skidoz.service.command.CommandName.P2B_RESP;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_CASHBACK;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_CASHBACK_APPROVE;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_CASHBACK_DISMISS;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_CASHBACK_REQUEST;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_CASHBACK_RESP;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_COUPON;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_COUPON_APPROVE;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_COUPON_BASKET;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_COUPON_REQUEST;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_COUPON_RESP;
import static ru.skidoz.service.command.CommandName.P2B_WRITEOFF_COUPON_SELECT_ACTION;
import static ru.skidoz.service.command.CommandName.P2NOP;
import static ru.skidoz.service.command.CommandName.P2NOP_RESP;
import static ru.skidoz.service.command.CommandName.P2P;
import static ru.skidoz.service.command.CommandName.P2P_RESP;
import static ru.skidoz.service.command.CommandName.PARTNERS;
import static ru.skidoz.service.command.CommandName.PSHARE2P;
import static ru.skidoz.service.command.CommandName.RESPONSE_BUYER_MESSAGE;
import static ru.skidoz.service.command.CommandName.RESPONSE_SHOP_MESSAGE;
import static ru.skidoz.service.command.CommandName.SEARCH;
import static ru.skidoz.service.command.CommandName.SEARCH_GROUP;
import static ru.skidoz.service.command.CommandName.SEARCH_GROUP_END;
import static ru.skidoz.service.command.CommandName.SEARCH_GROUP_LIMIT;
import static ru.skidoz.service.command.CommandName.SEARCH_GROUP_RESP;
import static ru.skidoz.service.command.CommandName.SEARCH_PARTNER;
import static ru.skidoz.service.command.CommandName.SEARCH_PARTNER_END;
import static ru.skidoz.service.command.CommandName.SEARCH_PARTNER_LIMIT;
import static ru.skidoz.service.command.CommandName.SEARCH_PARTNER_RATE;
import static ru.skidoz.service.command.CommandName.SEARCH_PARTNER_RESP;
import static ru.skidoz.service.command.CommandName.SEARCH_PARTNER_RESP_BUTTON;
import static ru.skidoz.service.command.CommandName.SEARCH_RESULT;
import static ru.skidoz.service.command.CommandName.SEARCH_RESULT_PRODUCT;
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
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER;
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER_END;
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER_GROUP;
import static ru.skidoz.service.command.CommandName.WITHDRAW_PARTNER_RESP;
import static ru.skidoz.util.TelegramElementsUtil.qrInputStream;

import com.google.zxing.WriterException;
import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.CategoryGroupCacheRepository;
import ru.skidoz.aop.repo.CategorySuperGroupCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.MenuCreator;
import ru.skidoz.util.MenuTypeEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class ShopLevelInitializer {

    public static User Users = null;
    
    @Autowired
    private MenuCreator menuCreator;
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowRepository;
    @Autowired
    private UserCacheRepository userRepository;
    @Autowired
    private ButtonCacheRepository buttonRepository;
    @Autowired
    private MessageCacheRepository messageRepository;
    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupRepository;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupRepository;
    @Autowired
    private CategoryCacheRepository categoryRepository;
    @Autowired
    private InitialLevel initialLevel;

    private static final String MANAGEMENT_FILE = "Book.xlsx";

    public void initLevels() {


        try {

                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ BK, BM, CB, PI-И ПОЛУЧАТЕЛь BI

                initialLevel.level_P2B.updateLevel(Users, P2B.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2B);
                Message message22_1 = new Message(initialLevel.level_P2B, Map.of(RU, "Пользователь X имеет закладки:"));
                messageRepository.cache(message22_1);
                initialLevel.level_P2B.addMessage(message22_1);

                Message message22_2 = new Message(initialLevel.level_P2B, 1, Map.of(RU, "Пользователь X имеет корзину:"));
                messageRepository.cache(message22_2);
                initialLevel.level_P2B.addMessage(message22_2);

                Message message22_3 = new Message(initialLevel.level_P2B, 2, Map.of(RU, "Пользователь X имеет кэшбеки:"));
                messageRepository.cache(message22_3);
                initialLevel.level_P2B.addMessage(message22_3);

                ButtonRow row22_0 = new ButtonRow(initialLevel.level_P2B);
                buttonRowRepository.cache(row22_0);
                initialLevel.level_P2B.addRow(row22_0);
                Button button22_0_0 = new Button(row22_0, Map.of(RU, "Предложить кэшбек вручную"), initialLevel.level_P2B_PROPOSE_CASHBACK.getIdString());
                buttonRepository.cache(button22_0_0);
                row22_0.add(button22_0_0);
                initialLevel.level_P2B.addRow(row22_0);

                ButtonRow row22_01 = new ButtonRow(initialLevel.level_P2B);
                buttonRowRepository.cache(row22_01);
                initialLevel.level_P2B.addRow(row22_01);
                Button button22_0_1 = new Button(row22_01, Map.of(RU, "Списать кэшбек вручную"), initialLevel.level_P2B_WRITEOFF_CASHBACK.getIdString());
                buttonRepository.cache(button22_0_1);
                row22_01.add(button22_0_1);
                initialLevel.level_P2B.addRow(row22_01);

                ButtonRow row22_1 = new ButtonRow(initialLevel.level_P2B);
                buttonRowRepository.cache(row22_1);
                initialLevel.level_P2B.addRow(row22_1);
                Button button22_1_1 = new Button(row22_1, Map.of(RU, "Начислить купон вручную"), initialLevel.level_P2B_CHARGE_COUPON.getIdString());
                buttonRepository.cache(button22_1_1);
                row22_1.add(button22_1_1);
                initialLevel.level_P2B.addRow(row22_1);

                ButtonRow row22_2 = new ButtonRow(initialLevel.level_P2B);
                buttonRowRepository.cache(row22_2);
                initialLevel.level_P2B.addRow(row22_2);
                Button button22_2_0 = new Button(row22_2, Map.of(RU, "Списать купон вручную"), initialLevel.level_P2B_WRITEOFF_COUPON.getIdString());
                buttonRepository.cache(button22_2_0);
                row22_2.add(button22_2_0);
                initialLevel.level_P2B.addRow(row22_2);

                ButtonRow row22_4 = new ButtonRow(initialLevel.level_P2B);
                buttonRowRepository.cache(row22_4);
                initialLevel.level_P2B.addRow(row22_4);
                Button button22_4_0 = new Button(row22_4, Map.of(RU, "Начислить купон по корзине"), initialLevel.level_P2B_CHARGE_COUPON_BASKET.getIdString());
                buttonRepository.cache(button22_4_0);
                row22_4.add(button22_4_0);
                initialLevel.level_P2B.addRow(row22_4);

                ButtonRow row22_3 = new ButtonRow(initialLevel.level_P2B);
                buttonRowRepository.cache(row22_3);
                initialLevel.level_P2B.addRow(row22_3);
                Button button22_3_0 = new Button(row22_3, Map.of(RU, "Списать купон по корзине"), initialLevel.level_P2B_WRITEOFF_COUPON_BASKET.getIdString());
                buttonRepository.cache(button22_3_0);
                row22_3.add(button22_3_0);
                initialLevel.level_P2B.addRow(row22_3);

                ButtonRow row22_5 = new ButtonRow(initialLevel.level_P2B);
                buttonRowRepository.cache(row22_5);
                Button button22_1_0 = new Button(row22_5, Map.of(RU, "Подтвердить кэшбек по корзине"), initialLevel.level_P2B_CHARGE_BASKET_CASHBACK.getIdString());
                buttonRepository.cache(button22_1_0);
                row22_5.add(button22_1_0);
                initialLevel.level_P2B.addRow(row22_5);


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


        ////НАЧИСЛЕНИЕ КЭШБЕКА

                initialLevel.level_P2B_PROPOSE_CASHBACK.updateLevel(Users, P2B_PROPOSE_CASHBACK.name(), initialLevel.level_P2B, true);

                levelRepository.cache(initialLevel.level_P2B_PROPOSE_CASHBACK);
                Message message22_0_0_1 = new Message(initialLevel.level_P2B_PROPOSE_CASHBACK, Map.of(RU, "Введите сумму начисленного кэшбека:"));
                messageRepository.cache(message22_0_0_1);
                initialLevel.level_P2B_PROPOSE_CASHBACK.addMessage(message22_0_0_1);

                initialLevel.level_P2B_PROPOSE_CASHBACK_RESP.updateLevel(Users, P2B_PROPOSE_CASHBACK_RESP.name(), initialLevel.level_P2B_PROPOSE_CASHBACK, true);

                levelRepository.cache(initialLevel.level_P2B_PROPOSE_CASHBACK_RESP);
                Message message22_0_1_1 = new Message(initialLevel.level_P2B_PROPOSE_CASHBACK_RESP, Map.of(RU, "Клиенту начислено X"));
                messageRepository.cache(message22_0_1_1);
                initialLevel.level_P2B_PROPOSE_CASHBACK_RESP.addMessage(message22_0_1_1);

                /////СПИСАНИЕ КЭШБЕКА

                initialLevel.level_P2B_WRITEOFF_CASHBACK.updateLevel(Users, P2B_WRITEOFF_CASHBACK.name(), initialLevel.level_P2B, false);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_CASHBACK);
                Message message22_1_0 = new Message(initialLevel.level_P2B_WRITEOFF_CASHBACK, Map.of(RU, "Введите сумму списания кэшбека:"));
                messageRepository.cache(message22_1_0);
                initialLevel.level_P2B_WRITEOFF_CASHBACK.addMessage(message22_1_0);

                initialLevel.level_P2B_WRITEOFF_CASHBACK_RESP.updateLevel(Users, P2B_WRITEOFF_CASHBACK_RESP.name(), initialLevel.level_P2B_WRITEOFF_CASHBACK, false);//true

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_CASHBACK_RESP);
                Message message22_1_3 = new Message(initialLevel.level_P2B_WRITEOFF_CASHBACK_RESP, Map.of(RU, "Запрос на списание отправлен"));
                messageRepository.cache(message22_1_3);
                initialLevel.level_P2B_WRITEOFF_CASHBACK_RESP.addMessage(message22_1_3);

                initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST.updateLevel(Users, P2B_WRITEOFF_CASHBACK_REQUEST.name(), initialLevel.level_P2B_WRITEOFF_CASHBACK, true);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST);
                Message message22_1_2 = new Message(initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST, Map.of(RU, "Магазин Y предлагает списать X"));
                messageRepository.cache(message22_1_2);
                initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST.addMessage(message22_1_2);
                ButtonRow row22_0_0 = new ButtonRow(initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST);
                buttonRowRepository.cache(row22_0_0);
                Button button22_0_0_0 = new Button(row22_0_0, Map.of(RU, "Списать"), initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE.getIdString());
                buttonRepository.cache(button22_0_0_0);
                row22_0_0.add(button22_0_0_0);
                Button button22_0_0_1 = new Button(row22_0_0, Map.of(RU, "Отклонить"), initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS.getIdString());
                buttonRepository.cache(button22_0_0_1);
                row22_0_0.add(button22_0_0_1);
                initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST.addRow(row22_0_0);

                initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE.updateLevel(Users, P2B_WRITEOFF_CASHBACK_APPROVE.name(), initialLevel.level_P2B, false);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE);
                Message message22_1_1 = new Message(initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE, Map.of(RU, "Списано кешбека X"));
                messageRepository.cache(message22_1_1);
                initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE.addMessage(message22_1_1);

                initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS.updateLevel(Users, P2B_WRITEOFF_CASHBACK_DISMISS.name(), initialLevel.level_P2B, false);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS);
                Message message22_1_4 = new Message(initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS, Map.of(RU, "Списание отклонено"));
                messageRepository.cache(message22_1_4);
                initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS.addMessage(message22_1_4);

                ///// ПОДТВЕРЖДЕНИЕ ПО КОРЗИНЕ

                initialLevel.level_P2B_CHARGE_BASKET_CASHBACK.updateLevel(Users, P2B_CHARGE_BASKET_CASHBACK.name(), initialLevel.level_P2B, true);

                levelRepository.cache(initialLevel.level_P2B_CHARGE_BASKET_CASHBACK);
                Message message22_2_0 = new Message(initialLevel.level_P2B_CHARGE_BASKET_CASHBACK, Map.of(RU, "Сумма списания кэшбека по корзине X, сумма начисления кэшбека Y"));
                messageRepository.cache(message22_2_0);
                initialLevel.level_P2B_CHARGE_BASKET_CASHBACK.addMessage(message22_2_0);
                ButtonRow row22_2_0 = new ButtonRow(initialLevel.level_P2B_CHARGE_BASKET_CASHBACK);
                buttonRowRepository.cache(row22_2_0);
                Button button22_2_0_0 = new Button(row22_2_0, Map.of(RU, "Подтвердить!"), initialLevel.level_P2B_APPROVE_BASKET_CASHBACK.getIdString());
                buttonRepository.cache(button22_2_0_0);
                row22_2_0.add(button22_2_0_0);
                Button button22_2_0_1 = new Button(row22_2_0, Map.of(RU, "Начислить вручную"), initialLevel.level_P2B_PROPOSE_CASHBACK.getIdString());
                buttonRepository.cache(button22_2_0_1);
                row22_2_0.add(button22_2_0_1);
                initialLevel.level_P2B_CHARGE_BASKET_CASHBACK.addRow(row22_2_0);


                initialLevel.level_P2B_APPROVE_BASKET_CASHBACK.updateLevel(Users, P2B_APPROVE_BASKET_CASHBACK.name(), initialLevel.level_P2B, false);//true

                levelRepository.cache(initialLevel.level_P2B_APPROVE_BASKET_CASHBACK);
                Message message22_2_1 = new Message(initialLevel.level_P2B_APPROVE_BASKET_CASHBACK, Map.of(RU, "Сумма списания кэшбека по корзине X, сумма начисления кэшбека Y"));
                messageRepository.cache(message22_2_1);
                initialLevel.level_P2B_APPROVE_BASKET_CASHBACK.addMessage(message22_2_1);
                

                /////НАЧИСЛЕНИЕ КУПОНА

                initialLevel.level_P2B_CHARGE_COUPON.updateLevel(Users, P2B_CHARGE_COUPON.name(), initialLevel.level_P2B, false);

                levelRepository.cache(initialLevel.level_P2B_CHARGE_COUPON);
                Message message22_3_0 = new Message(initialLevel.level_P2B_CHARGE_COUPON, Map.of(RU, "Выберите акцию:"));
                messageRepository.cache(message22_3_0);
                initialLevel.level_P2B_CHARGE_COUPON.addMessage(message22_3_0);

                /////НАЧИСЛЕНИЕ КУПОНА

                initialLevel.level_P2B_CHARGE_COUPON_BASKET.updateLevel(Users, P2B_CHARGE_COUPON_BASKET.name(), initialLevel.level_P2B, false);

                levelRepository.cache(initialLevel.level_P2B_CHARGE_COUPON_BASKET);
                Message message220_3_0 = new Message(initialLevel.level_P2B_CHARGE_COUPON_BASKET, Map.of(RU, "Выберите акцию:"));
                messageRepository.cache(message220_3_0);
                initialLevel.level_P2B_CHARGE_COUPON_BASKET.addMessage(message220_3_0);


                initialLevel.level_P2B_CHARGE_COUPON_REQUEST.updateLevel(Users, P2B_CHARGE_COUPON_REQUEST.name(), initialLevel.level_P2B_CHARGE_COUPON, true);

                levelRepository.cache(initialLevel.level_P2B_CHARGE_COUPON_REQUEST);
                Message message220_3_1 = new Message(initialLevel.level_P2B_CHARGE_COUPON_REQUEST, Map.of(RU, "Введите количество начисленных купонов по акции X"));
                messageRepository.cache(message220_3_1);
                initialLevel.level_P2B_CHARGE_COUPON_REQUEST.addMessage(message220_3_1);


                initialLevel.level_P2B_CHARGE_COUPON_RESP.updateLevel(Users, P2B_CHARGE_COUPON_RESP.name(), initialLevel.level_P2B_CHARGE_COUPON_REQUEST, true);

                levelRepository.cache(initialLevel.level_P2B_CHARGE_COUPON_RESP);
                Message message22_3_1 = new Message(initialLevel.level_P2B_CHARGE_COUPON_RESP, Map.of(RU, "начислено количество X"));
                messageRepository.cache(message22_3_1);
                initialLevel.level_P2B_CHARGE_COUPON_RESP.addMessage(message22_3_1);


                /////СПИСАНИЕ КУПОНА

                initialLevel.level_P2B_WRITEOFF_COUPON_BASKET.updateLevel(Users, P2B_WRITEOFF_COUPON_BASKET.name(), initialLevel.level_P2B, true);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_COUPON_BASKET);
                Message message2201_4_0 = new Message(initialLevel.level_P2B_WRITEOFF_COUPON_BASKET, Map.of(RU, "Выберите акцию:"));
                messageRepository.cache(message2201_4_0);
                initialLevel.level_P2B_WRITEOFF_COUPON_BASKET.addMessage(message2201_4_0);

                /////СПИСАНИЕ КУПОНА

                initialLevel.level_P2B_WRITEOFF_COUPON.updateLevel(Users, P2B_WRITEOFF_COUPON.name(), initialLevel.level_P2B, true);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_COUPON);
                Message message220_4_0 = new Message(initialLevel.level_P2B_WRITEOFF_COUPON, Map.of(RU, "Выберите акцию:"));
                messageRepository.cache(message220_4_0);
                initialLevel.level_P2B_WRITEOFF_COUPON.addMessage(message220_4_0);

                initialLevel.level_P2B_WRITEOFF_COUPON_SELECT_ACTION.updateLevel(Users, P2B_WRITEOFF_COUPON_SELECT_ACTION.name(), initialLevel.level_P2B, true);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_COUPON_SELECT_ACTION);
                Message message22_4_0 = new Message(initialLevel.level_P2B_WRITEOFF_COUPON_SELECT_ACTION, Map.of(RU, "Введите количество списаний X"));
                messageRepository.cache(message22_4_0);
                initialLevel.level_P2B_WRITEOFF_COUPON_SELECT_ACTION.addMessage(message22_4_0);

                initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST.updateLevel(Users, P2B_WRITEOFF_COUPON_REQUEST.name(), initialLevel.level_P2B_WRITEOFF_COUPON_SELECT_ACTION, true);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST);
                Message message22_4_2 = new Message(initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST, Map.of(RU, "Магазин Y предлагает списать X"));
                messageRepository.cache(message22_4_2);
                initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST.addMessage(message22_4_2);
                ButtonRow row22_4_0 = new ButtonRow(initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST);
                buttonRowRepository.cache(row22_4_0);
                Button button22_4_0_0 = new Button(row22_4_0, Map.of(RU, "Списать"), initialLevel.level_P2B_PROPOSE_CASHBACK.getIdString());
                buttonRepository.cache(button22_4_0_0);
                row22_4_0.add(button22_4_0_0);
                Button button22_4_0_1 = new Button(row22_4_0, Map.of(RU, "Отклонить"), initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS.getIdString());
                buttonRepository.cache(button22_4_0_1);
                row22_4_0.add(button22_4_0_1);
                initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST.addRow(row22_4_0);

                initialLevel.level_P2B_WRITEOFF_COUPON_RESP.updateLevel(Users, P2B_WRITEOFF_COUPON_RESP.name(), initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST, true);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_COUPON_RESP);
                Message message22_4_1 = new Message(initialLevel.level_P2B_WRITEOFF_COUPON_RESP, Map.of(RU, "Списано количество X"));
                messageRepository.cache(message22_4_1);
                initialLevel.level_P2B_WRITEOFF_COUPON_RESP.addMessage(message22_4_1);

                initialLevel.level_P2B_WRITEOFF_COUPON_APPROVE.updateLevel(Users, P2B_WRITEOFF_COUPON_APPROVE.name(), initialLevel.level_P2B_WRITEOFF_COUPON_RESP, false);

                levelRepository.cache(initialLevel.level_P2B_WRITEOFF_COUPON_APPROVE);
                Message message221_4_1 = new Message(initialLevel.level_P2B_WRITEOFF_COUPON_APPROVE, Map.of(RU, "Списано количество X"));
                messageRepository.cache(message221_4_1);
                initialLevel.level_P2B_WRITEOFF_COUPON_APPROVE.addMessage(message221_4_1);

//            ButtonRow row22_0 = new ArrayList<>();
//            Button button49_0_0 = new Button(initialLevel.level_TAXI_LOCATION, "Выбрать шаблон и перейти к редактированию", ADD_TAXI_BOT.name());
//            buttonRepository.cache(button49_0_0);
//            row18_0.add(button49_0_0);
//            initialLevel.level_TAXI_LOCATION.addRow(row22_0);
//            ButtonRow row22_1 = new ArrayList<>();
//            Button button49_0_1 = new Button(initialLevel.level_TAXI_LOCATION, "Вернуться к выбору шаблона", ADD_BOT.name());
//            buttonRepository.cache(button49_0_1);
//            row22_1.add(button49_0_1);
//            initialLevel.level_TAXI_LOCATION.addRow(row22_1);


                initialLevel.level_P2B_RESP.updateLevel(Users, P2B_RESP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2B_RESP);
                Message message22_11_1 = new Message(initialLevel.level_P2B_RESP, Map.of(RU, "Пользователь X имеет закладки:"));
                messageRepository.cache(message22_11_1);
                initialLevel.level_P2B_RESP.addMessage(message22_11_1);


                System.out.println("PRE addFinalButton");

                initialLevel.addFinalButton(initialLevel.level_INITIALIZE, initialLevel.level_INITIALIZE);

                System.out.println("POST addFinalButton");

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
