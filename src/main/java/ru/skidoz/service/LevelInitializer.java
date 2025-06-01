package ru.skidoz.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.zxing.WriterException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
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
import ru.skidoz.util.MenuTypeEnum;

import static ru.skidoz.model.entity.category.LanguageEnum.DE;
import static ru.skidoz.model.entity.category.LanguageEnum.EN;
import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.util.TelegramElementsUtil.qrInputStream;
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
import static ru.skidoz.service.command.CommandName.INITIALIZE0;
import static ru.skidoz.service.command.CommandName.LANGUAGER;
import static ru.skidoz.service.command.CommandName.LANGUAGES;
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

/**
 * @author andrey.semenov
 */
@Component
public class LevelInitializer {

    public static User Users = null;

    public static Shop SHOP = null;

    public static CategorySuperGroup bigCategorySuperGroup = null;

    public static CategoryGroup bigCategoryGroup = null;

    public static Category bigCategory = null;
    
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

        initialLevel.level_INITIALIZE222 = levelRepository.cache(new Level());
        initialLevel.level_INITIALIZE0 = levelRepository.cache(new Level());
        initialLevel.level_INITIALIZE = levelRepository.cache(new Level());
        initialLevel.level_ADMIN = levelRepository.cache(new Level());
        initialLevel.level_ADMIN_ADMIN = levelRepository.cache(new Level());
        initialLevel.level_LANGUAGES = levelRepository.cache(new Level());
        initialLevel.level_LANGUAGER = levelRepository.cache(new Level());
        initialLevel.level_MAP = levelRepository.cache(new Level());
        initialLevel.level_MONITOR = levelRepository.cache(new Level());
        initialLevel.level_MONITOR_PRICE = levelRepository.cache(new Level());
        initialLevel.level_MONITOR_RESP = levelRepository.cache(new Level());
        initialLevel.level_GOODS_LIST = levelRepository.cache(new Level());
        initialLevel.level_SHOP_BOTS = levelRepository.cache(new Level());
        initialLevel.level_PARTNERS = levelRepository.cache(new Level());
        initialLevel.level_SELLERS = levelRepository.cache(new Level());
        initialLevel.level_SELLERS_REMOVE = levelRepository.cache(new Level());
        initialLevel.level_SELLERS_ADD = levelRepository.cache(new Level());
        initialLevel.level_SELLERS_ADD_APPROVE = levelRepository.cache(new Level());
        initialLevel.level_SELLERS_ADD_DISMISS = levelRepository.cache(new Level());
        initialLevel.level_SELLERS_REMOVE_APPROVE = levelRepository.cache(new Level());
        initialLevel.level_SELLERS_REMOVE_DISMISS = levelRepository.cache(new Level());
        initialLevel.level_BASIC = levelRepository.cache(new Level());
        initialLevel.level_WITHDRAW_PARTNER = levelRepository.cache(new Level());
        initialLevel.level_WITHDRAW_PARTNER_GROUP = levelRepository.cache(new Level());
        initialLevel.level_WITHDRAW_PARTNER_RESP = levelRepository.cache(new Level());
        initialLevel.level_WITHDRAW_PARTNER_END = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_PARTNER = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_PARTNER_RESP = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_PARTNER_RESP_BUTTON = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_PARTNER_RATE = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_PARTNER_LIMIT = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_PARTNER_END = levelRepository.cache(new Level());
        initialLevel.level_EDIT_PARTNER = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_GROUP = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_GROUP_RESP = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_GROUP_LIMIT = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_GROUP_END = levelRepository.cache(new Level());
        initialLevel.level_ADD_PARTNER = levelRepository.cache(new Level());
        initialLevel.level_ADD_GROUP = levelRepository.cache(new Level());
        initialLevel.level_ACTIONS = levelRepository.cache(new Level());
        initialLevel.level_BASKETS_FOR_SHOP = levelRepository.cache(new Level());
        initialLevel.level_ACTION_TYPE = levelRepository.cache(new Level());
        initialLevel.level_SELECT_LEVEL_TYPE = levelRepository.cache(new Level());
        initialLevel.level_MULTI_ACTION_LEVEL = levelRepository.cache(new Level());
        initialLevel.level_MULTI_ACTION_LEVEL_BASIC = levelRepository.cache(new Level());
        initialLevel.level_MULTI_LEVEL_RATE = levelRepository.cache(new Level());
        initialLevel.level_MULTI_LEVEL_RATE_BASIC = levelRepository.cache(new Level());
        initialLevel.level_MULTI_LEVEL_QUESTION = levelRepository.cache(new Level());
        initialLevel.level_MULTI_LEVEL_QUESTION_BASIC = levelRepository.cache(new Level());
        initialLevel.level_ONE_LEVEL_RATE = levelRepository.cache(new Level());
        initialLevel.level_ONE_LEVEL_RATE_BASIC = levelRepository.cache(new Level());
        initialLevel.level_ACTION_RATE_WITHDRAW = levelRepository.cache(new Level());
        initialLevel.level_ACTION_RATE_WITHDRAW_BASIC = levelRepository.cache(new Level());
        initialLevel.level_ACTION_RATE_PARTNER = levelRepository.cache(new Level());
        initialLevel.level_ADD_ACTION_RATE_SOURCE = levelRepository.cache(new Level());
        initialLevel.level_ADD_ACTION_COUPON_TARGET = levelRepository.cache(new Level());
        initialLevel.level_COUPON = levelRepository.cache(new Level());
        initialLevel.level_COUPON_NUMBER = levelRepository.cache(new Level());
        initialLevel.level_COUPON_RATE_WITHDRAW = levelRepository.cache(new Level());
        initialLevel.level_ADD_ACTION_COUPON_SOURCE = levelRepository.cache(new Level());
        initialLevel.level_ADD_ACTION_RATE_TARGET = levelRepository.cache(new Level());
        initialLevel.level_LINK_TO_PRODUCT = levelRepository.cache(new Level());
        initialLevel.level_LINK_TO_PRODUCT_NUMBER = levelRepository.cache(new Level());
        initialLevel.level_ADD_ACTION_LINK_SOURCE = levelRepository.cache(new Level());
        initialLevel.level_ADD_ACTION_LINK_TARGET = levelRepository.cache(new Level());
        initialLevel.level_MY_SHOPS = levelRepository.cache(new Level());
        initialLevel.level_CASHBACKS = levelRepository.cache(new Level());
        initialLevel.level_CONNECT = levelRepository.cache(new Level());
        initialLevel.level_CONNECT_SHOP = levelRepository.cache(new Level());
        initialLevel.level_BOOKMARKS = levelRepository.cache(new Level());
        initialLevel.level_ADD_BOOKMARK = levelRepository.cache(new Level());
        initialLevel.level_BASKET = levelRepository.cache(new Level());
        initialLevel.level_BASKET_ARCHIVE = levelRepository.cache(new Level());
        initialLevel.level_ADD_BASKET = levelRepository.cache(new Level());
        initialLevel.level_SEARCH = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_RESULT = levelRepository.cache(new Level());
        initialLevel.level_SEARCH_RESULT_PRODUCT = levelRepository.cache(new Level());
        initialLevel.level_CONSTRUCT = levelRepository.cache(new Level());
        initialLevel.level_ADMIN_SHOPS = levelRepository.cache(new Level());
        initialLevel.level_CONSTRUCT_SARAFAN_SHARE = levelRepository.cache(new Level());
        initialLevel.level_CONSTRUCT_MIN_BILL_SHARE = levelRepository.cache(new Level());
        initialLevel.level_CONSTRUCT_ADD = levelRepository.cache(new Level());
        initialLevel.level_ADD_GOODS = levelRepository.cache(new Level());
        initialLevel.level_ADD_GOODS_NAME = levelRepository.cache(new Level());
        initialLevel.level_ADD_GOODS_PHOTO = levelRepository.cache(new Level());
        initialLevel.level_ADD_GOODS_DESCRIPTION = levelRepository.cache(new Level());
        initialLevel.level_ADD_GOODS_PRICE = levelRepository.cache(new Level());
        initialLevel.level_ADD_GOODS_END = levelRepository.cache(new Level());
        initialLevel.level_ADD_BOT = levelRepository.cache(new Level());
        initialLevel.level_ADD_TAXI_BOT = levelRepository.cache(new Level());
        initialLevel.level_TAXI_LOCATION = levelRepository.cache(new Level());
        initialLevel.level_TAXI_SUBMIT = levelRepository.cache(new Level());
        initialLevel.level_PSHARE2P = levelRepository.cache(new Level());
        initialLevel.level_P2NOP = levelRepository.cache(new Level());
        initialLevel.level_P2NOP_RESP = levelRepository.cache(new Level());
        initialLevel.level_P2P = levelRepository.cache(new Level());
        initialLevel.level_P2P_RESP = levelRepository.cache(new Level());
        initialLevel.level_P2B = levelRepository.cache(new Level());
        initialLevel.level_NEGATIVE_SUM = levelRepository.cache(new Level());
        initialLevel.level_NEGATIVE_COUNT = levelRepository.cache(new Level());
        initialLevel.level_P2B_PROPOSE_CASHBACK = levelRepository.cache(new Level());
        initialLevel.level_P2B_PROPOSE_CASHBACK_RESP = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_CASHBACK = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_CASHBACK_RESP = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_CASHBACK_REQUEST = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_CASHBACK_APPROVE = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_CASHBACK_DISMISS = levelRepository.cache(new Level());
        initialLevel.level_P2B_CHARGE_BASKET_CASHBACK = levelRepository.cache(new Level());
        initialLevel.level_P2B_APPROVE_BASKET_CASHBACK = levelRepository.cache(new Level());
        initialLevel.level_P2B_CHARGE_COUPON = levelRepository.cache(new Level());
        initialLevel.level_P2B_CHARGE_COUPON_REQUEST = levelRepository.cache(new Level());
        initialLevel.level_P2B_CHARGE_COUPON_RESP = levelRepository.cache(new Level());
        initialLevel.level_P2B_CHARGE_COUPON_BASKET = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_COUPON = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_COUPON_SELECT_ACTION = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_COUPON_BASKET = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_COUPON_RESP = levelRepository.cache(new Level());
        initialLevel.level_P2B_WRITEOFF_COUPON_APPROVE = levelRepository.cache(new Level());
        initialLevel.level_P2B_RESP = levelRepository.cache(new Level());
        initialLevel.level_B2B = levelRepository.cache(new Level());
        initialLevel.level_B2NOB = levelRepository.cache(new Level());
        initialLevel.level_DISCARD_NEW_PARTNER = levelRepository.cache(new Level());
        initialLevel.level_APPROVE_NEW_PARTNER = levelRepository.cache(new Level());
        initialLevel.level_NEW_GRUPP = levelRepository.cache(new Level());
        initialLevel.level_DISCARD_NEW_GRUPP = levelRepository.cache(new Level());
        initialLevel.level_APPROVE_NEW_GRUPP = levelRepository.cache(new Level());
        initialLevel.level0_1_1 = levelRepository.cache(new Level());
        initialLevel.level_RESPONSE_BUYER_MESSAGE = levelRepository.cache(new Level());
        initialLevel.level_RESPONSE_SHOP_MESSAGE = levelRepository.cache(new Level());
        initialLevel.level_NON_RESPONSE = levelRepository.cache(new Level());
        initialLevel.level0_1_3 = levelRepository.cache(new Level());
        initialLevel.level0_1_4 = levelRepository.cache(new Level());
        initialLevel.level0_1_5 = levelRepository.cache(new Level());
        initialLevel.level_EDIT_BUTTON_NAME = levelRepository.cache(new Level());
        initialLevel.level_EDIT_MESSAGE = levelRepository.cache(new Level());
        initialLevel.level_NEW_LEVEL_END_BUTTON = levelRepository.cache(new Level());
        initialLevel.level_NEW_LEVEL_INPUT_BUTTON = levelRepository.cache(new Level());
        initialLevel.level_NEW_LEVEL_BUTTON = levelRepository.cache(new Level());

