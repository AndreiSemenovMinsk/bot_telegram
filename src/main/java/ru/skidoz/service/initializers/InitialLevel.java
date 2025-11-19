package ru.skidoz.service.initializers;


import static ru.skidoz.model.entity.category.LanguageEnum.RU;

import java.util.List;
import java.util.stream.Collectors;

import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class InitialLevel {

    public static User Users = null;

    public static Shop SHOP = null;

    public static CategorySuperGroup bigCategorySuperGroup = null;

    public static CategoryGroup bigCategoryGroup = null;

    public static Category bigCategory = null;

    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowRepository;
    @Autowired
    private ButtonCacheRepository buttonRepository;
    @Autowired
    private MessageCacheRepository messageRepository;
    @Autowired
    private UserCacheRepository userRepository;

    public Level level_INITIALIZE222;
    public Level level_INITIALIZE0;
    public Level level_INITIALIZE;
    public Level level_TOKEN_REQUEST;
    public Level level_ADMIN;
    public Level level_ADMIN_ADMIN;
    public Level level_ADD_GOODS_XLS;
    public Level level_LANGUAGES;
    public Level level_LANGUAGER;
    public Level level_GEOMAP;
    public Level level_MONITOR;
    public Level level_MONITOR_PRICE;
    public Level level_MONITOR_RESP;
    public Level level_GOODS_LIST;
    public Level level_SHOP_BOTS;
    public Level level_PARTNERS;
    public Level level_ADD_SHOP_TO_SHOP_GROUP;
    public Level level_VOTE_ADD_SHOP_GROUP;
    public Level level_SELLERS;
    public Level level_SELLERS_REMOVE;
    public Level level_SELLERS_ADD;
    public Level level_SELLERS_ADD_APPROVE;
    public Level level_SELLERS_ADD_DISMISS;
    public Level level_SELLERS_REMOVE_APPROVE;
    public Level level_SELLERS_REMOVE_DISMISS;
    public Level level_BASIC;
    public Level level_WITHDRAW_PARTNER;
    public Level level_WITHDRAW_PARTNER_GROUP;
    public Level level_WITHDRAW_PARTNER_RESP;
    public Level level_WITHDRAW_PARTNER_END;
    public Level level_SEARCH_PARTNER;
    public Level level_SEARCH_PARTNER_RESP;
    public Level level_SEARCH_PARTNER_RESP_BUTTON;
    public Level level_SEARCH_PARTNER_RATE;
    public Level level_SEARCH_PARTNER_LIMIT;
    public Level level_SEARCH_PARTNER_END;
    public Level level_EDIT_PARTNER;
    public Level level_CREATE_GROUP;
    public Level level_CREATE_GROUP_RESP;
    public Level level_CREATE_GROUP_LIMIT;
    public Level level_ADD_PARTNER;
    public Level level_ADD_GROUP;
    public Level level_ACTIONS;
    public Level level_BASKETS_FOR_SHOP;
    public Level level_ACTION_TYPE;
    public Level level_SELECT_LEVEL_TYPE;
    public Level level_MULTI_ACTION_LEVEL;
    public Level level_MULTI_ACTION_LEVEL_BASIC;
    public Level level_MULTI_LEVEL_RATE;
    public Level level_MULTI_LEVEL_RATE_BASIC;
    public Level level_MULTI_LEVEL_QUESTION;
    public Level level_MULTI_LEVEL_QUESTION_BASIC;
    public Level level_ONE_LEVEL_RATE;
    public Level level_ONE_LEVEL_RATE_BASIC;
    public Level level_ACTION_RATE_WITHDRAW;
    public Level level_ACTION_RATE_WITHDRAW_BASIC;
    public Level level_ACTION_RATE_PARTNER;
    public Level level_ADD_ACTION_RATE_SOURCE;
    public Level level_ADD_ACTION_COUPON_TARGET;
    public Level level_COUPON;
    public Level level_COUPON_NUMBER;
    public Level level_COUPON_RATE_WITHDRAW;
    public Level level_ADD_ACTION_COUPON_SOURCE;
    public Level level_ADD_ACTION_RATE_TARGET;
    public Level level_LINK_TO_PRODUCT;
    public Level level_LINK_TO_PRODUCT_NUMBER;
    public Level level_ADD_ACTION_LINK_SOURCE;
    public Level level_ADD_ACTION_LINK_TARGET;
    public Level level_MY_SHOPS;
    public Level level_CASHBACKS;
    public Level level_CONNECT;
    public Level level_CONNECT_SHOP;
    public Level level_BOOKMARKS;
    public Level level_ADD_BOOKMARK;
    public Level level_BASKET;
    public Level level_BASKET_ARCHIVE;
    public Level level_ADD_BASKET;
    public Level level_SEARCH;
    public Level level_SEARCH_RESULT;
    public Level level_SEARCH_RESULT_PRODUCT;
    public Level level_CONSTRUCT;
    public Level level_ADMIN_SHOPS;
    public Level level_CONSTRUCT_SARAFAN_SHARE;
    public Level level_CONSTRUCT_MIN_BILL_SHARE;
    public Level level_CONSTRUCT_ADD;
    public Level level_ADD_GOODS;
    public Level level_ADD_GOODS_NAME;
    public Level level_ADD_GOODS_PHOTO;
    public Level level_ADD_GOODS_DESCRIPTION;
    public Level level_ADD_GOODS_PRICE;
    public Level level_ADD_GOODS_END;
    public Level level_ADD_BOT;
    public Level level_ADD_TAXI_BOT;
    public Level level_TAXI_LOCATION;
    public Level level_TAXI_SUBMIT;
    public Level level_PSHARE2P;
    public Level level_P2NOP;
    public Level level_P2NOP_RESP;
    public Level level_P2P;
    public Level level_P2P_RESP;
    public Level level_P2B;
    public Level level_NEGATIVE_SUM;
    public Level level_NEGATIVE_COUNT;
    public Level level_P2B_PROPOSE_CASHBACK;
    public Level level_P2B_PROPOSE_CASHBACK_RESP;
    public Level level_P2B_WRITEOFF_CASHBACK;
    public Level level_P2B_WRITEOFF_CASHBACK_RESP;
    public Level level_P2B_WRITEOFF_CASHBACK_REQUEST;
    public Level level_P2B_WRITEOFF_CASHBACK_APPROVE;
    public Level level_P2B_WRITEOFF_CASHBACK_DISMISS;
    public Level level_P2B_CHARGE_BASKET_CASHBACK;
    public Level level_P2B_APPROVE_BASKET_CASHBACK;
    public Level level_P2B_CHARGE_COUPON;
    public Level level_P2B_CHARGE_COUPON_REQUEST;
    public Level level_P2B_CHARGE_COUPON_RESP;
    public Level level_P2B_CHARGE_COUPON_BASKET;
    public Level level_P2B_WRITEOFF_COUPON;
    public Level level_P2B_WRITEOFF_COUPON_SELECT_ACTION;
    public Level level_P2B_WRITEOFF_COUPON_BASKET;
    public Level level_P2B_WRITEOFF_COUPON_REQUEST;
    public Level level_P2B_WRITEOFF_COUPON_RESP;
    public Level level_P2B_WRITEOFF_COUPON_APPROVE;
    public Level level_P2B_RESP;
    public Level level_B2B;
    public Level level_B2NOB;
    public Level level_DISCARD_NEW_PARTNER;
    public Level level_APPROVE_NEW_PARTNER;
    public Level level_NEW_GRUPP;
    public Level level_DISCARD_NEW_GRUPP;
    public Level level_APPROVE_NEW_GRUPP;
    public Level level0_1_1;
    public Level level_RESPONSE_BUYER_MESSAGE;
    public Level level_RESPONSE_SHOP_MESSAGE;
    public Level level_NON_RESPONSE;
    public Level level0_1_3;
    public Level level0_1_4;
    public Level level0_1_5;
    public Level level_EDIT_BUTTON_NAME;
    public Level level_EDIT_MESSAGE;
    public Level level_NEW_LEVEL_END_BUTTON;
    public Level level_NEW_LEVEL_INPUT_BUTTON;
    public Level level_NEW_LEVEL_BUTTON;
    
    public void initLevels() {

        level_INITIALIZE222 = levelRepository.cache(new Level());
        level_INITIALIZE0 = levelRepository.cache(new Level());
        level_INITIALIZE = levelRepository.cache(new Level());
        level_TOKEN_REQUEST = levelRepository.cache(new Level());

        level_ADMIN = levelRepository.cache(new Level());
        level_ADMIN_ADMIN = levelRepository.cache(new Level());
        level_ADD_GOODS_XLS = levelRepository.cache(new Level());
        level_LANGUAGES = levelRepository.cache(new Level());
        level_LANGUAGER = levelRepository.cache(new Level());
        level_GEOMAP = levelRepository.cache(new Level());
        level_MONITOR = levelRepository.cache(new Level());
        level_MONITOR_PRICE = levelRepository.cache(new Level());
        level_MONITOR_RESP = levelRepository.cache(new Level());
        level_GOODS_LIST = levelRepository.cache(new Level());
        level_SHOP_BOTS = levelRepository.cache(new Level());
        level_PARTNERS = levelRepository.cache(new Level());
        level_ADD_SHOP_TO_SHOP_GROUP = levelRepository.cache(new Level());
        level_VOTE_ADD_SHOP_GROUP = levelRepository.cache(new Level());
        level_SELLERS = levelRepository.cache(new Level());
        level_SELLERS_REMOVE = levelRepository.cache(new Level());
        level_SELLERS_ADD = levelRepository.cache(new Level());
        level_SELLERS_ADD_APPROVE = levelRepository.cache(new Level());
        level_SELLERS_ADD_DISMISS = levelRepository.cache(new Level());
        level_SELLERS_REMOVE_APPROVE = levelRepository.cache(new Level());
        level_SELLERS_REMOVE_DISMISS = levelRepository.cache(new Level());
        level_BASIC = levelRepository.cache(new Level());
        level_WITHDRAW_PARTNER = levelRepository.cache(new Level());
        level_WITHDRAW_PARTNER_GROUP = levelRepository.cache(new Level());
        level_WITHDRAW_PARTNER_RESP = levelRepository.cache(new Level());
        level_WITHDRAW_PARTNER_END = levelRepository.cache(new Level());
        level_SEARCH_PARTNER = levelRepository.cache(new Level());
        level_SEARCH_PARTNER_RESP = levelRepository.cache(new Level());
        level_SEARCH_PARTNER_RESP_BUTTON = levelRepository.cache(new Level());
        level_SEARCH_PARTNER_RATE = levelRepository.cache(new Level());
        level_SEARCH_PARTNER_LIMIT = levelRepository.cache(new Level());
        level_SEARCH_PARTNER_END = levelRepository.cache(new Level());
        level_EDIT_PARTNER = levelRepository.cache(new Level());
        level_CREATE_GROUP = levelRepository.cache(new Level());
        level_CREATE_GROUP_RESP = levelRepository.cache(new Level());
        level_CREATE_GROUP_LIMIT = levelRepository.cache(new Level());
        level_ADD_PARTNER = levelRepository.cache(new Level());
        level_ADD_GROUP = levelRepository.cache(new Level());
        level_ACTIONS = levelRepository.cache(new Level());
        level_BASKETS_FOR_SHOP = levelRepository.cache(new Level());
        level_ACTION_TYPE = levelRepository.cache(new Level());
        level_SELECT_LEVEL_TYPE = levelRepository.cache(new Level());
        level_MULTI_ACTION_LEVEL = levelRepository.cache(new Level());
        level_MULTI_ACTION_LEVEL_BASIC = levelRepository.cache(new Level());
        level_MULTI_LEVEL_RATE = levelRepository.cache(new Level());
        level_MULTI_LEVEL_RATE_BASIC = levelRepository.cache(new Level());
        level_MULTI_LEVEL_QUESTION = levelRepository.cache(new Level());
        level_MULTI_LEVEL_QUESTION_BASIC = levelRepository.cache(new Level());
        level_ONE_LEVEL_RATE = levelRepository.cache(new Level());
        level_ONE_LEVEL_RATE_BASIC = levelRepository.cache(new Level());
        level_ACTION_RATE_WITHDRAW = levelRepository.cache(new Level());
        level_ACTION_RATE_WITHDRAW_BASIC = levelRepository.cache(new Level());
        level_ACTION_RATE_PARTNER = levelRepository.cache(new Level());
        level_ADD_ACTION_RATE_SOURCE = levelRepository.cache(new Level());
        level_ADD_ACTION_COUPON_TARGET = levelRepository.cache(new Level());
        level_COUPON = levelRepository.cache(new Level());
        level_COUPON_NUMBER = levelRepository.cache(new Level());
        level_COUPON_RATE_WITHDRAW = levelRepository.cache(new Level());
        level_ADD_ACTION_COUPON_SOURCE = levelRepository.cache(new Level());
        level_ADD_ACTION_RATE_TARGET = levelRepository.cache(new Level());
        level_LINK_TO_PRODUCT = levelRepository.cache(new Level());
        level_LINK_TO_PRODUCT_NUMBER = levelRepository.cache(new Level());
        level_ADD_ACTION_LINK_SOURCE = levelRepository.cache(new Level());
        level_ADD_ACTION_LINK_TARGET = levelRepository.cache(new Level());
        level_MY_SHOPS = levelRepository.cache(new Level());
        level_CASHBACKS = levelRepository.cache(new Level());
        level_CONNECT = levelRepository.cache(new Level());
        level_CONNECT_SHOP = levelRepository.cache(new Level());
        level_BOOKMARKS = levelRepository.cache(new Level());
        level_ADD_BOOKMARK = levelRepository.cache(new Level());
        level_BASKET = levelRepository.cache(new Level());
        level_BASKET_ARCHIVE = levelRepository.cache(new Level());
        level_ADD_BASKET = levelRepository.cache(new Level());
        level_SEARCH = levelRepository.cache(new Level());
        level_SEARCH_RESULT = levelRepository.cache(new Level());
        level_SEARCH_RESULT_PRODUCT = levelRepository.cache(new Level());
        level_CONSTRUCT = levelRepository.cache(new Level());
        level_ADMIN_SHOPS = levelRepository.cache(new Level());
        level_CONSTRUCT_SARAFAN_SHARE = levelRepository.cache(new Level());
        level_CONSTRUCT_MIN_BILL_SHARE = levelRepository.cache(new Level());
        level_CONSTRUCT_ADD = levelRepository.cache(new Level());
        level_ADD_GOODS = levelRepository.cache(new Level());
        level_ADD_GOODS_NAME = levelRepository.cache(new Level());
        level_ADD_GOODS_PHOTO = levelRepository.cache(new Level());
        level_ADD_GOODS_DESCRIPTION = levelRepository.cache(new Level());
        level_ADD_GOODS_PRICE = levelRepository.cache(new Level());
        level_ADD_GOODS_END = levelRepository.cache(new Level());
        level_ADD_BOT = levelRepository.cache(new Level());
        level_ADD_TAXI_BOT = levelRepository.cache(new Level());
        level_TAXI_LOCATION = levelRepository.cache(new Level());
        level_TAXI_SUBMIT = levelRepository.cache(new Level());
        level_PSHARE2P = levelRepository.cache(new Level());
        level_P2NOP = levelRepository.cache(new Level());
        level_P2NOP_RESP = levelRepository.cache(new Level());
        level_P2P = levelRepository.cache(new Level());
        level_P2P_RESP = levelRepository.cache(new Level());
        level_P2B = levelRepository.cache(new Level());
        level_NEGATIVE_SUM = levelRepository.cache(new Level());
        level_NEGATIVE_COUNT = levelRepository.cache(new Level());
        level_P2B_PROPOSE_CASHBACK = levelRepository.cache(new Level());
        level_P2B_PROPOSE_CASHBACK_RESP = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_CASHBACK = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_CASHBACK_RESP = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_CASHBACK_REQUEST = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_CASHBACK_APPROVE = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_CASHBACK_DISMISS = levelRepository.cache(new Level());
        level_P2B_CHARGE_BASKET_CASHBACK = levelRepository.cache(new Level());
        level_P2B_APPROVE_BASKET_CASHBACK = levelRepository.cache(new Level());
        level_P2B_CHARGE_COUPON = levelRepository.cache(new Level());
        level_P2B_CHARGE_COUPON_REQUEST = levelRepository.cache(new Level());
        level_P2B_CHARGE_COUPON_RESP = levelRepository.cache(new Level());
        level_P2B_CHARGE_COUPON_BASKET = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_COUPON = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_COUPON_SELECT_ACTION = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_COUPON_BASKET = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_COUPON_REQUEST = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_COUPON_RESP = levelRepository.cache(new Level());
        level_P2B_WRITEOFF_COUPON_APPROVE = levelRepository.cache(new Level());
        level_P2B_RESP = levelRepository.cache(new Level());
        level_B2B = levelRepository.cache(new Level());
        level_B2NOB = levelRepository.cache(new Level());
        level_DISCARD_NEW_PARTNER = levelRepository.cache(new Level());
        level_APPROVE_NEW_PARTNER = levelRepository.cache(new Level());
        level_NEW_GRUPP = levelRepository.cache(new Level());
        level_DISCARD_NEW_GRUPP = levelRepository.cache(new Level());
        level_APPROVE_NEW_GRUPP = levelRepository.cache(new Level());
        level0_1_1 = levelRepository.cache(new Level());
        level_RESPONSE_BUYER_MESSAGE = levelRepository.cache(new Level());
        level_RESPONSE_SHOP_MESSAGE = levelRepository.cache(new Level());
        level_NON_RESPONSE = levelRepository.cache(new Level());
        level0_1_3 = levelRepository.cache(new Level());
        level0_1_4 = levelRepository.cache(new Level());
        level0_1_5 = levelRepository.cache(new Level());
        level_EDIT_BUTTON_NAME = levelRepository.cache(new Level());
        level_EDIT_MESSAGE = levelRepository.cache(new Level());
        level_NEW_LEVEL_END_BUTTON = levelRepository.cache(new Level());
        level_NEW_LEVEL_INPUT_BUTTON = levelRepository.cache(new Level());
        level_NEW_LEVEL_BUTTON = levelRepository.cache(new Level());


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
    }


    public List<Message> retrieveMessages(Level level) {
        return messageRepository.findAllByLevel_Id(level.getId());
    }

    public List<ButtonRow> retrieveButtonRows(Level level) {
        return buttonRowRepository.findAllByLevel_Id(level.getId());
                /*.stream().peek(row -> System.out.println("row.getId()---" + row.getId()))
                .peek(row -> System.out.println("row.getId()+++" + buttonCacheRepository.findByButtonRowId(row.getId())))
                .peek(row -> row.getButtonList()
                        .addAll(buttonCacheRepository.findByButtonRowId(row.getId())))
                .collect(Collectors.toList());*/
        //level.setButtonRows(buttonRowList);
    }

    public LevelDTOWrapper convertToLevel(Level level, boolean retrieveMessages, boolean retrieveButtonRows) {
        return new LevelDTOWrapper(e -> {
            e.setLevel(level);
            if (retrieveMessages) {
                e.setMessages(retrieveMessages(level));
            }
            if (retrieveButtonRows) {
                e.setButtonRows(retrieveButtonRows(level));
            }
        });
    }

    public LevelDTOWrapper convertToLevel(Integer levelId, boolean retrieveMessages, boolean retrieveButtonRows) {

        Level level = levelRepository.findById(levelId);
        return new LevelDTOWrapper(e -> {
            e.setLevel(level);
            if (retrieveMessages) {
                e.setMessages(retrieveMessages(level));
            }
            if (retrieveButtonRows) {
                e.setButtonRows(retrieveButtonRows(level));
            }
        });
    }


    public Level cloneLevel(Level oldLevel, User users) {
        return cloneLevel(oldLevel, users, true, true);
    }


    public Level cloneLevel(Level oldLevel, User users, boolean copyButtons, boolean copyMessages) {

        Level newLevel = new Level();
        newLevel.setUserId(users.getId());
        newLevel.setCallName(oldLevel.getCallName());
        newLevel.setSourceIsMessage(oldLevel.isSourceIsMessage());
        newLevel.setParentLevelId(oldLevel.getParentLevelId());
        newLevel.setBotLevel(oldLevel.isBotLevel());
        newLevel.setTerminateBotLevel(oldLevel.isTerminateBotLevel());
        newLevel.setChatId(users.getChatId());

        levelRepository.cache(newLevel);

        if (copyMessages) {
            newLevel.setMessages(retrieveMessages(oldLevel).parallelStream().map(oldMessage -> {
                Message newMessage = new Message(
                        newLevel,
                        oldMessage.getLevelID(),
                        oldMessage.getNameEN(),
                        oldMessage.getNameRU(),
                        oldMessage.getNameDE(),
                        oldMessage.getImage(),
                        oldMessage.getImageDescription(),
                        oldMessage.getLongitude(),
                        oldMessage.getLatitude());

                messageRepository.cache(newMessage);
                return newMessage;
            }).collect(Collectors.toList()));
        }
        //newLevel.childLevels = new ArrayList<>(childLevels);
        System.out.println("copyButtons***" + copyButtons);

        if (copyButtons) {
            newLevel.setButtonRows(retrieveButtonRows(oldLevel).parallelStream().map(oldButtonRow -> {

                ButtonRow newButtonRow = new ButtonRow(newLevel);
                buttonRowRepository.cache(newButtonRow);

                System.out.println("oldButtonRow**" + oldButtonRow.getId() + "   at level " + newLevel);

//                List<Button> buttonList =
                        oldButtonRow.getButtonList().forEach(oldButton -> {
                    try {
//                        System.out.println("oldButton**" + oldButton.getId() + "+++++callback-" + oldButton.getCallback());

                        Button newButton = oldButton.clone(newButtonRow);

//                        System.out.println("newButton**" + oldButton.getId() + "+++++callback-" + oldButton.getCallback());
//
                        buttonRepository.cache(newButton);
                        //return newButton;
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    //return oldButton;
                });

                        //.collect(Collectors.toList());

                return newButtonRow;
            }).collect(Collectors.toList()));
        }
        levelRepository.cache(newLevel);
        return newLevel;
    }
}