            try {
                Users = userRepository.findByChatId(1L);
                if (Users == null) {
                    Users = new User(e -> {
                        e.setChatId(1L);
                        e.setName("SkidoBOT");
                        e.setSessionId("1");
                        e.setShopOwner(true);
                        e.setLanguage(RU);
                    });
                    userRepository.save(Users);
                }

                Shop SHOP = shopRepository.findById(1);
                if (SHOP == null) {
                    SHOP = new Shop();
                    SHOP.setName("DEFAULT");
                    SHOP.setAdminUser(Users.getId());
                    SHOP.setChatId(1L);
                    shopRepository.save(SHOP);

                    System.out.println("SHOP----------" + SHOP);

                    SHOP.getSellerSet().add(Users.getId());

                    shopRepository.save(SHOP);

                    bigCategorySuperGroup = new CategorySuperGroup();
                    bigCategorySuperGroup.setAlias("big");
                    categorySuperGroupRepository.save(bigCategorySuperGroup);


                    System.out.println("bigCategorySuperGroupDTO+++++++++++++++++++++++" + bigCategorySuperGroup);

                    bigCategoryGroup = new CategoryGroup();
                    bigCategoryGroup.setCategorySuperGroup(bigCategorySuperGroup.getId());
                    bigCategoryGroup.setAlias("big");
                    categoryGroupRepository.save(bigCategoryGroup);

                    bigCategory = new Category();
                    bigCategory.setCategorySuperGroup(bigCategorySuperGroup.getId());
                    bigCategory.setCategoryGroup(bigCategoryGroup.getId());
                    bigCategory.setActual(true);
                    bigCategory.setAlias("big");
                    categoryRepository.save(bigCategory);

                    Shop lamoda = new Shop();
                    lamoda.setName("lamoda");
                    lamoda.setAdminUser(Users.getId());
                    lamoda.setChatId(1L);
                    shopRepository.save(lamoda);
                    System.out.println("lamoda----------" + lamoda);
                    lamoda.getSellerSet().add(Users.getId());
                    shopRepository.save(lamoda);

                    Shop wildberries = new Shop();
                    wildberries.setName("wildberries");
                    wildberries.setAdminUser(Users.getId());
                    wildberries.setChatId(1L);
                    shopRepository.save(wildberries);
                    System.out.println("wildberries----------" + wildberries);
                    wildberries.getSellerSet().add(Users.getId());
                    shopRepository.save(wildberries);

                    Shop ozon = new Shop();
                    ozon.setName("ozon");
                    ozon.setAdminUser(Users.getId());
                    ozon.setChatId(1L);
                    shopRepository.save(ozon);
                    System.out.println("ozon----------" + ozon);
                    ozon.getSellerSet().add(Users.getId());
                    shopRepository.save(ozon);
                }




                //////////СТАРТОВОЕ МЕНЮ для пользователя с магазинами

                initialLevel.level_INITIALIZE.updateLevel(Users, INITIALIZE.name(), initialLevel.level_INITIALIZE222, false);

                levelRepository.cache(initialLevel.level_INITIALIZE);
                Message message0_1 = new Message(initialLevel.level_INITIALIZE, Map.of(RU, "Добро пожаловать в Skidozona"));
                messageRepository.cache(message0_1);
                initialLevel.level_INITIALIZE.addMessage(message0_1);

                ButtonRow row0_00 = new ButtonRow(initialLevel.level_INITIALIZE);
                buttonRowRepository.cache(row0_00);
                initialLevel.level_INITIALIZE.addRow(row0_00);
                Button button0_0_00 = new Button(row0_00, Map.of(RU, "Мониторить цену в WB, Ozone, LaModa.."), initialLevel.level_MONITOR.getIdString());
                buttonRepository.cache(button0_0_00);
                row0_00.add(button0_0_00);

                ButtonRow row0_0 = new ButtonRow(initialLevel.level_INITIALIZE);
                buttonRowRepository.cache(row0_0);
                initialLevel.level_INITIALIZE.addRow(row0_0);
                Button button0_0_0 = new Button(row0_0, Map.of(RU, "Кешбеки"), initialLevel.level_CASHBACKS.getIdString());
                buttonRepository.cache(button0_0_0);
                row0_0.add(button0_0_0);
                Button button0_0_1 = new Button(row0_0, Map.of(RU, "Закладки"), initialLevel.level_BOOKMARKS.getIdString());
                buttonRepository.cache(button0_0_1);
                row0_0.add(button0_0_1);
                System.out.println("D+++" + row0_0);
//                buttonRowRepository.cache(row0_0);
                System.out.println("E+++" + row0_0);
                ButtonRow row0_1 = new ButtonRow(initialLevel.level_INITIALIZE);
                buttonRowRepository.cache(row0_1);
                initialLevel.level_INITIALIZE.addRow(row0_1);
                Button button0_1_0 = new Button(row0_1, Map.of(RU, "Корзина"), initialLevel.level_BASKET.getIdString());
                buttonRepository.cache(button0_1_0);
                row0_1.add(button0_1_0);
                Button button0_1_1 = new Button(row0_1, Map.of(RU, "Поиск"), initialLevel.level_SEARCH.getIdString());
                buttonRepository.cache(button0_1_1);
                row0_1.add(button0_1_1);
//                buttonRowRepository.cache(row0_1);
                ButtonRow row0_2 = new ButtonRow(initialLevel.level_INITIALIZE);
                buttonRowRepository.cache(row0_2);
                initialLevel.level_INITIALIZE.addRow(row0_2);
                Button button0_2_0 = new Button(row0_2, Map.of(RU, "Соединить"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button0_2_0);
                row0_2.add(button0_2_0);
                Button button0_2_1 = new Button(row0_2, Map.of(RU, "Админка"), initialLevel.level_ADMIN.getIdString());
                buttonRepository.cache(button0_2_1);
                row0_2.add(button0_2_1);
                ButtonRow row0_3 = new ButtonRow(initialLevel.level_INITIALIZE);
                buttonRowRepository.cache(row0_3);
                Button button0_3_1 = new Button(row0_3, Map.of(RU, "EN/DE/RU"), initialLevel.level_LANGUAGES.getIdString());
                buttonRepository.cache(button0_3_1);
                row0_3.add(button0_3_1);
                Button button0_1_0_11 = new Button(row0_3, Map.of(RU, "Архив Корзин"), initialLevel.level_BASKET_ARCHIVE.getIdString());
                buttonRepository.cache(button0_1_0_11);
                row0_3.add(button0_1_0_11);
//                buttonRowRepository.cache(row0_2);

                //////////СТАРТОВОЕ МЕНЮ для пользователя без магазинов

                initialLevel.level_INITIALIZE0.updateLevel(Users, INITIALIZE0.name(), initialLevel.level_INITIALIZE, true);

                levelRepository.cache(initialLevel.level_INITIALIZE0);
                Message message00_1 = new Message(initialLevel.level_INITIALIZE0, Map.of(RU, "Добро пожаловать в Skidozona"));
                messageRepository.cache(message00_1);
                initialLevel.level_INITIALIZE0.addMessage(message00_1);
                ButtonRow row00_0 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_0);
                initialLevel.level_INITIALIZE0.addRow(row00_0);
                Button button00_0_0 = new Button(row00_0, Map.of(RU, "Кешбеки"), initialLevel.level_CASHBACKS.getIdString());
                buttonRepository.cache(button00_0_0);
                row00_0.add(button00_0_0);
                Button button00_0_1 = new Button(row00_0, Map.of(RU, "Закладки"), initialLevel.level_BOOKMARKS.getIdString());
                buttonRepository.cache(button00_0_1);
                row00_0.add(button00_0_1);
//                buttonRowRepository.cache(row00_0);
                ButtonRow row00_1 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_1);
                initialLevel.level_INITIALIZE0.addRow(row00_1);
                Button button00_1_0 = new Button(row00_1, Map.of(RU, "Корзина"), initialLevel.level_BASKET.getIdString());
                buttonRepository.cache(button00_1_0);
                row00_1.add(button00_1_0);
                Button button00_1_1 = new Button(row00_1, Map.of(RU, "Поиск"), initialLevel.level_SEARCH.getIdString());
                buttonRepository.cache(button00_1_1);
                row00_1.add(button00_1_1);
//                buttonRowRepository.cache(row00_1);
                ButtonRow row00_2 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_2);
                initialLevel.level_INITIALIZE0.addRow(row00_2);
                Button button00_2_0 = new Button(row00_2, Map.of(RU, "Соединить"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button00_2_0);
                row00_2.add(button00_2_0);
                Button button00_2_1 = new Button(row00_2, Map.of(RU, "Мои магазины"), initialLevel.level_MY_SHOPS.getIdString());
                buttonRepository.cache(button00_2_1);
                row00_2.add(button00_2_1);
//                buttonRowRepository.cache(row00_2);
                ButtonRow row00_3 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_3);
                initialLevel.level_INITIALIZE0.addRow(row00_3);
                Button button00_3_0 = new Button(row00_3, Map.of(RU, "Создать магазин/сервис"), initialLevel.level_CONSTRUCT.getIdString());
                buttonRepository.cache(button00_3_0);
                row00_3.add(button00_3_0);
                Button button00_3_1 = new Button(row00_3, Map.of(RU, "EN/DE/RU"), initialLevel.level_LANGUAGES.getIdString());
                buttonRepository.cache(button00_3_1);
                row00_3.add(button00_3_1);
                Button button000_1_0_11 = new Button(row00_3, Map.of(RU, "Архив Корзин"), initialLevel.level_BASKET_ARCHIVE.getIdString());
                buttonRepository.cache(button000_1_0_11);
                row00_3.add(button000_1_0_11);
//                buttonRowRepository.cache(row00_3);

                //////////ОБЩЕЕ ДЛЯ ЯЗЫКОВ
                initialLevel.level_LANGUAGES.updateLevel(Users, LANGUAGES.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_LANGUAGES);
                Message message00_1_ = new Message(initialLevel.level_LANGUAGES, Map.of(RU, "Choose another language"));
                messageRepository.cache(message00_1_);
                initialLevel.level_LANGUAGES.addMessage(message00_1_);

                ButtonRow row0_2_lang = new ButtonRow(initialLevel.level_LANGUAGES);
                buttonRowRepository.cache(row0_2_lang);
                initialLevel.level_LANGUAGES.addRow(row0_2_lang);
                Button button0_2_0_lang = new Button(row0_2_lang, Map.of(RU, "RU", EN,  "RU", DE,  "RU"),  initialLevel.level_LANGUAGER.getIdString() + "RU");
                buttonRepository.cache(button0_2_0_lang);
                row0_2_lang.add(button0_2_0_lang);
                Button button0_2_1_lang = new Button(row0_2_lang, Map.of(RU, "EN", EN,  "EN", DE,  "EN"),  initialLevel.level_LANGUAGER.getIdString() + "EN");
                buttonRepository.cache(button0_2_1_lang);
                row0_2_lang.add(button0_2_1_lang);
                Button button0_2_2_lang = new Button(row0_2_lang, Map.of(RU, "DE", EN,  "DE", DE,  "DE"),  initialLevel.level_LANGUAGER.getIdString() + "DE");
                buttonRepository.cache(button0_2_2_lang);
                row0_2_lang.add(button0_2_2_lang);

                //////////ОБРАБОТЧИК ВЫБОРА ЯЗЫКОВ
                initialLevel.level_LANGUAGER.updateLevel(Users, LANGUAGER.name(), initialLevel.level_LANGUAGES, false);

                levelRepository.cache(initialLevel.level_LANGUAGER);
                Message message00_1_r = new Message(initialLevel.level_LANGUAGER, Map.of(RU, "Language successfully changed"));
                messageRepository.cache(message00_1_r);
                initialLevel.level_LANGUAGER.addMessage(message00_1_r);

                //////////ДОБАВОЧНОЕ СТАРТОВОЕ МЕНЮ ДЛЯ МАГАЗИНА

                initialLevel.level_ADMIN.updateLevel(Users, ADMIN.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_ADMIN);

                ButtonRow row0_2_admin = new ButtonRow(initialLevel.level_ADMIN);
                buttonRowRepository.cache(row0_2_admin);
                initialLevel.level_ADMIN.addRow(row0_2_admin);
                Button button0_2_0_admin = new Button(row0_2_admin, Map.of(RU, "Создать магазин/сервис"), initialLevel.level_CONSTRUCT.getIdString());
                buttonRepository.cache(button0_2_0_admin);
                row0_2_admin.add(button0_2_0_admin);
                Button button0_2_1_admin = new Button(row0_2_admin, Map.of(RU, "Админить магазины/сервисы"), initialLevel.level_ADMIN_SHOPS.getIdString());
                buttonRepository.cache(button0_2_1_admin);
                row0_2_admin.add(button0_2_1_admin);


                //////////ДОБАВОЧНОЕ СТАРТОВОЕ МЕНЮ ДЛЯ МАГАЗИНА

                initialLevel.level_ADMIN_ADMIN.updateLevel(Users, ADMIN_ADMIN.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_ADMIN_ADMIN);
                ButtonRow row0_0_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_0_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_0_admin0);
                Button button0_0_0_shop0 = new Button(row0_0_admin0, Map.of(RU, "Акции"), initialLevel.level_ACTIONS.getIdString());
                buttonRepository.cache(button0_0_0_shop0);
                row0_0_admin0.add(button0_0_0_shop0);
                Button button0_0_1_shop0 = new Button(row0_0_admin0, Map.of(RU, "Корзины"), initialLevel.level_BASKETS_FOR_SHOP.getIdString());
                buttonRepository.cache(button0_0_1_shop0);
                row0_0_admin0.add(button0_0_1_shop0);
                ButtonRow row0_1_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_1_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_1_admin0);
                Button button0_1_0_shop0 = new Button(row0_1_admin0, Map.of(RU, "Товары"), initialLevel.level_GOODS_LIST.getIdString());
                buttonRepository.cache(button0_1_0_shop0);
                row0_1_admin0.add(button0_1_0_shop0);
                Button button0_1_1_shop0 = new Button(row0_1_admin0, Map.of(RU, "Партнеры"), initialLevel.level_PARTNERS.getIdString());
                buttonRepository.cache(button0_1_1_shop0);
                row0_1_admin0.add(button0_1_1_shop0);
                ButtonRow row0_2_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_2_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_2_admin0);
                Button button1_0_1 = new Button(row0_2_admin0, Map.of(RU, "Бот магазина"), initialLevel.level_SHOP_BOTS.getIdString());
                buttonRepository.cache(button1_0_1);
                row0_2_admin0.add(button1_0_1);
                Button button0_2_1_shop0 = new Button(row0_2_admin0, Map.of(RU, "Продавцы"), initialLevel.level_SELLERS.getIdString());
                buttonRepository.cache(button0_2_1_shop0);
                row0_2_admin0.add(button0_2_1_shop0);
                ButtonRow row0_3_admin0 = new ButtonRow(initialLevel.level_ADMIN_ADMIN);
                buttonRowRepository.cache(row0_3_admin0);
                initialLevel.level_ADMIN_ADMIN.addRow(row0_3_admin0);
                Button button0_3_1_shop0 = new Button(row0_3_admin0, Map.of(RU, "Geo-карта"), initialLevel.level_MAP.getIdString(), "https://t.me/Skido_bot/geomapper");
                buttonRepository.cache(button0_3_1_shop0);
                row0_3_admin0.add(button0_3_1_shop0);



                //////////КАРТА
                initialLevel.level_MAP.updateLevel(Users, GEOMAP.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_MAP);
                Message message00_1__ = new Message(initialLevel.level_MAP, Map.of(RU, "Перейдите на страницу редактрирования местоположения вашего объекта https://skidozona.by/geomapper"));
                messageRepository.cache(message00_1__);
                initialLevel.level_MAP.addMessage(message00_1__);
                ButtonRow row0_0_geo = new ButtonRow(initialLevel.level_MAP);
                buttonRowRepository.cache(row0_0_geo);
                initialLevel.level_MAP.addRow(row0_0_geo);
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
                Message message4603_1 = new Message(initialLevel.level_SELLERS_REMOVE_APPROVE, Map.of(RU, "Удаление отменено"));
                messageRepository.cache(message4603_1);
                initialLevel.level_SELLERS_REMOVE_APPROVE.addMessage(message4603_1);

                initialLevel.level_SELLERS_REMOVE_DISMISS.updateLevel(Users, SELLERS_REMOVE_DISMISS.name(), initialLevel.level_SELLERS_REMOVE, false);

                levelRepository.cache(initialLevel.level_SELLERS_REMOVE_DISMISS);
                Message message4604_1 = new Message(initialLevel.level_SELLERS_REMOVE_DISMISS, Map.of(RU, "Продавец удален"));
                messageRepository.cache(message4604_1);
                initialLevel.level_SELLERS_REMOVE_DISMISS.addMessage(message4604_1);


                ///////////////////////// СПИСОК ПАРТНЕРОВ

                initialLevel.level_PARTNERS.updateLevel(Users, PARTNERS.name(), initialLevel.level_ADMIN_ADMIN, false);

                levelRepository.cache(initialLevel.level_PARTNERS);
                Message message46_1 = new Message(initialLevel.level_PARTNERS, Map.of(RU, "Список моих партнеров: "));
                messageRepository.cache(message46_1);
                initialLevel.level_PARTNERS.addMessage(message46_1);
                ButtonRow row46_0 = new ButtonRow(initialLevel.level_PARTNERS);
                buttonRowRepository.cache(row46_0);
                Button button46_0_0 = new Button(row46_0, Map.of(RU, "Найти нового партнера"), initialLevel.level_SEARCH_PARTNER.getIdString());
                buttonRepository.cache(button46_0_0);
                row46_0.add(button46_0_0);
                initialLevel.level_PARTNERS.addRow(row46_0);

                ButtonRow row46_1 = new ButtonRow(initialLevel.level_PARTNERS);
                buttonRowRepository.cache(row46_1);
                Button button46_0_1 = new Button(row46_1, Map.of(RU, "Редактировать партнеров"), initialLevel.level_EDIT_PARTNER.getIdString());
                buttonRepository.cache(button46_0_1);
                row46_1.add(button46_0_1);
                initialLevel.level_PARTNERS.addRow(row46_1);

                ButtonRow row46_2 = new ButtonRow(initialLevel.level_PARTNERS);
                buttonRowRepository.cache(row46_2);
                Button button46_0_2 = new Button(row46_2, Map.of(RU, "Войти в новую группу кэшбеков"), initialLevel.level_SEARCH_GROUP.getIdString());
                buttonRepository.cache(button46_0_2);
                row46_2.add(button46_0_2);
                initialLevel.level_PARTNERS.addRow(row46_2);


                initialLevel.level_WITHDRAW_PARTNER.updateLevel(Users, WITHDRAW_PARTNER.name(), initialLevel.level_PARTNERS, false);

                levelRepository.cache(initialLevel.level_WITHDRAW_PARTNER);

                initialLevel.level_WITHDRAW_PARTNER_GROUP.updateLevel(Users, WITHDRAW_PARTNER_GROUP.name(), initialLevel.level_PARTNERS, false);

                levelRepository.cache(initialLevel.level_WITHDRAW_PARTNER_GROUP);

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

///это я проверил 12.05.2021 отсюда
                initialLevel.level_SEARCH_PARTNER.updateLevel(Users, SEARCH_PARTNER.name(), initialLevel.level_PARTNERS, false);

                levelRepository.cache(initialLevel.level_SEARCH_PARTNER);
                Message message47_1 = new Message(initialLevel.level_SEARCH_PARTNER, Map.of(RU, "Найдите нужный магазин. Введите название "));
                messageRepository.cache(message47_1);
                initialLevel.level_SEARCH_PARTNER.addMessage(message47_1);



                initialLevel.level_EDIT_PARTNER.updateLevel(Users, EDIT_PARTNER.name(), initialLevel.level_PARTNERS, false);

                levelRepository.cache(initialLevel.level_EDIT_PARTNER);
                Message message47_1_1_01 = new Message(initialLevel.level_EDIT_PARTNER, Map.of(RU, "Редактировать партнеров"));
                messageRepository.cache(message47_1_1_01);
                initialLevel.level_EDIT_PARTNER.addMessage(message47_1_1_01);



                initialLevel.level_SEARCH_PARTNER_RESP_BUTTON.updateLevel(Users, SEARCH_PARTNER_RESP_BUTTON.name(), initialLevel.level_SEARCH_PARTNER, true);

                levelRepository.cache(initialLevel.level_SEARCH_PARTNER_RESP_BUTTON);
                Message message47_1_1_1 = new Message(initialLevel.level_SEARCH_PARTNER_RESP_BUTTON, Map.of(RU, "Результаты поиска новых партнеров"));
                messageRepository.cache(message47_1_1_1);
                initialLevel.level_SEARCH_PARTNER_RESP_BUTTON.addMessage(message47_1_1_1);


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
/// 12.05.2021 досюда

                initialLevel.level_SEARCH_GROUP.updateLevel(Users, SEARCH_GROUP.name(), initialLevel.level_PARTNERS, false);

                levelRepository.cache(initialLevel.level_SEARCH_GROUP);
                Message message47_g_1 = new Message(initialLevel.level_SEARCH_GROUP, Map.of(RU, "Введите название группы"));
                messageRepository.cache(message47_g_1);
                initialLevel.level_SEARCH_GROUP.addMessage(message47_g_1);

                initialLevel.level_SEARCH_GROUP_RESP.updateLevel(Users, SEARCH_GROUP_RESP.name(), initialLevel.level_SEARCH_GROUP, true);

                levelRepository.cache(initialLevel.level_SEARCH_GROUP_RESP);
                Message message47_1_g_1 = new Message(initialLevel.level_SEARCH_GROUP_RESP, Map.of(RU, "Результаты поиска групп"));
                messageRepository.cache(message47_1_g_1);
                initialLevel.level_SEARCH_GROUP_RESP.addMessage(message47_1_g_1);

                initialLevel.level_SEARCH_GROUP_LIMIT.updateLevel(Users, SEARCH_GROUP_LIMIT.name(), initialLevel.level_SEARCH_GROUP_RESP, true);

                levelRepository.cache(initialLevel.level_SEARCH_GROUP_LIMIT);
                Message message47_2_g_1 = new Message(initialLevel.level_SEARCH_GROUP_LIMIT, Map.of(RU, "Введите размер лимита кэшбека по группе"));
                messageRepository.cache(message47_2_g_1);
                initialLevel.level_SEARCH_GROUP_LIMIT.addMessage(message47_2_g_1);

                initialLevel.level_SEARCH_GROUP_END.updateLevel(Users, SEARCH_GROUP_END.name(), initialLevel.level_SEARCH_GROUP_LIMIT, true);

                levelRepository.cache(initialLevel.level_SEARCH_GROUP_END);
                Message message4700_3_1 = new Message(initialLevel.level_SEARCH_GROUP_END, Map.of(RU, "Вы установили лимит WW для  EE"));
                messageRepository.cache(message4700_3_1);
                initialLevel.level_SEARCH_GROUP_END.addMessage(message4700_3_1);

                ///////выводится в цикле как результат поиска
                initialLevel.level_ADD_PARTNER.updateLevel(Users, ADD_PARTNER.name(), initialLevel.level_SEARCH_PARTNER_END, false);

                levelRepository.cache(initialLevel.level_ADD_PARTNER);
                ButtonRow row48_0 = new ButtonRow(initialLevel.level_ADD_PARTNER);
                buttonRowRepository.cache(row48_0);
                initialLevel.level_ADD_PARTNER.addRow(row48_0);
                Button button48_0_0 = new Button(row48_0, Map.of(RU, "Добавить партнера"), initialLevel.level_B2NOB.getIdString());
                buttonRepository.cache(button48_0_0);
                row48_0.add(button48_0_0);


                ///////выводится в цикле как результат поиска
//                    initialLevel.level_ADD_GROUP.updateLevel(CHAT, ADD_GROUP.name(), initialLevel.level_SEARCH_GROUP_LIMIT, false);
//                    levelRepository.cache(initialLevel.level_ADD_GROUP);
//                    ButtonRow row48_g_0 = new ButtonRow();
//                    Button button48_g_0_0 = new Button(initialLevel.level_ADD_GROUP, "Добавить групппу", initialLevel.level_NEW_GRUPP.getIdString());
//                    buttonRepository.cache(button48_g_0_0);
//                    initialLevel.level_ADD_GROUP.addRow(row48_g_0);



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
                Message message52_12 = new Message(initialLevel.level_SHOP_BOTS, Map.of(RU, "Мои боты: "));
                messageRepository.cache(message52_12);
                initialLevel.level_SHOP_BOTS.addMessage(message52_12);


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




                //////////Монитор

                initialLevel.level_MONITOR.updateLevel(Users, MONITOR.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_MONITOR);
                Message message123_10 = new Message(initialLevel.level_MONITOR, Map.of(RU, "Пришлите ссылку из карточки товара на Wildberries, Ozon, LaModa, 21 Век"));
                messageRepository.cache(message123_10);
                initialLevel.level_MONITOR.addMessage(message123_10);


                initialLevel.level_MONITOR_PRICE.updateLevel(Users, MONITOR_PRICE.name(), initialLevel.level_MONITOR, true);

                levelRepository.cache(initialLevel.level_MONITOR_PRICE);
                Message message123_11 = new Message(initialLevel.level_MONITOR_PRICE, Map.of(RU, "Товар успешно найден. Текущая цена ЦЦЦ."));
                messageRepository.cache(message123_11);
                initialLevel.level_MONITOR_PRICE.addMessage(message123_11);
                Message message123_12 = new Message(initialLevel.level_MONITOR_PRICE, Map.of(RU, "Пришлите целевую цену, при достижении которой будет отправлено уведомление"));
                messageRepository.cache(message123_12);
                initialLevel.level_MONITOR_PRICE.addMessage(message123_12);


                initialLevel.level_MONITOR_RESP.updateLevel(Users, MONITOR_RESP.name(), initialLevel.level_MONITOR_PRICE, true);

                levelRepository.cache(initialLevel.level_MONITOR_RESP);//Цена на ТТТ снизилась до ППП
                Message message123_13 = new Message(initialLevel.level_MONITOR_RESP, Map.of(RU, "Закладка сохранена!"));
                messageRepository.cache(message123_13);
                initialLevel.level_MONITOR_RESP.addMessage(message123_13);


                //////////КЭШБЕКИ

                initialLevel.level_CASHBACKS.updateLevel(Users, CASHBACKS.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_CASHBACKS);
                Message message1_1 = new Message(initialLevel.level_CASHBACKS, Map.of(RU, "Список активных кэшбеков:"));
                messageRepository.cache(message1_1);
                initialLevel.level_CASHBACKS.addMessage(message1_1);
                ButtonRow row01_0 = new ButtonRow(initialLevel.level_CASHBACKS);
                buttonRowRepository.cache(row01_0);
                Button button01_0_0 = new Button(row01_0, Map.of(RU, "Рекомендовать/ запрос кэшбека"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button01_0_0);
                row01_0.add(button01_0_0);

                initialLevel.level_CASHBACKS.addRow(row01_0);


                //////////СВЯЗАТЬ

                initialLevel.level_CONNECT.updateLevel(Users, CONNECT.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_CONNECT);
                Message message2_1 = new Message(initialLevel.level_CONNECT, 0, Map.of(RU, "ссылка"), IOUtils.toByteArray(qrInputStream("ссылка")), "ссылка");
                messageRepository.cache(message2_1);
                initialLevel.level_CONNECT.addMessage(message2_1);
                Message message2_2 = new Message(initialLevel.level_CONNECT, Map.of(RU, "Или перешлите ссылку:"));
                messageRepository.cache(message2_2);
                initialLevel.level_CONNECT.addMessage(message2_2);
                Message message2_3 = new Message(initialLevel.level_CONNECT, Map.of(RU, "https://t.me/Skido_Bot?start=userId"));
                messageRepository.cache(message2_3);
                initialLevel.level_CONNECT.addMessage(message2_3);


                //////////СВЯЗАТЬ С МАГАЗИНОМ
                initialLevel.level_CONNECT_SHOP.updateLevel(Users, CONNECT_SHOP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_CONNECT_SHOP);

                //////////ЗАКЛАДКИ

                initialLevel.level_BOOKMARKS.updateLevel(Users, BOOKMARKS.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_BOOKMARKS);
                Message message3_1 = new Message(initialLevel.level_BOOKMARKS, Map.of(RU, "Список закладок:"));
                messageRepository.cache(message3_1);
                initialLevel.level_BOOKMARKS.addMessage(message3_1);
                ButtonRow row3_0 = new ButtonRow(initialLevel.level_BOOKMARKS);
                buttonRowRepository.cache(row3_0);
                Button button3_0_0 = new Button(row3_0, Map.of(RU, "Показать магазину"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button3_0_0);
                row3_0.add(button3_0_0);
                initialLevel.level_BOOKMARKS.addRow(row3_0);


                initialLevel.level_ADD_BOOKMARK.updateLevel(Users, ADD_BOOKMARK.name(), initialLevel.level_SEARCH, false);

                levelRepository.cache(initialLevel.level_ADD_BOOKMARK);
                Message message3_1_1 = new Message(initialLevel.level_ADD_BOOKMARK, Map.of(RU, "Закладка на товар добавлена!"));
                messageRepository.cache(message3_1_1);
                initialLevel.level_ADD_BOOKMARK.addMessage(message3_1_1);
                ButtonRow row3_1 = new ButtonRow(initialLevel.level_ADD_BOOKMARK);
                buttonRowRepository.cache(row3_1);
                Button button3_1_1 = new Button(row3_1, Map.of(RU, "К поиску"), initialLevel.level_SEARCH.getIdString());
                buttonRepository.cache(button3_1_1);
                row3_1.add(button3_1_1);
                initialLevel.level_ADD_BOOKMARK.addRow(row3_1);


                //////////КОРЗИНА

                initialLevel.level_BASKET.updateLevel(Users, BASKET.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_BASKET);
                Message message4_1 = new Message(initialLevel.level_BASKET, Map.of(RU, "Товары в корзине:"));
                messageRepository.cache(message4_1);
                initialLevel.level_BASKET.addMessage(message4_1);
                ButtonRow row4_0 = new ButtonRow(initialLevel.level_BASKET);
                buttonRowRepository.cache(row4_0);
                Button button4_0_0 = new Button(row4_0, Map.of(RU, "Показать магазину"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button4_0_0);
                row4_0.add(button4_0_0);
                initialLevel.level_BASKET.addRow(row4_0);


                ////////// АРХИВ КОРЗИН

                initialLevel.level_BASKET_ARCHIVE.updateLevel(Users, BASKET_ARCHIVE.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_BASKET_ARCHIVE);
                Message message400_1 = new Message(initialLevel.level_BASKET_ARCHIVE, Map.of(RU, "Архив корзин:"));
                messageRepository.cache(message400_1);
                initialLevel.level_BASKET_ARCHIVE.addMessage(message400_1);
                ButtonRow row400_0 = new ButtonRow(initialLevel.level_BASKET_ARCHIVE);
                buttonRowRepository.cache(row400_0);
                Button button400_0_0 = new Button(row400_0, Map.of(RU, "Показать магазину"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button400_0_0);
                row400_0.add(button400_0_0);
                initialLevel.level_BASKET_ARCHIVE.addRow(row400_0);




                initialLevel.level_ADD_BASKET.updateLevel(Users, ADD_BASKET.name(), initialLevel.level_SEARCH, false);

                levelRepository.cache(initialLevel.level_ADD_BASKET);
                Message message4_1_1 = new Message(initialLevel.level_ADD_BASKET, Map.of(RU, "Товар добавлен в корзину!"));
                messageRepository.cache(message4_1_1);
                initialLevel.level_ADD_BASKET.addMessage(message4_1_1);
                ButtonRow row4_1 = new ButtonRow(initialLevel.level_ADD_BASKET);
                buttonRowRepository.cache(row4_1);
                Button button4_1_1 = new Button(row4_1, Map.of(RU, "К поиску"), initialLevel.level_SEARCH.getIdString());
                buttonRepository.cache(button4_1_1);
                row4_1.add(button4_1_1);
                initialLevel.level_ADD_BASKET.addRow(row4_1);


                //////////МАГАЗИНЫ ДЛЯ ПОКУПАТЕЛЯ

                initialLevel.level_MY_SHOPS.updateLevel(Users, MY_SHOPS.name(), initialLevel.level_SEARCH_RESULT_PRODUCT, true);

                levelRepository.cache(initialLevel.level_MY_SHOPS);
                Message message001_1 = new Message(initialLevel.level_MY_SHOPS, Map.of(RU, "Список моих магазинов"));
                messageRepository.cache(message001_1);
                initialLevel.level_MY_SHOPS.addMessage(message001_1);
                ButtonRow row001_0 = new ButtonRow(initialLevel.level_MY_SHOPS);
                buttonRowRepository.cache(row001_0);
                Button button001_0_0 = new Button(row001_0, Map.of(RU, "Рекомендовать/ запрос кэшбека"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button001_0_0);
                row001_0.add(button001_0_0);
//                Button button001_0_1 = new Button(initialLevel.level_CONSTRUCT_ADD, "Мои боты", initialLevel.level_SHOP_BOTS.getIdString());
//                buttonRepository.cache(button001_0_1);
//                row001_0.add(button001_0_1);
                initialLevel.level_MY_SHOPS.addRow(row001_0);


                //////////ПОИСК ТОВАРА ПО КАТЕГОРИЯМ

                initialLevel.level_SEARCH.updateLevel(Users, SEARCH.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_SEARCH);
                Message message15_1 = new Message(initialLevel.level_SEARCH, Map.of(RU, "Введите название для поиска:"));
                messageRepository.cache(message15_1);
                initialLevel.level_SEARCH.addMessage(message15_1);

                menuCreator.createMenu(initialLevel.level_SEARCH, MenuTypeEnum.SEARCH_LEVEL_CHOICER, Users);

                initialLevel.level_SEARCH_RESULT.updateLevel(Users, SEARCH_RESULT.name(), initialLevel.level_SEARCH, false);

                levelRepository.cache(initialLevel.level_SEARCH_RESULT);
                Message message52_1_1 = new Message(initialLevel.level_SEARCH_RESULT, Map.of(RU, "Результат поиска"));
                messageRepository.cache(message52_1_1);
                initialLevel.level_SEARCH_RESULT.addMessage(message52_1_1);


                initialLevel.level_SEARCH_RESULT_PRODUCT.updateLevel(Users, SEARCH_RESULT_PRODUCT.name(), initialLevel.level_SEARCH_RESULT, false);

                levelRepository.cache(initialLevel.level_SEARCH_RESULT_PRODUCT);
                Message message521_1_1 = new Message(initialLevel.level_SEARCH_RESULT_PRODUCT, Map.of(RU, "Подробнее: "));
                messageRepository.cache(message521_1_1);
                initialLevel.level_SEARCH_RESULT_PRODUCT.addMessage(message521_1_1);

                //////////СОЗДАНИЕ МАГАЗИНА С ТОВАРАМИ

                initialLevel.level_CONSTRUCT.updateLevel(Users, CONSTRUCT.name(), initialLevel.level_INITIALIZE, false);
                levelRepository.cache(initialLevel.level_CONSTRUCT);
                Message message6_1 = new Message(initialLevel.level_CONSTRUCT, Map.of(RU, "Введите название магазина/сервиса"));
                messageRepository.cache(message6_1);
                initialLevel.level_CONSTRUCT.addMessage(message6_1);


//                initialLevel.level_CONSTRUCT_MIN_BILL_SHARE.updateLevel(Users, CONSTRUCT_MIN_BILL_SHARE.name(), initialLevel.level_CONSTRUCT, true);
//                levelRepository.cache(initialLevel.level_CONSTRUCT_MIN_BILL_SHARE);
//                Message message6_0_1 = new Message(initialLevel.level_CONSTRUCT_MIN_BILL_SHARE, Map.of(LanguageEnum.ru, "Введите размер минимальной скидки по чеку, %"));
//                messageRepository.cache(message6_0_1);
//                initialLevel.level_CONSTRUCT_MIN_BILL_SHARE.addMessage(message6_0_1);



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


                //////////СОЗДАНИЕ ВВОДА ТОВАРОВ
// проверил 12.05 отсюда
                initialLevel.level_ADD_GOODS.updateLevel(Users, ADD_GOODS.name(), initialLevel.level_CONSTRUCT_ADD, false);

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
                row16_0.add(button16_0_0);

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
                        IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream(MANAGEMENT_FILE)),
                        "как отправить геолокацию");
                messageRepository.cache(message17_2);
                initialLevel.level_ADD_TAXI_BOT.addMessage(message17_2);
//                        ButtonRow row17_0 = new ArrayList<>();
//                        Button button17_0_0 = new Button(level17, "Такси бот", TAXI_BOT.name());
//                        buttonRepository.cache(button17_0_0);
//                        row17_0.add(button17_0_0);
//                        level17.addRow(row17_0);
//                        ButtonRow row17_1 = new ArrayList<>();
//                        Button button17_1_0 = new Button(level17, "Вернуться к выбору шаблона", ADD_BOT.name());
//                        buttonRepository.cache(button17_1_0);
//                        row17_1.add(button17_1_0);
//                        level17.addRow(row17_1);

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

//                            Button yesButton = new Button(initialLevel.level_EDIT_MESSAGE,"Да", EDIT_MESSAGE_RESP.name());
//                            buttonRepository.cache(yesButton);
//                            ButtonRow editRow = new ArrayList<>();
//                            editRow.add(yesButton);
//                            Button noButton = new Button(initialLevel.level_EDIT_MESSAGE,"Нет", SAVE_USER_PARAMETER.name());
//                            buttonRepository.cache(noButton);
//                            editRow.add(noButton);
//                            initialLevel.level_EDIT_MESSAGE.addRow(editRow);
//                            levelRepository.cache(initialLevel.level_EDIT_MESSAGE);
//
//                            Level saveParameterLevel.updateLevel(CHAT, SAVE_USER_PARAMETER.name(), initialLevel.level_TAXI_LOCATION, true);
//                            Message messageSaveParameter = new Message(saveParameterLevel, Map.of(LanguageEnum.ru, "Сохранить ввод пользователя в параметр?", null, null, null, null);
//                            saveParameterLevel.addMessage(messageSaveParameter);
//                            Button yesButton2 = new Button(saveParameterLevel,"Да", SAVE_BOT_PARAMETER.name());
//                            buttonRepository.cache(yesButton2);
//                            ButtonRow editRow2 = new ArrayList<>();
//                            editRow2.add(yesButton2);
//                            Button noButton2 = new Button(saveParameterLevel,"Нет", "no");
//                            buttonRepository.cache(noButton2);
//                            editRow2.add(noButton2);
//                            saveParameterLevel.addRow(editRow2);
//                            levelRepository.cache(saveParameterLevel);
//
//                            Level addParameterNameLevel.updateLevel(CHAT, SAVE_BOT_PARAMETER.name(), saveParameterLevel, false);
//                            Message addParameterNameMessage = new Message(addParameterNameLevel, Map.of(LanguageEnum.ru, "Введите название параметра. Вы сможете использовать его в сообщениях и подписях кнопокс %, например - %name", null, null, null, null);
//                            addParameterNameLevel.addMessage(addParameterNameMessage);
//                            levelRepository.cache(addParameterNameLevel);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ BK, BM, CB -И ПОЛЛУЧАТЕЛь PI

                initialLevel.level_PSHARE2P.updateLevel(Users, PSHARE2P.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_PSHARE2P);
                Message message19_1 = new Message(initialLevel.level_PSHARE2P, Map.of(RU, "Вы получили рекомендацию от X"));
                messageRepository.cache(message19_1);
                initialLevel.level_PSHARE2P.addMessage(message19_1);

                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ PI -И ПОЛЛУЧАТЕЛЯ PI НЕТ ЕЩЕ В СИСТЕМЕ

                initialLevel.level_P2NOP.updateLevel(Users, P2NOP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2NOP);
                Message message20_1 = new Message(initialLevel.level_P2NOP, Map.of(RU, "Добро пожаловать в систему Skidozona!"));
                messageRepository.cache(message20_1);
                initialLevel.level_P2NOP.addMessage(message20_1);


                initialLevel.level_P2NOP_RESP.updateLevel(Users, P2NOP_RESP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2NOP_RESP);
                Message message20_1_1 = new Message(initialLevel.level_P2NOP_RESP, Map.of(RU, "Пользователю Y успешно добавлен в систему!"));
                messageRepository.cache(message20_1_1);
                initialLevel.level_P2NOP_RESP.addMessage(message20_1_1);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ PI -И ПОЛЛУЧАТЕЛь PI УЖЕ ЕСТЬ В СИСТЕМЕ

                initialLevel.level_P2P.updateLevel(Users, P2P.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2P);
                Message message21_1 = new Message(initialLevel.level_P2P, Map.of(RU, "Пользователь X пытался добавить Вас в систему"));
                messageRepository.cache(message21_1);
                initialLevel.level_P2P.addMessage(message21_1);


                initialLevel.level_P2P_RESP.updateLevel(Users, P2P_RESP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2P_RESP);
                Message message21_1_1 = new Message(initialLevel.level_P2P_RESP, Map.of(RU, "Пользователь Y уже есть в системе"));
                messageRepository.cache(message21_1_1);
                initialLevel.level_P2P_RESP.addMessage(message21_1_1);


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

                System.out.println("PRE addFinalButton");

                addFinalButton(initialLevel.level_INITIALIZE);

                System.out.println("POST addFinalButton");

            } catch (IOException | WriterException e) {
                e.printStackTrace();
            }
    }

    private boolean isBotLevel = false;


    private void addFinalButton(Level level) {

        List<ButtonRow> buttonRows = buttonRowRepository.findAllByLevel_Id(level.getId());

        if (buttonRows != null && !buttonRows.isEmpty()) {

            ButtonRow backRow = new ButtonRow(level);
            buttonRowRepository.cache(backRow);

            Button backButton = new Button(backRow, Map.of(RU, "В начало"), initialLevel.level_INITIALIZE.getIdString());
            buttonRepository.cache(backButton);
            backRow.add(backButton);
            buttonRowRepository.cache(backRow);
            //level.addRow(backRow);
        }
        levelRepository.cache(level);

        List<Level> levels = levelRepository.findAllByParentLevelId(level.getId());

        System.out.println("level.getId() " + level.getId() + " levels  " + levels);

        for (Level childLevel : levels) {
            addFinalButton(childLevel);
        }
    }
}
