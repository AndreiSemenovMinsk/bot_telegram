package ru.skidoz.service;


import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class InitialLevelOld {


/*
    public static Users Users = null;

    public static Shop SHOP = null;

    public static CategorySuperGroup bigCategorySuperGroup = null;

    public static CategoryGroup bigCategoryGroup = null;

    public static Category bigCategoryDTO = null;

    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupCacheRepository;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupCacheRepository;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;

    @Autowired
    private LevelRepository levelRepositoryJPA;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private BotRepository botRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowCacheRepository;
    @Autowired
    private ButtonRowRepository buttonRowRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ButtonCacheRepository buttonCacheRepository;
    @Autowired
    private ButtonRepository buttonRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MenuCreator menuCreator;
    @Autowired
    private LevelMapper levelMapper;
    @Autowired
    private ButtonRowMapper buttonRowMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDllAuto;


    public Level level_INITIALIZE222;
    public Level level_INITIALIZE0;
    public Level level_INITIALIZE;
    public Level level_ADMIN;
    public Level level_ADMIN_ADMIN;
    public Level level_LANGUAGES;
    public Level level_LANGUAGER;
    public Level level_MAP;
    public Level level_MONITOR;
    public Level level_MONITOR_PRICE;
    public Level level_MONITOR_RESP;
    public Level level_GOODS_LIST;
    public Level level_SHOP_BOTS;
    public Level level_PARTNERS;
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
    public Level level_SEARCH_GROUP;
    public Level level_SEARCH_GROUP_RESP;
    public Level level_SEARCH_GROUP_LIMIT;
    public Level level_SEARCH_GROUP_END;
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


    public LevelDTO levelDTO_INITIALIZE222;
    public LevelDTO levelDTO_INITIALIZE0;
    public LevelDTO levelDTO_INITIALIZE;
    public LevelDTO levelDTO_ADMIN;
    public LevelDTO levelDTO_LANGUAGES;
    public LevelDTO levelDTO_LANGUAGER;
    public LevelDTO levelDTO_MAP;
    public LevelDTO levelDTO_ADMIN_ADMIN;
    public LevelDTO levelDTO_MONITOR;
    public LevelDTO levelDTO_MONITOR_PRICE;
    public LevelDTO levelDTO_MONITOR_RESP;
    public LevelDTO levelDTO_GOODS_LIST;
    public LevelDTO levelDTO_SHOP_BOTS;
    public LevelDTO levelDTO_PARTNERS;
    public LevelDTO levelDTO_SELLERS;
    public LevelDTO levelDTO_SELLERS_REMOVE;
    public LevelDTO levelDTO_SELLERS_ADD;
    public LevelDTO levelDTO_SELLERS_ADD_APPROVE;
    public LevelDTO levelDTO_SELLERS_ADD_DISMISS;
    public LevelDTO levelDTO_SELLERS_REMOVE_APPROVE;
    public LevelDTO levelDTO_SELLERS_REMOVE_DISMISS;
    public LevelDTO levelDTO_BASIC;
    public LevelDTO levelDTO_WITHDRAW_PARTNER;
    public LevelDTO levelDTO_WITHDRAW_PARTNER_GROUP;
    public LevelDTO levelDTO_WITHDRAW_PARTNER_RESP;
    public LevelDTO levelDTO_WITHDRAW_PARTNER_END;
    public LevelDTO levelDTO_SEARCH_PARTNER;
    public LevelDTO levelDTO_SEARCH_PARTNER_RESP;
    public LevelDTO levelDTO_SEARCH_PARTNER_RESP_BUTTON;
    public LevelDTO levelDTO_SEARCH_PARTNER_RATE;
    public LevelDTO levelDTO_SEARCH_PARTNER_LIMIT;
    public LevelDTO levelDTO_SEARCH_PARTNER_END;
    public LevelDTO levelDTO_EDIT_PARTNER;
    public LevelDTO levelDTO_SEARCH_GROUP;
    public LevelDTO levelDTO_SEARCH_GROUP_RESP;
    public LevelDTO levelDTO_SEARCH_GROUP_LIMIT;
    public LevelDTO levelDTO_SEARCH_GROUP_END;
    public LevelDTO levelDTO_ADD_PARTNER;
    public LevelDTO levelDTO_ADD_GROUP;
    public LevelDTO levelDTO_ACTIONS;
    public LevelDTO levelDTO_BASKETS_FOR_SHOP;
    public LevelDTO levelDTO_ACTION_TYPE;
    public LevelDTO levelDTO_SELECT_LEVEL_TYPE;
    public LevelDTO levelDTO_MULTI_ACTION_LEVEL;
    public LevelDTO levelDTO_MULTI_ACTION_LEVEL_BASIC;
    public LevelDTO levelDTO_MULTI_LEVEL_RATE;
    public LevelDTO levelDTO_MULTI_LEVEL_RATE_BASIC;
    public LevelDTO levelDTO_MULTI_LEVEL_QUESTION;
    public LevelDTO levelDTO_MULTI_LEVEL_QUESTION_BASIC;
    public LevelDTO levelDTO_ONE_LEVEL_RATE;
    public LevelDTO levelDTO_ONE_LEVEL_RATE_BASIC;
    public LevelDTO levelDTO_ACTION_RATE_WITHDRAW;
    public LevelDTO levelDTO_ACTION_RATE_WITHDRAW_BASIC;
    public LevelDTO levelDTO_ACTION_RATE_PARTNER;
    public LevelDTO levelDTO_ADD_ACTION_RATE_SOURCE;
    public LevelDTO levelDTO_ADD_ACTION_COUPON_TARGET;
    public LevelDTO levelDTO_COUPON;
    public LevelDTO levelDTO_COUPON_NUMBER;
    public LevelDTO levelDTO_COUPON_RATE_WITHDRAW;
    public LevelDTO levelDTO_ADD_ACTION_COUPON_SOURCE;
    public LevelDTO levelDTO_ADD_ACTION_RATE_TARGET;
    public LevelDTO levelDTO_LINK_TO_PRODUCT;
    public LevelDTO levelDTO_LINK_TO_PRODUCT_NUMBER;
    public LevelDTO levelDTO_ADD_ACTION_LINK_SOURCE;
    public LevelDTO levelDTO_ADD_ACTION_LINK_TARGET;
    public LevelDTO levelDTO_MY_SHOPS;
    public LevelDTO levelDTO_CASHBACKS;
    public LevelDTO levelDTO_CONNECT;
    public LevelDTO levelDTO_CONNECT_SHOP;
    public LevelDTO levelDTO_BOOKMARKS;
    public LevelDTO levelDTO_ADD_BOOKMARK;
    public LevelDTO levelDTO_BASKET;
    public LevelDTO levelDTO_BASKET_ARCHIVE;
    public LevelDTO levelDTO_ADD_BASKET;
    public LevelDTO levelDTO_SEARCH;
    public LevelDTO levelDTO_SEARCH_RESULT;
    public LevelDTO levelDTO_SEARCH_RESULT_PRODUCT;
    public LevelDTO levelDTO_CONSTRUCT;
    public LevelDTO levelDTO_ADMIN_SHOPS;
    public LevelDTO levelDTO_CONSTRUCT_SARAFAN_SHARE;
    public LevelDTO levelDTO_CONSTRUCT_MIN_BILL_SHARE;
    public LevelDTO levelDTO_CONSTRUCT_ADD;
    public LevelDTO levelDTO_ADD_GOODS;
    public LevelDTO levelDTO_ADD_GOODS_NAME;
    public LevelDTO levelDTO_ADD_GOODS_PHOTO;
    public LevelDTO levelDTO_ADD_GOODS_DESCRIPTION;
    public LevelDTO levelDTO_ADD_GOODS_PRICE;
    public LevelDTO levelDTO_ADD_GOODS_END;
    public LevelDTO levelDTO_ADD_BOT;
    public LevelDTO levelDTO_ADD_TAXI_BOT;
    public LevelDTO levelDTO_TAXI_LOCATION;
    public LevelDTO levelDTO_TAXI_SUBMIT;
    public LevelDTO levelDTO_PSHARE2P;
    public LevelDTO levelDTO_P2NOP;
    public LevelDTO levelDTO_P2NOP_RESP;
    public LevelDTO levelDTO_P2P;
    public LevelDTO levelDTO_P2P_RESP;
    public LevelDTO levelDTO_P2B;
    public LevelDTO levelDTO_NEGATIVE_SUM;
    public LevelDTO levelDTO_NEGATIVE_COUNT;
    public LevelDTO levelDTO_P2B_PROPOSE_CASHBACK;
    public LevelDTO levelDTO_P2B_PROPOSE_CASHBACK_RESP;
    public LevelDTO levelDTO_P2B_WRITEOFF_CASHBACK;
    public LevelDTO levelDTO_P2B_WRITEOFF_CASHBACK_RESP;
    public LevelDTO levelDTO_P2B_WRITEOFF_CASHBACK_REQUEST;
    public LevelDTO levelDTO_P2B_WRITEOFF_CASHBACK_APPROVE;
    public LevelDTO levelDTO_P2B_WRITEOFF_CASHBACK_DISMISS;
    public LevelDTO levelDTO_P2B_CHARGE_BASKET_CASHBACK;
    public LevelDTO levelDTO_P2B_APPROVE_BASKET_CASHBACK;
    public LevelDTO levelDTO_P2B_CHARGE_COUPON;
    public LevelDTO levelDTO_P2B_CHARGE_COUPON_REQUEST;
    public LevelDTO levelDTO_P2B_CHARGE_COUPON_RESP;
    public LevelDTO levelDTO_P2B_CHARGE_COUPON_BASKET;
    public LevelDTO levelDTO_P2B_WRITEOFF_COUPON;
    public LevelDTO levelDTO_P2B_WRITEOFF_COUPON_SELECT_ACTION;
    public LevelDTO levelDTO_P2B_WRITEOFF_COUPON_BASKET;
    public LevelDTO levelDTO_P2B_WRITEOFF_COUPON_REQUEST;
    public LevelDTO levelDTO_P2B_WRITEOFF_COUPON_RESP;
    public LevelDTO levelDTO_P2B_WRITEOFF_COUPON_APPROVE;
    public LevelDTO levelDTO_P2B_RESP;
    public LevelDTO levelDTO_B2B;
    public LevelDTO levelDTO_B2NOB;
    public LevelDTO levelDTO_DISCARD_NEW_PARTNER;
    public LevelDTO levelDTO_APPROVE_NEW_PARTNER;
    public LevelDTO levelDTO_NEW_GRUPP;
    public LevelDTO levelDTO_DISCARD_NEW_GRUPP;
    public LevelDTO levelDTO_APPROVE_NEW_GRUPP;
    public LevelDTO levelDTO0_1_1;
    public LevelDTO levelDTO_RESPONSE_BUYER_MESSAGE;
    public LevelDTO levelDTO_RESPONSE_SHOP_MESSAGE;
    public LevelDTO levelDTO_NON_RESPONSE;
    public LevelDTO levelDTO0_1_3;
    public LevelDTO levelDTO0_1_4;
    public LevelDTO levelDTO0_1_5;
    public LevelDTO levelDTO_EDIT_BUTTON_NAME;
    public LevelDTO levelDTO_EDIT_MESSAGE;
    public LevelDTO levelDTO_NEW_LEVEL_END_BUTTON;
    public LevelDTO levelDTO_NEW_LEVEL_INPUT_BUTTON;
    public LevelDTO levelDTO_NEW_LEVEL_BUTTON;


    public InitialLevelOld() {

    }

    //    @PostConstruct
    public void initLevels() {

        System.out.println("hibernateDllAuto****************" + hibernateDllAuto);
        System.out.println(!hibernateDllAuto.contains("create-drop"));

        if (!hibernateDllAuto.contains("create")) {

            level_INITIALIZE222 = levelRepository.findFirstByCallName(INITIALIZE222.name());
            if (level_INITIALIZE222 == null) {
                level_INITIALIZE222 = new Level();
            }
            level_INITIALIZE0 = levelRepository.findFirstByCallName(INITIALIZE0.name());
            if (level_INITIALIZE0 == null) {
                level_INITIALIZE0 = new Level();
            }
            level_INITIALIZE = levelRepository.findFirstByCallName(INITIALIZE.name());
            if (level_INITIALIZE == null) {
                level_INITIALIZE = new Level();
            }
            level_ADMIN = levelRepository.findFirstByCallName(ADMIN.name());
            if (level_ADMIN == null) {
                level_ADMIN = new Level();
            }
            level_ADMIN_ADMIN = levelRepository.findFirstByCallName(ADMIN_ADMIN.name());
            if (level_ADMIN_ADMIN == null) {
                level_ADMIN_ADMIN = new Level();
            }
            level_LANGUAGES = levelRepository.findFirstByCallName(LANGUAGES.name());
            if (level_LANGUAGES == null) {
                level_LANGUAGES = new Level();
            }
            level_LANGUAGER = levelRepository.findFirstByCallName(LANGUAGER.name());
            if (level_LANGUAGER == null) {
                level_LANGUAGER = new Level();
            }
            level_MAP = levelRepository.findFirstByCallName(GEOMAP.name());
            if (level_MAP == null) {
                level_MAP = new Level();
            }

            level_MONITOR = levelRepository.findFirstByCallName(MONITOR.name());
            if (level_MONITOR == null) {
                level_MONITOR = new Level();
            }

            level_MONITOR_PRICE = levelRepository.findFirstByCallName(MONITOR_PRICE.name());
            if (level_MONITOR_PRICE == null) {
                level_MONITOR_PRICE = new Level();
            }

            level_MONITOR_RESP = levelRepository.findFirstByCallName(MONITOR_RESP.name());
            if (level_MONITOR_RESP == null) {
                level_MONITOR_RESP = new Level();
            }

            level_GOODS_LIST = levelRepository.findFirstByCallName(GOODS_LIST.name());
            if (level_GOODS_LIST == null) {
                level_GOODS_LIST = new Level();
            }
            level_SHOP_BOTS = levelRepository.findFirstByCallName(SHOP_BOTS.name());
            if (level_SHOP_BOTS == null) {
                level_SHOP_BOTS = new Level();
            }
            level_PARTNERS = levelRepository.findFirstByCallName(PARTNERS.name());
            if (level_PARTNERS == null) {
                level_PARTNERS = new Level();
            }
            level_SELLERS = levelRepository.findFirstByCallName(SELLERS.name());
            if (level_SELLERS == null) {
                level_SELLERS = new Level();
            }
            level_SELLERS_REMOVE = levelRepository.findFirstByCallName(SELLERS_REMOVE.name());
            if (level_SELLERS_REMOVE == null) {
                level_SELLERS_REMOVE = new Level();
            }
            level_SELLERS_ADD = levelRepository.findFirstByCallName(SELLERS_ADD.name());
            if (level_SELLERS_ADD == null) {
                level_SELLERS_ADD = new Level();
            }

            level_SELLERS_REMOVE_DISMISS = levelRepository.findFirstByCallName(SELLERS_REMOVE_DISMISS.name());
            if (level_SELLERS_REMOVE_DISMISS == null) {
                level_SELLERS_REMOVE_DISMISS = new Level();
            }
            level_SELLERS_REMOVE_APPROVE = levelRepository.findFirstByCallName(SELLERS_REMOVE_APPROVE.name());
            if (level_SELLERS_REMOVE_APPROVE == null) {
                level_SELLERS_REMOVE_APPROVE = new Level();
            }
            level_SELLERS_ADD_DISMISS = levelRepository.findFirstByCallName(SELLERS_ADD_DISMISS.name());
            if (level_SELLERS_ADD_DISMISS == null) {
                level_SELLERS_ADD_DISMISS = new Level();
            }
            level_SELLERS_ADD_APPROVE = levelRepository.findFirstByCallName(SELLERS_ADD_APPROVE.name());
            if (level_SELLERS_ADD_APPROVE == null) {
                level_SELLERS_ADD_APPROVE = new Level();
            }

            level_BASIC = levelRepository.findFirstByCallName(BASIC.name());
            if (level_BASIC == null) {
                level_BASIC = new Level();
            }
            level_WITHDRAW_PARTNER = levelRepository.findFirstByCallName(WITHDRAW_PARTNER.name());
            if (level_WITHDRAW_PARTNER == null) {
                level_WITHDRAW_PARTNER = new Level();
            }
            level_WITHDRAW_PARTNER_GROUP = levelRepository.findFirstByCallName(WITHDRAW_PARTNER_GROUP.name());
            if (level_WITHDRAW_PARTNER_GROUP == null) {
                level_WITHDRAW_PARTNER_GROUP = new Level();
            }
            level_WITHDRAW_PARTNER_RESP = levelRepository.findFirstByCallName(WITHDRAW_PARTNER_RESP.name());
            if (level_WITHDRAW_PARTNER_RESP == null) {
                level_WITHDRAW_PARTNER_RESP = new Level();
            }
            level_WITHDRAW_PARTNER_END = levelRepository.findFirstByCallName(WITHDRAW_PARTNER_END.name());
            if (level_WITHDRAW_PARTNER_END == null) {
                level_WITHDRAW_PARTNER_END = new Level();
            }
            level_SEARCH_PARTNER = levelRepository.findFirstByCallName(SEARCH_PARTNER.name());
            if (level_SEARCH_PARTNER == null) {
                level_SEARCH_PARTNER = new Level();
            }
            level_SEARCH_PARTNER_RESP = levelRepository.findFirstByCallName(SEARCH_PARTNER_RESP.name());
            if (level_SEARCH_PARTNER_RESP == null) {
                level_SEARCH_PARTNER_RESP = new Level();
            }
            level_SEARCH_PARTNER_RESP_BUTTON = levelRepository.findFirstByCallName(SEARCH_PARTNER_RESP_BUTTON.name());
            if (level_SEARCH_PARTNER_RESP_BUTTON == null) {
                level_SEARCH_PARTNER_RESP_BUTTON = new Level();
            }
            level_SEARCH_PARTNER_RATE = levelRepository.findFirstByCallName(SEARCH_PARTNER_RATE.name());
            if (level_SEARCH_PARTNER_RATE == null) {
                level_SEARCH_PARTNER_RATE = new Level();
            }
            level_SEARCH_PARTNER_LIMIT = levelRepository.findFirstByCallName(SEARCH_PARTNER_LIMIT.name());
            if (level_SEARCH_PARTNER_LIMIT == null) {
                level_SEARCH_PARTNER_LIMIT = new Level();
            }
            level_SEARCH_PARTNER_END = levelRepository.findFirstByCallName(SEARCH_PARTNER_END.name());
            if (level_SEARCH_PARTNER_END == null) {
                level_SEARCH_PARTNER_END = new Level();
            }

            level_EDIT_PARTNER = levelRepository.findFirstByCallName(EDIT_PARTNER.name());
            if (level_EDIT_PARTNER == null) {
                level_EDIT_PARTNER = new Level();
            }

            level_SEARCH_GROUP = levelRepository.findFirstByCallName(SEARCH_GROUP.name());
            if (level_SEARCH_GROUP == null) {
                level_SEARCH_GROUP = new Level();
            }
            level_SEARCH_GROUP_RESP = levelRepository.findFirstByCallName(SEARCH_GROUP_RESP.name());
            if (level_SEARCH_GROUP_RESP == null) {
                level_SEARCH_GROUP_RESP = new Level();
            }
            level_SEARCH_GROUP_LIMIT = levelRepository.findFirstByCallName(SEARCH_GROUP_LIMIT.name());
            if (level_SEARCH_GROUP_LIMIT == null) {
                level_SEARCH_GROUP_LIMIT = new Level();
            }
            level_SEARCH_GROUP_END = levelRepository.findFirstByCallName(SEARCH_GROUP_END.name());
            if (level_SEARCH_GROUP_END == null) {
                level_SEARCH_GROUP_END = new Level();
            }
            level_ADD_PARTNER = levelRepository.findFirstByCallName(ADD_PARTNER.name());
            if (level_ADD_PARTNER == null) {
                level_ADD_PARTNER = new Level();
            }
            level_ADD_GROUP = levelRepository.findFirstByCallName(ADD_GROUP.name());
            if (level_ADD_GROUP == null) {
                level_ADD_GROUP = new Level();
            }
            level_ACTIONS = levelRepository.findFirstByCallName(ACTIONS.name());
            if (level_ACTIONS == null) {
                level_ACTIONS = new Level();
            }
            level_BASKETS_FOR_SHOP = levelRepository.findFirstByCallName(BASKETS_FOR_SHOP.name());
            if (level_BASKETS_FOR_SHOP == null) {
                level_BASKETS_FOR_SHOP = new Level();
            }
            level_ACTION_TYPE = levelRepository.findFirstByCallName(ACTION_TYPE.name());
            if (level_ACTION_TYPE == null) {
                level_ACTION_TYPE = new Level();
            }
            level_SELECT_LEVEL_TYPE = levelRepository.findFirstByCallName(SELECT_LEVEL_TYPE.name());
            if (level_SELECT_LEVEL_TYPE == null) {
                level_SELECT_LEVEL_TYPE = new Level();
            }
            level_MULTI_ACTION_LEVEL = levelRepository.findFirstByCallName(MULTI_ACTION_LEVEL.name());
            if (level_MULTI_ACTION_LEVEL == null) {
                level_MULTI_ACTION_LEVEL = new Level();
            }
            level_MULTI_ACTION_LEVEL_BASIC = levelRepository.findFirstByCallName(MULTI_ACTION_LEVEL_BASIC.name());
            if (level_MULTI_ACTION_LEVEL_BASIC == null) {
                level_MULTI_ACTION_LEVEL_BASIC = new Level();
            }
            level_MULTI_LEVEL_RATE = levelRepository.findFirstByCallName(MULTI_LEVEL_RATE.name());
            if (level_MULTI_LEVEL_RATE == null) {
                level_MULTI_LEVEL_RATE = new Level();
            }
            level_MULTI_LEVEL_RATE_BASIC = levelRepository.findFirstByCallName(MULTI_LEVEL_RATE_BASIC.name());
            if (level_MULTI_LEVEL_RATE_BASIC == null) {
                level_MULTI_LEVEL_RATE_BASIC = new Level();
            }
            level_MULTI_LEVEL_QUESTION = levelRepository.findFirstByCallName(MULTI_LEVEL_QUESTION.name());
            if (level_MULTI_LEVEL_QUESTION == null) {
                level_MULTI_LEVEL_QUESTION = new Level();
            }
            level_MULTI_LEVEL_QUESTION_BASIC = levelRepository.findFirstByCallName(MULTI_LEVEL_QUESTION_BASIC.name());
            if (level_MULTI_LEVEL_QUESTION_BASIC == null) {
                level_MULTI_LEVEL_QUESTION_BASIC = new Level();
            }
            level_ONE_LEVEL_RATE = levelRepository.findFirstByCallName(ONE_LEVEL_RATE.name());
            if (level_ONE_LEVEL_RATE == null) {
                level_ONE_LEVEL_RATE = new Level();
            }
            level_ONE_LEVEL_RATE_BASIC = levelRepository.findFirstByCallName(ONE_LEVEL_RATE_BASIC.name());
            if (level_ONE_LEVEL_RATE_BASIC == null) {
                level_ONE_LEVEL_RATE_BASIC = new Level();
            }
            level_ACTION_RATE_WITHDRAW = levelRepository.findFirstByCallName(ACTION_RATE_WITHDRAW.name());
            if (level_ACTION_RATE_WITHDRAW == null) {
                level_ACTION_RATE_WITHDRAW = new Level();
            }
            level_ACTION_RATE_WITHDRAW_BASIC = levelRepository.findFirstByCallName(ACTION_RATE_WITHDRAW_BASIC.name());
            if (level_ACTION_RATE_WITHDRAW_BASIC == null) {
                level_ACTION_RATE_WITHDRAW_BASIC = new Level();
            }
            level_ACTION_RATE_PARTNER = levelRepository.findFirstByCallName(ACTION_RATE_PARTNER.name());
            if (level_ACTION_RATE_PARTNER == null) {
                level_ACTION_RATE_PARTNER = new Level();
            }
            level_ADD_ACTION_RATE_SOURCE = levelRepository.findFirstByCallName(ADD_ACTION_RATE_SOURCE.name());
            if (level_ADD_ACTION_RATE_SOURCE == null) {
                level_ADD_ACTION_RATE_SOURCE = new Level();
            }
            level_ADD_ACTION_COUPON_TARGET = levelRepository.findFirstByCallName(ADD_ACTION_COUPON_TARGET.name());
            if (level_ADD_ACTION_COUPON_TARGET == null) {
                level_ADD_ACTION_COUPON_TARGET = new Level();
            }
            level_COUPON = levelRepository.findFirstByCallName(COUPON.name());
            if (level_COUPON == null) {
                level_COUPON = new Level();
            }
            level_COUPON_NUMBER = levelRepository.findFirstByCallName(COUPON_NUMBER.name());
            if (level_COUPON_NUMBER == null) {
                level_COUPON_NUMBER = new Level();
            }
            level_COUPON_RATE_WITHDRAW = levelRepository.findFirstByCallName(COUPON_RATE_WITHDRAW.name());
            if (level_COUPON_RATE_WITHDRAW == null) {
                level_COUPON_RATE_WITHDRAW = new Level();
            }
            level_ADD_ACTION_COUPON_SOURCE = levelRepository.findFirstByCallName(ADD_ACTION_COUPON_SOURCE.name());
            if (level_ADD_ACTION_COUPON_SOURCE == null) {
                level_ADD_ACTION_COUPON_SOURCE = new Level();
            }
            level_ADD_ACTION_RATE_TARGET = levelRepository.findFirstByCallName(ADD_ACTION_RATE_TARGET.name());
            if (level_ADD_ACTION_RATE_TARGET == null) {
                level_ADD_ACTION_RATE_TARGET = new Level();
            }
            level_LINK_TO_PRODUCT = levelRepository.findFirstByCallName(LINK_TO_PRODUCT.name());
            if (level_LINK_TO_PRODUCT == null) {
                level_LINK_TO_PRODUCT = new Level();
            }
            level_LINK_TO_PRODUCT_NUMBER = levelRepository.findFirstByCallName(LINK_TO_PRODUCT_NUMBER.name());
            if (level_LINK_TO_PRODUCT_NUMBER == null) {
                level_LINK_TO_PRODUCT_NUMBER = new Level();
            }
            level_ADD_ACTION_LINK_SOURCE = levelRepository.findFirstByCallName(ADD_ACTION_LINK_SOURCE.name());
            if (level_ADD_ACTION_LINK_SOURCE == null) {
                level_ADD_ACTION_LINK_SOURCE = new Level();
            }
            level_ADD_ACTION_LINK_TARGET = levelRepository.findFirstByCallName(ADD_ACTION_LINK_TARGET.name());
            if (level_ADD_ACTION_LINK_TARGET == null) {
                level_ADD_ACTION_LINK_TARGET = new Level();
            }
            level_MY_SHOPS = levelRepository.findFirstByCallName(MY_SHOPS.name());
            if (level_MY_SHOPS == null) {
                level_MY_SHOPS = new Level();
            }
            level_CASHBACKS = levelRepository.findFirstByCallName(CASHBACKS.name());
            if (level_CASHBACKS == null) {
                level_CASHBACKS = new Level();
            }
            level_CONNECT = levelRepository.findFirstByCallName(CONNECT.name());
            if (level_CONNECT == null) {
                level_CONNECT = new Level();
            }
            level_CONNECT_SHOP = levelRepository.findFirstByCallName(CONNECT_SHOP.name());
            if (level_CONNECT_SHOP == null) {
                level_CONNECT_SHOP = new Level();
            }
            level_BOOKMARKS = levelRepository.findFirstByCallName(BOOKMARKS.name());
            if (level_BOOKMARKS == null) {
                level_BOOKMARKS = new Level();
            }
            level_ADD_BOOKMARK = levelRepository.findFirstByCallName(ADD_BOOKMARK.name());
            if (level_ADD_BOOKMARK == null) {
                level_ADD_BOOKMARK = new Level();
            }
            level_BASKET = levelRepository.findFirstByCallName(BASKET.name());
            if (level_BASKET == null) {
                level_BASKET = new Level();
            }
            level_BASKET_ARCHIVE = levelRepository.findFirstByCallName(BASKET_ARCHIVE.name());
            if (level_BASKET_ARCHIVE == null) {
                level_BASKET_ARCHIVE = new Level();
            }
            level_ADD_BASKET = levelRepository.findFirstByCallName(ADD_BASKET.name());
            if (level_ADD_BASKET == null) {
                level_ADD_BASKET = new Level();
            }
            level_SEARCH = levelRepository.findFirstByCallName(SEARCH.name());
            if (level_SEARCH == null) {
                level_SEARCH = new Level();
            }
            level_SEARCH_RESULT = levelRepository.findFirstByCallName(SEARCH_RESULT.name());
            if (level_SEARCH_RESULT == null) {
                level_SEARCH_RESULT = new Level();
            }
            level_SEARCH_RESULT_PRODUCT = levelRepository.findFirstByCallName(SEARCH_RESULT_PRODUCT.name());
            if (level_SEARCH_RESULT_PRODUCT == null) {
                level_SEARCH_RESULT_PRODUCT = new Level();
            }
            level_ADMIN_SHOPS = levelRepository.findFirstByCallName(ADMIN_SHOPS.name());
            if (level_ADMIN_SHOPS == null) {
                level_ADMIN_SHOPS = new Level();
            }
            level_CONSTRUCT = levelRepository.findFirstByCallName(CONSTRUCT.name());
            if (level_CONSTRUCT == null) {
                level_CONSTRUCT = new Level();
            }
            level_CONSTRUCT_SARAFAN_SHARE = levelRepository.findFirstByCallName(CONSTRUCT_SARAFAN_SHARE.name());
            if (level_CONSTRUCT_SARAFAN_SHARE == null) {
                level_CONSTRUCT_SARAFAN_SHARE = new Level();
            }
            level_CONSTRUCT_MIN_BILL_SHARE = levelRepository.findFirstByCallName(CONSTRUCT_MIN_BILL_SHARE.name());
            if (level_CONSTRUCT_MIN_BILL_SHARE == null) {
                level_CONSTRUCT_MIN_BILL_SHARE = new Level();
            }
            level_CONSTRUCT_ADD = levelRepository.findFirstByCallName(CONSTRUCT_ADD.name());
            if (level_CONSTRUCT_ADD == null) {
                level_CONSTRUCT_ADD = new Level();
            }
            level_ADD_GOODS = levelRepository.findFirstByCallName(ADD_GOODS.name());
            if (level_ADD_GOODS == null) {
                level_ADD_GOODS = new Level();
            }
            level_ADD_GOODS_NAME = levelRepository.findFirstByCallName(ADD_GOODS_NAME.name());
            if (level_ADD_GOODS_NAME == null) {
                level_ADD_GOODS_NAME = new Level();
            }
            level_ADD_GOODS_PHOTO = levelRepository.findFirstByCallName(ADD_GOODS_PHOTO.name());
            if (level_ADD_GOODS_PHOTO == null) {
                level_ADD_GOODS_PHOTO = new Level();
            }
            level_ADD_GOODS_DESCRIPTION = levelRepository.findFirstByCallName(ADD_GOODS_DESCRIPTION.name());
            if (level_ADD_GOODS_DESCRIPTION == null) {
                level_ADD_GOODS_DESCRIPTION = new Level();
            }
            level_ADD_GOODS_PRICE = levelRepository.findFirstByCallName(ADD_GOODS_PRICE.name());
            if (level_ADD_GOODS_PRICE == null) {
                level_ADD_GOODS_PRICE = new Level();
            }
            level_ADD_GOODS_END = levelRepository.findFirstByCallName(ADD_GOODS_END.name());
            if (level_ADD_GOODS_END == null) {
                level_ADD_GOODS_END = new Level();
            }
            level_ADD_BOT = levelRepository.findFirstByCallName(ADD_BOT.name());
            if (level_ADD_BOT == null) {
                level_ADD_BOT = new Level();
            }
            level_ADD_TAXI_BOT = levelRepository.findFirstByCallName(ADD_TAXI_BOT.name());
            if (level_ADD_TAXI_BOT == null) {
                level_ADD_TAXI_BOT = new Level();
            }
            level_TAXI_LOCATION = levelRepository.findFirstByCallName(TAXI_LOCATION.name());
            if (level_TAXI_LOCATION == null) {
                level_TAXI_LOCATION = new Level();
            }
            level_TAXI_SUBMIT = levelRepository.findFirstByCallName(TAXI_SUBMIT.name());
            if (level_TAXI_SUBMIT == null) {
                level_TAXI_SUBMIT = new Level();
            }
            level_PSHARE2P = levelRepository.findFirstByCallName(PSHARE2P.name());
            if (level_PSHARE2P == null) {
                level_PSHARE2P = new Level();
            }
            level_P2NOP = levelRepository.findFirstByCallName(P2NOP.name());
            if (level_P2NOP == null) {
                level_P2NOP = new Level();
            }
            level_P2NOP_RESP = levelRepository.findFirstByCallName(P2NOP_RESP.name());
            if (level_P2NOP_RESP == null) {
                level_P2NOP_RESP = new Level();
            }
            level_P2P = levelRepository.findFirstByCallName(P2P.name());
            if (level_P2P == null) {
                level_P2P = new Level();
            }
            level_P2P_RESP = levelRepository.findFirstByCallName(P2P_RESP.name());
            if (level_P2P_RESP == null) {
                level_P2P_RESP = new Level();
            }
            level_P2B = levelRepository.findFirstByCallName(P2B.name());
            if (level_P2B == null) {
                level_P2B = new Level();
            }
            level_NEGATIVE_SUM = levelRepository.findFirstByCallName(NEGATIVE_SUM.name());
            if (level_NEGATIVE_SUM == null) {
                level_NEGATIVE_SUM = new Level();
            }
            level_NEGATIVE_COUNT = levelRepository.findFirstByCallName(NEGATIVE_COUNT.name());
            if (level_NEGATIVE_COUNT == null) {
                level_NEGATIVE_COUNT = new Level();
            }
            level_P2B_PROPOSE_CASHBACK = levelRepository.findFirstByCallName(P2B_PROPOSE_CASHBACK.name());
            if (level_P2B_PROPOSE_CASHBACK == null) {
                level_P2B_PROPOSE_CASHBACK = new Level();
            }
            level_P2B_PROPOSE_CASHBACK_RESP = levelRepository.findFirstByCallName(P2B_PROPOSE_CASHBACK_RESP.name());
            if (level_P2B_PROPOSE_CASHBACK_RESP == null) {
                level_P2B_PROPOSE_CASHBACK_RESP = new Level();
            }
            level_P2B_WRITEOFF_CASHBACK = levelRepository.findFirstByCallName(P2B_WRITEOFF_CASHBACK.name());
            if (level_P2B_WRITEOFF_CASHBACK == null) {
                level_P2B_WRITEOFF_CASHBACK = new Level();
            }
            level_P2B_WRITEOFF_CASHBACK_RESP = levelRepository.findFirstByCallName(P2B_WRITEOFF_CASHBACK_RESP.name());
            if (level_P2B_WRITEOFF_CASHBACK_RESP == null) {
                level_P2B_WRITEOFF_CASHBACK_RESP = new Level();
            }
            level_P2B_WRITEOFF_CASHBACK_REQUEST = levelRepository.findFirstByCallName(P2B_WRITEOFF_CASHBACK_REQUEST.name());
            if (level_P2B_WRITEOFF_CASHBACK_REQUEST == null) {
                level_P2B_WRITEOFF_CASHBACK_REQUEST = new Level();
            }
            level_P2B_WRITEOFF_CASHBACK_APPROVE = levelRepository.findFirstByCallName(P2B_WRITEOFF_CASHBACK_APPROVE.name());
            if (level_P2B_WRITEOFF_CASHBACK_APPROVE == null) {
                level_P2B_WRITEOFF_CASHBACK_APPROVE = new Level();
            }
            level_P2B_WRITEOFF_CASHBACK_DISMISS = levelRepository.findFirstByCallName(P2B_WRITEOFF_CASHBACK_DISMISS.name());
            if (level_P2B_WRITEOFF_CASHBACK_DISMISS == null) {
                level_P2B_WRITEOFF_CASHBACK_DISMISS = new Level();
            }
            level_P2B_CHARGE_BASKET_CASHBACK = levelRepository.findFirstByCallName(P2B_CHARGE_BASKET_CASHBACK.name());
            if (level_P2B_CHARGE_BASKET_CASHBACK == null) {
                level_P2B_CHARGE_BASKET_CASHBACK = new Level();
            }
            level_P2B_APPROVE_BASKET_CASHBACK = levelRepository.findFirstByCallName(P2B_APPROVE_BASKET_CASHBACK.name());
            if (level_P2B_APPROVE_BASKET_CASHBACK == null) {
                level_P2B_APPROVE_BASKET_CASHBACK = new Level();
            }
            level_P2B_CHARGE_COUPON = levelRepository.findFirstByCallName(P2B_CHARGE_COUPON.name());
            if (level_P2B_CHARGE_COUPON == null) {
                level_P2B_CHARGE_COUPON = new Level();
            }
            level_P2B_CHARGE_COUPON_REQUEST = levelRepository.findFirstByCallName(P2B_CHARGE_COUPON_REQUEST.name());
            if (level_P2B_CHARGE_COUPON_REQUEST == null) {
                level_P2B_CHARGE_COUPON_REQUEST = new Level();
            }
            level_P2B_CHARGE_COUPON_RESP = levelRepository.findFirstByCallName(P2B_CHARGE_COUPON_RESP.name());
            if (level_P2B_CHARGE_COUPON_RESP == null) {
                level_P2B_CHARGE_COUPON_RESP = new Level();
            }
            level_P2B_CHARGE_COUPON_BASKET = levelRepository.findFirstByCallName(P2B_CHARGE_COUPON_BASKET.name());
            if (level_P2B_CHARGE_COUPON_BASKET == null) {
                level_P2B_CHARGE_COUPON_BASKET = new Level();
            }
            level_P2B_WRITEOFF_COUPON = levelRepository.findFirstByCallName(P2B_WRITEOFF_COUPON.name());
            if (level_P2B_WRITEOFF_COUPON == null) {
                level_P2B_WRITEOFF_COUPON = new Level();
            }
            level_P2B_WRITEOFF_COUPON_REQUEST = levelRepository.findFirstByCallName(P2B_WRITEOFF_COUPON_REQUEST.name());
            if (level_P2B_WRITEOFF_COUPON_REQUEST == null) {
                level_P2B_WRITEOFF_COUPON_REQUEST = new Level();
            }
            level_P2B_WRITEOFF_COUPON_RESP = levelRepository.findFirstByCallName(P2B_WRITEOFF_COUPON_RESP.name());
            if (level_P2B_WRITEOFF_COUPON_RESP == null) {
                level_P2B_WRITEOFF_COUPON_RESP = new Level();
            }
            level_P2B_WRITEOFF_COUPON_APPROVE = levelRepository.findFirstByCallName(P2B_WRITEOFF_COUPON_APPROVE.name());
            if (level_P2B_WRITEOFF_COUPON_APPROVE == null) {
                level_P2B_WRITEOFF_COUPON_APPROVE = new Level();
            }
            level_P2B_WRITEOFF_COUPON_SELECT_ACTION = levelRepository.findFirstByCallName(P2B_WRITEOFF_COUPON_SELECT_ACTION.name());
            if (level_P2B_WRITEOFF_COUPON_SELECT_ACTION == null) {
                level_P2B_WRITEOFF_COUPON_SELECT_ACTION = new Level();
            }
            level_P2B_WRITEOFF_COUPON_BASKET = levelRepository.findFirstByCallName(P2B_WRITEOFF_COUPON_BASKET.name());
            if (level_P2B_WRITEOFF_COUPON_BASKET == null) {
                level_P2B_WRITEOFF_COUPON_BASKET = new Level();
            }
            level_P2B_RESP = levelRepository.findFirstByCallName(P2B_RESP.name());
            if (level_P2B_RESP == null) {
                level_P2B_RESP = new Level();
            }
            level_B2B = levelRepository.findFirstByCallName(B2B.name());
            if (level_B2B == null) {
                level_B2B = new Level();
            }
            level_B2NOB = levelRepository.findFirstByCallName(B2NOB.name());
            if (level_B2NOB == null) {
                level_B2NOB = new Level();
            }
            level_DISCARD_NEW_PARTNER = levelRepository.findFirstByCallName(DISCARD_NEW_PARTNER.name());
            if (level_DISCARD_NEW_PARTNER == null) {
                level_DISCARD_NEW_PARTNER = new Level();
            }
            level_APPROVE_NEW_PARTNER = levelRepository.findFirstByCallName(APPROVE_NEW_PARTNER.name());
            if (level_APPROVE_NEW_PARTNER == null) {
                level_APPROVE_NEW_PARTNER = new Level();
            }
            level_NEW_GRUPP = levelRepository.findFirstByCallName(NEW_GRUPP.name());
            if (level_NEW_GRUPP == null) {
                level_NEW_GRUPP = new Level();
            }
            level_DISCARD_NEW_GRUPP = levelRepository.findFirstByCallName(DISCARD_NEW_GRUPP.name());
            if (level_DISCARD_NEW_GRUPP == null) {
                level_DISCARD_NEW_GRUPP = new Level();
            }
            level_APPROVE_NEW_GRUPP = levelRepository.findFirstByCallName(APPROVE_NEW_GRUPP.name());
            if (level_APPROVE_NEW_GRUPP == null) {
                level_APPROVE_NEW_GRUPP = new Level();
            }
            level0_1_1 = levelRepository.findFirstByCallName("level0_1_1");
            if (level0_1_1 == null) {
                level0_1_1 = new Level();
            }
            level_RESPONSE_BUYER_MESSAGE = levelRepository.findFirstByCallName(RESPONSE_BUYER_MESSAGE.name());
            if (level_RESPONSE_BUYER_MESSAGE == null) {
                level_RESPONSE_BUYER_MESSAGE = new Level();
            }
            level_RESPONSE_SHOP_MESSAGE = levelRepository.findFirstByCallName(RESPONSE_SHOP_MESSAGE.name());
            if (level_RESPONSE_SHOP_MESSAGE == null) {
                level_RESPONSE_SHOP_MESSAGE = new Level();
            }
            level_NON_RESPONSE = levelRepository.findFirstByCallName(NON_RESPONSE.name());
            if (level_NON_RESPONSE == null) {
                level_NON_RESPONSE = new Level();
            }
            level0_1_3 = levelRepository.findFirstByCallName("level0_1_3");
            if (level0_1_3 == null) {
                level0_1_3 = new Level();
            }
            level0_1_4 = levelRepository.findFirstByCallName(SEND_SHOP_MESSAGE.name());
            if (level0_1_4 == null) {
                level0_1_4 = new Level();
            }
            level0_1_5 = levelRepository.findFirstByCallName(SEND_BUYER_MESSAGE.name());
            if (level0_1_5 == null) {
                level0_1_5 = new Level();
            }
            level_EDIT_BUTTON_NAME = levelRepository.findFirstByCallName(EDIT_BUTTON_NAME.name());
            if (level_EDIT_BUTTON_NAME == null) {
                level_EDIT_BUTTON_NAME = new Level();
            }
            level_EDIT_MESSAGE = levelRepository.findFirstByCallName(EDIT_MESSAGE.name());
            if (level_EDIT_MESSAGE == null) {
                level_EDIT_MESSAGE = new Level();
            }
            level_NEW_LEVEL_END_BUTTON = levelRepository.findFirstByCallName(NEW_LEVEL_END_BUTTON.name());
            if (level_NEW_LEVEL_END_BUTTON == null) {
                level_NEW_LEVEL_END_BUTTON = new Level();
            }
            level_NEW_LEVEL_INPUT_BUTTON = levelRepository.findFirstByCallName(NEW_LEVEL_INPUT_BUTTON.name());
            if (level_NEW_LEVEL_INPUT_BUTTON == null) {
                level_NEW_LEVEL_INPUT_BUTTON = new Level();
            }
            level_NEW_LEVEL_BUTTON = levelRepository.findFirstByCallName(NEW_LEVEL_BUTTON.name());
            if (level_NEW_LEVEL_BUTTON == null) {
                level_NEW_LEVEL_BUTTON = new Level();
            }

        } else {

            level_INITIALIZE222 = new Level();
            level_INITIALIZE0 = new Level();
            level_INITIALIZE = new Level();
            level_ADMIN = new Level();
            level_ADMIN_ADMIN = new Level();
            level_LANGUAGES = new Level();
            level_LANGUAGER = new Level();
            level_MAP = new Level();
            level_MONITOR = new Level();
            level_MONITOR_PRICE = new Level();
            level_MONITOR_RESP = new Level();
            level_GOODS_LIST = new Level();
            level_SHOP_BOTS = new Level();
            level_PARTNERS = new Level();
            level_SELLERS = new Level();
            level_SELLERS_REMOVE = new Level();
            level_SELLERS_ADD = new Level();
            level_SELLERS_ADD_APPROVE = new Level();
            level_SELLERS_ADD_DISMISS = new Level();
            level_SELLERS_REMOVE_APPROVE = new Level();
            level_SELLERS_REMOVE_DISMISS = new Level();
            level_BASIC = new Level();
            level_WITHDRAW_PARTNER = new Level();
            level_WITHDRAW_PARTNER_GROUP = new Level();
            level_WITHDRAW_PARTNER_RESP = new Level();
            level_WITHDRAW_PARTNER_END = new Level();
            level_SEARCH_PARTNER = new Level();
            level_SEARCH_PARTNER_RESP = new Level();
            level_SEARCH_PARTNER_RESP_BUTTON = new Level();
            level_SEARCH_PARTNER_RATE = new Level();
            level_SEARCH_PARTNER_LIMIT = new Level();
            level_SEARCH_PARTNER_END = new Level();
            level_EDIT_PARTNER = new Level();
            level_SEARCH_GROUP = new Level();
            level_SEARCH_GROUP_RESP = new Level();
            level_SEARCH_GROUP_LIMIT = new Level();
            level_SEARCH_GROUP_END = new Level();
            level_ADD_PARTNER = new Level();
            level_ADD_GROUP = new Level();
            level_ACTIONS = new Level();
            level_BASKETS_FOR_SHOP = new Level();
            level_ACTION_TYPE = new Level();
            level_SELECT_LEVEL_TYPE = new Level();
            level_MULTI_ACTION_LEVEL = new Level();
            level_MULTI_ACTION_LEVEL_BASIC = new Level();
            level_MULTI_LEVEL_RATE = new Level();
            level_MULTI_LEVEL_RATE_BASIC = new Level();
            level_MULTI_LEVEL_QUESTION = new Level();
            level_MULTI_LEVEL_QUESTION_BASIC = new Level();
            level_ONE_LEVEL_RATE = new Level();
            level_ONE_LEVEL_RATE_BASIC = new Level();
            level_ACTION_RATE_WITHDRAW = new Level();
            level_ACTION_RATE_WITHDRAW_BASIC = new Level();
            level_ACTION_RATE_PARTNER = new Level();
            level_ADD_ACTION_RATE_SOURCE = new Level();
            level_ADD_ACTION_COUPON_TARGET = new Level();
            level_COUPON = new Level();
            level_COUPON_NUMBER = new Level();
            level_COUPON_RATE_WITHDRAW = new Level();
            level_ADD_ACTION_COUPON_SOURCE = new Level();
            level_ADD_ACTION_RATE_TARGET = new Level();
            level_LINK_TO_PRODUCT = new Level();
            level_LINK_TO_PRODUCT_NUMBER = new Level();
            level_ADD_ACTION_LINK_SOURCE = new Level();
            level_ADD_ACTION_LINK_TARGET = new Level();
            level_MY_SHOPS = new Level();
            level_CASHBACKS = new Level();
            level_CONNECT = new Level();
            level_CONNECT_SHOP = new Level();
            level_BOOKMARKS = new Level();
            level_ADD_BOOKMARK = new Level();
            level_BASKET = new Level();
            level_BASKET_ARCHIVE = new Level();
            level_ADD_BASKET = new Level();
            level_SEARCH = new Level();
            level_SEARCH_RESULT = new Level();
            level_SEARCH_RESULT_PRODUCT = new Level();
            level_ADMIN_SHOPS = new Level();
            level_CONSTRUCT = new Level();
            level_CONSTRUCT_SARAFAN_SHARE = new Level();
            level_CONSTRUCT_MIN_BILL_SHARE = new Level();
            level_CONSTRUCT_ADD = new Level();
            level_ADD_GOODS = new Level();
            level_ADD_GOODS_NAME = new Level();
            level_ADD_GOODS_PHOTO = new Level();
            level_ADD_GOODS_DESCRIPTION = new Level();
            level_ADD_GOODS_PRICE = new Level();
            level_ADD_GOODS_END = new Level();
            level_ADD_BOT = new Level();
            level_ADD_TAXI_BOT = new Level();
            level_TAXI_LOCATION = new Level();
            level_TAXI_SUBMIT = new Level();
            level_PSHARE2P = new Level();
            level_P2NOP = new Level();
            level_P2NOP_RESP = new Level();
            level_P2P = new Level();
            level_P2P_RESP = new Level();
            level_P2B = new Level();
            level_NEGATIVE_SUM = new Level();
            level_NEGATIVE_COUNT = new Level();
            level_P2B_PROPOSE_CASHBACK = new Level();
            level_P2B_PROPOSE_CASHBACK_RESP = new Level();
            level_P2B_WRITEOFF_CASHBACK = new Level();
            level_P2B_WRITEOFF_CASHBACK_RESP = new Level();
            level_P2B_WRITEOFF_CASHBACK_REQUEST = new Level();
            level_P2B_WRITEOFF_CASHBACK_APPROVE = new Level();
            level_P2B_WRITEOFF_CASHBACK_DISMISS = new Level();
            level_P2B_CHARGE_BASKET_CASHBACK = new Level();
            level_P2B_APPROVE_BASKET_CASHBACK = new Level();
            level_P2B_CHARGE_COUPON = new Level();
            level_P2B_CHARGE_COUPON_REQUEST = new Level();
            level_P2B_CHARGE_COUPON_RESP = new Level();
            level_P2B_CHARGE_COUPON_BASKET = new Level();
            level_P2B_WRITEOFF_COUPON = new Level();
            level_P2B_WRITEOFF_COUPON_REQUEST = new Level();
            level_P2B_WRITEOFF_COUPON_RESP = new Level();
            level_P2B_WRITEOFF_COUPON_APPROVE = new Level();
            level_P2B_WRITEOFF_COUPON_SELECT_ACTION = new Level();
            level_P2B_WRITEOFF_COUPON_BASKET = new Level();
            level_P2B_RESP = new Level();
            level_B2B = new Level();
            level_B2NOB = new Level();
            level_DISCARD_NEW_PARTNER = new Level();
            level_APPROVE_NEW_PARTNER = new Level();
            level_NEW_GRUPP = new Level();
            level_DISCARD_NEW_GRUPP = new Level();
            level_APPROVE_NEW_GRUPP = new Level();
            level0_1_1 = new Level();
            level_RESPONSE_BUYER_MESSAGE = new Level();
            level_RESPONSE_SHOP_MESSAGE = new Level();
            level_NON_RESPONSE = new Level();
            level0_1_3 = new Level();
            level0_1_4 = new Level();
            level0_1_5 = new Level();
            level_EDIT_BUTTON_NAME = new Level();
            level_EDIT_MESSAGE = new Level();
            level_NEW_LEVEL_END_BUTTON = new Level();
            level_NEW_LEVEL_INPUT_BUTTON = new Level();
            level_NEW_LEVEL_BUTTON = new Level();


//            initialExcelMenu.execute();
//            categoryOptionsTasklet.execute();


            levelCacheRepository.save(level_INITIALIZE222);
            levelCacheRepository.save(level_INITIALIZE0);
            levelCacheRepository.save(level_INITIALIZE);
            levelCacheRepository.save(level_ADMIN);
            levelCacheRepository.save(level_ADMIN_ADMIN);
            levelCacheRepository.save(level_LANGUAGES);
            levelCacheRepository.save(level_LANGUAGER);
            levelCacheRepository.save(level_MAP);
            levelCacheRepository.save(level_MONITOR);
            levelCacheRepository.save(level_MONITOR_PRICE);
            levelCacheRepository.save(level_MONITOR_RESP);
            levelCacheRepository.save(level_GOODS_LIST);
            levelCacheRepository.save(level_SHOP_BOTS);
            levelCacheRepository.save(level_PARTNERS);
            levelCacheRepository.save(level_SELLERS);
            levelCacheRepository.save(level_SELLERS_REMOVE);
            levelCacheRepository.save(level_SELLERS_ADD);
            levelCacheRepository.save(level_SELLERS_ADD_APPROVE);
            levelCacheRepository.save(level_SELLERS_ADD_DISMISS);
            levelCacheRepository.save(level_SELLERS_REMOVE_APPROVE);
            levelCacheRepository.save(level_SELLERS_REMOVE_DISMISS);
            levelCacheRepository.save(level_BASIC);
            levelCacheRepository.save(level_WITHDRAW_PARTNER);
            levelCacheRepository.save(level_WITHDRAW_PARTNER_GROUP);
            levelCacheRepository.save(level_WITHDRAW_PARTNER_RESP);
            levelCacheRepository.save(level_WITHDRAW_PARTNER_END);
            levelCacheRepository.save(level_SEARCH_PARTNER);
            levelCacheRepository.save(level_SEARCH_PARTNER_RESP);
            levelCacheRepository.save(level_SEARCH_PARTNER_RESP_BUTTON);
            levelCacheRepository.save(level_SEARCH_PARTNER_RATE);
            levelCacheRepository.save(level_SEARCH_PARTNER_LIMIT);
            levelCacheRepository.save(level_SEARCH_PARTNER_END);
            levelCacheRepository.save(level_EDIT_PARTNER);
            levelCacheRepository.save(level_SEARCH_GROUP);
            levelCacheRepository.save(level_SEARCH_GROUP_RESP);
            levelCacheRepository.save(level_SEARCH_GROUP_LIMIT);
            levelCacheRepository.save(level_SEARCH_GROUP_END);
            levelCacheRepository.save(level_ADD_PARTNER);
            levelCacheRepository.save(level_ADD_GROUP);
            levelCacheRepository.save(level_ACTIONS);
            levelCacheRepository.save(level_BASKETS_FOR_SHOP);
            levelCacheRepository.save(level_ACTION_TYPE);
            levelCacheRepository.save(level_SELECT_LEVEL_TYPE);
            levelCacheRepository.save(level_MULTI_ACTION_LEVEL);
            levelCacheRepository.save(level_MULTI_ACTION_LEVEL_BASIC);
            levelCacheRepository.save(level_MULTI_LEVEL_RATE);
            levelCacheRepository.save(level_MULTI_LEVEL_RATE_BASIC);
            levelCacheRepository.save(level_MULTI_LEVEL_QUESTION);
            levelCacheRepository.save(level_MULTI_LEVEL_QUESTION_BASIC);
            levelCacheRepository.save(level_ONE_LEVEL_RATE);
            levelCacheRepository.save(level_ONE_LEVEL_RATE_BASIC);
            levelCacheRepository.save(level_ACTION_RATE_WITHDRAW);
            levelCacheRepository.save(level_ACTION_RATE_WITHDRAW_BASIC);
            levelCacheRepository.save(level_ACTION_RATE_PARTNER);
            levelCacheRepository.save(level_ADD_ACTION_RATE_SOURCE);
            levelCacheRepository.save(level_ADD_ACTION_COUPON_TARGET);
            levelCacheRepository.save(level_COUPON);
            levelCacheRepository.save(level_COUPON_NUMBER);
            levelCacheRepository.save(level_COUPON_RATE_WITHDRAW);
            levelCacheRepository.save(level_ADD_ACTION_COUPON_SOURCE);
            levelCacheRepository.save(level_ADD_ACTION_RATE_TARGET);
            levelCacheRepository.save(level_LINK_TO_PRODUCT);
            levelCacheRepository.save(level_LINK_TO_PRODUCT_NUMBER);
            levelCacheRepository.save(level_ADD_ACTION_LINK_SOURCE);
            levelCacheRepository.save(level_ADD_ACTION_LINK_TARGET);
            levelCacheRepository.save(level_MY_SHOPS);
            levelCacheRepository.save(level_CASHBACKS);
            levelCacheRepository.save(level_CONNECT);
            levelCacheRepository.save(level_CONNECT_SHOP);
            levelCacheRepository.save(level_BOOKMARKS);
            levelCacheRepository.save(level_ADD_BOOKMARK);
            levelCacheRepository.save(level_BASKET);
            levelCacheRepository.save(level_BASKET_ARCHIVE);
            levelCacheRepository.save(level_ADD_BASKET);
            levelCacheRepository.save(level_SEARCH);
            levelCacheRepository.save(level_SEARCH_RESULT);
            levelCacheRepository.save(level_SEARCH_RESULT_PRODUCT);
            levelCacheRepository.save(level_ADMIN_SHOPS);
            levelCacheRepository.save(level_CONSTRUCT);
            levelCacheRepository.save(level_CONSTRUCT_ADD);
            levelCacheRepository.save(level_CONSTRUCT_SARAFAN_SHARE);
            levelCacheRepository.save(level_CONSTRUCT_MIN_BILL_SHARE);
            levelCacheRepository.save(level_ADD_GOODS);
            levelCacheRepository.save(level_ADD_GOODS_NAME);
            levelCacheRepository.save(level_ADD_GOODS_PHOTO);
            levelCacheRepository.save(level_ADD_GOODS_DESCRIPTION);
            levelCacheRepository.save(level_ADD_GOODS_PRICE);
            levelCacheRepository.save(level_ADD_GOODS_END);
            levelCacheRepository.save(level_ADD_BOT);
            levelCacheRepository.save(level_ADD_TAXI_BOT);
            levelCacheRepository.save(level_TAXI_LOCATION);
            levelCacheRepository.save(level_TAXI_SUBMIT);
            levelCacheRepository.save(level_PSHARE2P);
            levelCacheRepository.save(level_P2NOP);
            levelCacheRepository.save(level_P2NOP_RESP);
            levelCacheRepository.save(level_P2P);
            levelCacheRepository.save(level_P2P_RESP);
            levelCacheRepository.save(level_P2B);
            levelCacheRepository.save(level_NEGATIVE_SUM);
            levelCacheRepository.save(level_NEGATIVE_COUNT);
            levelCacheRepository.save(level_P2B_PROPOSE_CASHBACK);
            levelCacheRepository.save(level_P2B_PROPOSE_CASHBACK_RESP);
            levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK);
            levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_RESP);
            levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_REQUEST);
            levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_APPROVE);
            levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_DISMISS);
            levelCacheRepository.save(level_P2B_CHARGE_BASKET_CASHBACK);
            levelCacheRepository.save(level_P2B_APPROVE_BASKET_CASHBACK);
            levelCacheRepository.save(level_P2B_CHARGE_COUPON);
            levelCacheRepository.save(level_P2B_CHARGE_COUPON_BASKET);
            levelCacheRepository.save(level_P2B_CHARGE_COUPON_REQUEST);
            levelCacheRepository.save(level_P2B_CHARGE_COUPON_RESP);
            levelCacheRepository.save(level_P2B_WRITEOFF_COUPON);
            levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_SELECT_ACTION);
            levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_REQUEST);
            levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_RESP);
            levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_APPROVE);
            levelCacheRepository.save(level_P2B_WRITEOFF_COUPON);
            levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_BASKET);
            levelCacheRepository.save(level_P2B_RESP);
            levelCacheRepository.save(level_B2B);
            levelCacheRepository.save(level_B2NOB);
            levelCacheRepository.save(level_DISCARD_NEW_PARTNER);
            levelCacheRepository.save(level_APPROVE_NEW_PARTNER);
            levelCacheRepository.save(level_NEW_GRUPP);
            levelCacheRepository.save(level_DISCARD_NEW_GRUPP);
            levelCacheRepository.save(level_APPROVE_NEW_GRUPP);
            levelCacheRepository.save(level0_1_1);
            levelCacheRepository.save(level_RESPONSE_BUYER_MESSAGE);
            levelCacheRepository.save(level_NON_RESPONSE);
            levelCacheRepository.save(level_RESPONSE_SHOP_MESSAGE);
            levelCacheRepository.save(level0_1_3);
            levelCacheRepository.save(level0_1_4);
            levelCacheRepository.save(level0_1_5);
            levelCacheRepository.save(level_EDIT_BUTTON_NAME);
            levelCacheRepository.save(level_EDIT_MESSAGE);
            levelCacheRepository.save(level_NEW_LEVEL_END_BUTTON);
            levelCacheRepository.save(level_NEW_LEVEL_INPUT_BUTTON);
            levelCacheRepository.save(level_NEW_LEVEL_BUTTON);

            try {
                Users = userRepository.findByChatId(1L);

                System.out.println(1200);
                System.out.println(Users);
                if (Users == null) {
                    Users = new Users(e -> {
                        e.setChatId(1L);
                        e.setName("SkidoBOT");
                        e.setSessionId("1");
                        e.setShopOwner(true);
                        e.setLanguage(RU);
                    });
                    userRepository.save(Users);
                    System.out.println(1211);
                    System.out.println(Users);

                    userCacheRepository.save(usersMapper.toDto(Users));
                }

                SHOP = shopRepository.findById(1).orElse(null);
                if (SHOP == null) {
                    SHOP = new Shop();
                    SHOP.setName("DEFAULT");
                    SHOP.setAdminUser(Users);
                    SHOP.setChatId(1L);
                    shopRepository.save(SHOP);
                    shopCacheRepository.save(shopMapper.toDto(SHOP));

                    System.out.println("SHOP----------" + SHOP);

                    SHOP.getSellerSet().add(Users);

                    shopRepository.save(SHOP);
                    shopCacheRepository.save(shopMapper.toDto(SHOP));

                    bigCategorySuperGroupDTO = new CategorySuperGroup ();
                    bigCategorySuperGroup.setAlias("big");
                    categorySuperGroupCacheRepository.save(bigCategorySuperGroup);


                    System.out.println("bigCategorySuperGroupDTO+++++++++++++++++++++++" + bigCategorySuperGroup);

                    bigCategoryGroupDTO = new CategoryGroup ();
                    bigCategoryGroup.setCategorySuperGroup(bigCategorySuperGroup.getId());
                    bigCategoryGroup.setAlias("big");
                    categoryGroupCacheRepository.save(bigCategoryGroup);

                    bigCategoryDTO = new Category ();
                    bigCategory.setCategorySuperGroup(bigCategorySuperGroup.getId());
                    bigCategory.setCategoryGroup(bigCategoryGroup.getId());
                    bigCategory.setActual(true);
                    bigCategory.setAlias("big");
                    categoryCacheRepository.save(bigCategory);

                    Shop lamoda = new Shop();
                    lamoda.setName("lamoda");
                    lamoda.setAdminUser(Users);
                    lamoda.setChatId(1L);
                    shopRepository.save(lamoda);
                    shopCacheRepository.save(shopMapper.toDto(lamoda));
                    System.out.println("lamoda----------" + lamoda);
                    lamoda.getSellerSet().add(Users);
                    shopRepository.save(lamoda);
                    shopCacheRepository.save(shopMapper.toDto(lamoda));

                    Shop wildberries = new Shop();
                    wildberries.setName("wildberries");
                    wildberries.setAdminUser(Users);
                    wildberries.setChatId(1L);
                    shopRepository.save(wildberries);
                    shopCacheRepository.save(shopMapper.toDto(wildberries));
                    System.out.println("wildberries----------" + wildberries);
                    wildberries.getSellerSet().add(Users);
                    shopRepository.save(wildberries);
                    shopCacheRepository.save(shopMapper.toDto(wildberries));

                    Shop ozon = new Shop();
                    ozon.setName("ozon");
                    ozon.setAdminUser(Users);
                    ozon.setChatId(1L);
                    shopRepository.save(ozon);
                    shopCacheRepository.save(shopMapper.toDto(ozon));
                    System.out.println("ozon----------" + ozon);
                    ozon.getSellerSet().add(Users);
                    shopRepository.save(ozon);
                    shopCacheRepository.save(shopMapper.toDto(ozon));
                }

                System.out.println("CHAT.getChatId()***" + Users.getChatId());


                //////////СТАРТОВОЕ МЕНЮ для пользователя с магазинами

                level_INITIALIZE.updateLevel(Users, INITIALIZE.name(), level_INITIALIZE222, false);

                levelCacheRepository.save(level_INITIALIZE);
                Message message0_1 = new Message(level_INITIALIZE, Map.of(RU, "Добро пожаловать в Skidozona"));
                messageCacheRepository.save(message0_1);
                level_INITIALIZE.addMessage(message0_1);

                ButtonRow row0_00 = new ButtonRow(level_INITIALIZE);
                buttonRowRepository.save(row0_00);
                level_INITIALIZE.addRow(row0_00);
                Button button0_0_00 = new Button(row0_00, Map.of(RU, "Мониторить цену в WB, Ozone, LaModa.."), level_MONITOR.getIdString());
                buttonCacheRepository.save(button0_0_00);
                row0_00.add(button0_0_00);

                ButtonRow row0_0 = new ButtonRow(level_INITIALIZE);
                buttonRowRepository.save(row0_0);
                level_INITIALIZE.addRow(row0_0);
                Button button0_0_0 = new Button(row0_0, Map.of(RU, "Кешбеки"), level_CASHBACKS.getIdString());
                buttonCacheRepository.save(button0_0_0);
                row0_0.add(button0_0_0);
                Button button0_0_1 = new Button(row0_0, Map.of(RU, "Закладки"), level_BOOKMARKS.getIdString());
                buttonCacheRepository.save(button0_0_1);
                row0_0.add(button0_0_1);
                System.out.println("D+++" + row0_0);
//                buttonRowRepository.save(row0_0);
                System.out.println("E+++" + row0_0);
                ButtonRow row0_1 = new ButtonRow(level_INITIALIZE);
                buttonRowRepository.save(row0_1);
                level_INITIALIZE.addRow(row0_1);
                Button button0_1_0 = new Button(row0_1, Map.of(RU, "Корзина"), level_BASKET.getIdString());
                buttonCacheRepository.save(button0_1_0);
                row0_1.add(button0_1_0);
                Button button0_1_1 = new Button(row0_1, Map.of(RU, "Поиск"), level_SEARCH.getIdString());
                buttonCacheRepository.save(button0_1_1);
                row0_1.add(button0_1_1);
//                buttonRowRepository.save(row0_1);
                ButtonRow row0_2 = new ButtonRow(level_INITIALIZE);
                buttonRowRepository.save(row0_2);
                level_INITIALIZE.addRow(row0_2);
                Button button0_2_0 = new Button(row0_2, Map.of(RU, "Соединить"), level_CONNECT.getIdString());
                buttonCacheRepository.save(button0_2_0);
                row0_2.add(button0_2_0);
                Button button0_2_1 = new Button(row0_2, Map.of(RU, "Админка"), level_ADMIN.getIdString());
                buttonCacheRepository.save(button0_2_1);
                row0_2.add(button0_2_1);
                ButtonRow row0_3 = new ButtonRow(level_INITIALIZE);
                buttonRowRepository.save(row0_3);
                Button button0_3_1 = new Button(row0_3, Map.of(RU, "EN/DE/RU"), level_LANGUAGES.getIdString());
                buttonCacheRepository.save(button0_3_1);
                row0_3.add(button0_3_1);
                Button button0_1_0_11 = new Button(row0_3, Map.of(RU, "Архив Корзин"), level_BASKET_ARCHIVE.getIdString());
                buttonCacheRepository.save(button0_1_0_11);
                row0_3.add(button0_1_0_11);
//                buttonRowRepository.save(row0_2);

                //////////СТАРТОВОЕ МЕНЮ для пользователя без магазинов

                level_INITIALIZE0.updateLevel(Users, INITIALIZE0.name(), level_INITIALIZE, true);

                levelCacheRepository.save(level_INITIALIZE0);
                Message message00_1 = new Message(level_INITIALIZE0, Map.of(RU, "Добро пожаловать в Skidozona"));
                messageCacheRepository.save(message00_1);
                level_INITIALIZE0.addMessage(message00_1);
                ButtonRow row00_0 = new ButtonRow(level_INITIALIZE0);
                buttonRowRepository.save(row00_0);
                level_INITIALIZE0.addRow(row00_0);
                Button button00_0_0 = new Button(row00_0, Map.of(RU, "Кешбеки"), level_CASHBACKS.getIdString());
                buttonCacheRepository.save(button00_0_0);
                row00_0.add(button00_0_0);
                Button button00_0_1 = new Button(row00_0, Map.of(RU, "Закладки"), level_BOOKMARKS.getIdString());
                buttonCacheRepository.save(button00_0_1);
                row00_0.add(button00_0_1);
//                buttonRowRepository.save(row00_0);
                ButtonRow row00_1 = new ButtonRow(level_INITIALIZE0);
                buttonRowRepository.save(row00_1);
                level_INITIALIZE0.addRow(row00_1);
                Button button00_1_0 = new Button(row00_1, Map.of(RU, "Корзина"), level_BASKET.getIdString());
                buttonCacheRepository.save(button00_1_0);
                row00_1.add(button00_1_0);
                Button button00_1_1 = new Button(row00_1, Map.of(RU, "Поиск"), level_SEARCH.getIdString());
                buttonCacheRepository.save(button00_1_1);
                row00_1.add(button00_1_1);
//                buttonRowRepository.save(row00_1);
                ButtonRow row00_2 = new ButtonRow(level_INITIALIZE0);
                buttonRowRepository.save(row00_2);
                level_INITIALIZE0.addRow(row00_2);
                Button button00_2_0 = new Button(row00_2, Map.of(RU, "Соединить"), level_CONNECT.getIdString());
                buttonCacheRepository.save(button00_2_0);
                row00_2.add(button00_2_0);
                Button button00_2_1 = new Button(row00_2, Map.of(RU, "Мои магазины"), level_MY_SHOPS.getIdString());
                buttonCacheRepository.save(button00_2_1);
                row00_2.add(button00_2_1);
//                buttonRowRepository.save(row00_2);
                ButtonRow row00_3 = new ButtonRow(level_INITIALIZE0);
                buttonRowRepository.save(row00_3);
                level_INITIALIZE0.addRow(row00_3);
                Button button00_3_0 = new Button(row00_3, Map.of(RU, "Создать магазин/сервис"), level_CONSTRUCT.getIdString());
                buttonCacheRepository.save(button00_3_0);
                row00_3.add(button00_3_0);
                Button button00_3_1 = new Button(row00_3, Map.of(RU, "EN/DE/RU"), level_LANGUAGES.getIdString());
                buttonCacheRepository.save(button00_3_1);
                row00_3.add(button00_3_1);
                Button button000_1_0_11 = new Button(row00_3, Map.of(RU, "Архив Корзин"), level_BASKET_ARCHIVE.getIdString());
                buttonCacheRepository.save(button000_1_0_11);
                row00_3.add(button000_1_0_11);
//                buttonRowRepository.save(row00_3);

                //////////ОБЩЕЕ ДЛЯ ЯЗЫКОВ
                level_LANGUAGES.updateLevel(Users, LANGUAGES.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_LANGUAGES);
                Message message00_1_ = new Message(level_LANGUAGES, Map.of(RU, "Choose another language"));
                messageCacheRepository.save(message00_1_);
                level_LANGUAGES.addMessage(message00_1_);

                ButtonRow row0_2_lang = new ButtonRow(level_LANGUAGES);
                buttonRowRepository.save(row0_2_lang);
                level_LANGUAGES.addRow(row0_2_lang);
                Button button0_2_0_lang = new Button(row0_2_lang, Map.of(RU, "RU", EN,  "RU", DE,  "RU"),  level_LANGUAGER.getIdString() + "RU");
                buttonCacheRepository.save(button0_2_0_lang);
                row0_2_lang.add(button0_2_0_lang);
                Button button0_2_1_lang = new Button(row0_2_lang, Map.of(RU, "EN", EN,  "EN", DE,  "EN"),  level_LANGUAGER.getIdString() + "EN");
                buttonCacheRepository.save(button0_2_1_lang);
                row0_2_lang.add(button0_2_1_lang);
                Button button0_2_2_lang = new Button(row0_2_lang, Map.of(RU, "DE", EN,  "DE", DE,  "DE"),  level_LANGUAGER.getIdString() + "DE");
                buttonCacheRepository.save(button0_2_2_lang);
                row0_2_lang.add(button0_2_2_lang);

                //////////ОБРАБОТЧИК ВЫБОРА ЯЗЫКОВ
                level_LANGUAGER.updateLevel(Users, LANGUAGER.name(), level_LANGUAGES, false);

                levelCacheRepository.save(level_LANGUAGER);
                Message message00_1_r = new Message(level_LANGUAGER, Map.of(RU, "Language successfully changed"));
                messageCacheRepository.save(message00_1_r);
                level_LANGUAGER.addMessage(message00_1_r);

                //////////ДОБАВОЧНОЕ СТАРТОВОЕ МЕНЮ ДЛЯ МАГАЗИНА

                level_ADMIN.updateLevel(Users, ADMIN.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_ADMIN);

                ButtonRow row0_2_admin = new ButtonRow(level_ADMIN);
                buttonRowRepository.save(row0_2_admin);
                level_ADMIN.addRow(row0_2_admin);
                Button button0_2_0_admin = new Button(row0_2_admin, Map.of(RU, "Создать магазин/сервис"), level_CONSTRUCT.getIdString());
                buttonCacheRepository.save(button0_2_0_admin);
                row0_2_admin.add(button0_2_0_admin);
                Button button0_2_1_admin = new Button(row0_2_admin, Map.of(RU, "Админить магазины/сервисы"), level_ADMIN_SHOPS.getIdString());
                buttonCacheRepository.save(button0_2_1_admin);
                row0_2_admin.add(button0_2_1_admin);


                //////////ДОБАВОЧНОЕ СТАРТОВОЕ МЕНЮ ДЛЯ МАГАЗИНА

                level_ADMIN_ADMIN.updateLevel(Users, ADMIN_ADMIN.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_ADMIN_ADMIN);
                ButtonRow row0_0_admin0 = new ButtonRow(level_ADMIN_ADMIN);
                buttonRowRepository.save(row0_0_admin0);
                level_ADMIN_ADMIN.addRow(row0_0_admin0);
                Button button0_0_0_shop0 = new Button(row0_0_admin0, Map.of(RU, "Акции"), level_ACTIONS.getIdString());
                buttonCacheRepository.save(button0_0_0_shop0);
                row0_0_admin0.add(button0_0_0_shop0);
                Button button0_0_1_shop0 = new Button(row0_0_admin0, Map.of(RU, "Корзины"), level_BASKETS_FOR_SHOP.getIdString());
                buttonCacheRepository.save(button0_0_1_shop0);
                row0_0_admin0.add(button0_0_1_shop0);
                ButtonRow row0_1_admin0 = new ButtonRow(level_ADMIN_ADMIN);
                buttonRowRepository.save(row0_1_admin0);
                level_ADMIN_ADMIN.addRow(row0_1_admin0);
                Button button0_1_0_shop0 = new Button(row0_1_admin0, Map.of(RU, "Товары"), level_GOODS_LIST.getIdString());
                buttonCacheRepository.save(button0_1_0_shop0);
                row0_1_admin0.add(button0_1_0_shop0);
                Button button0_1_1_shop0 = new Button(row0_1_admin0, Map.of(RU, "Партнеры"), level_PARTNERS.getIdString());
                buttonCacheRepository.save(button0_1_1_shop0);
                row0_1_admin0.add(button0_1_1_shop0);
                ButtonRow row0_2_admin0 = new ButtonRow(level_ADMIN_ADMIN);
                buttonRowRepository.save(row0_2_admin0);
                level_ADMIN_ADMIN.addRow(row0_2_admin0);
                Button button1_0_1 = new Button(row0_2_admin0, Map.of(RU, "Бот магазина"), level_SHOP_BOTS.getIdString());
                buttonCacheRepository.save(button1_0_1);
                row0_2_admin0.add(button1_0_1);
                Button button0_2_1_shop0 = new Button(row0_2_admin0, Map.of(RU, "Продавцы"), level_SELLERS.getIdString());
                buttonCacheRepository.save(button0_2_1_shop0);
                row0_2_admin0.add(button0_2_1_shop0);
                ButtonRow row0_3_admin0 = new ButtonRow(level_ADMIN_ADMIN);
                buttonRowRepository.save(row0_3_admin0);
                level_ADMIN_ADMIN.addRow(row0_3_admin0);
                Button button0_3_1_shop0 = new Button(row0_3_admin0, Map.of(RU, "Geo-карта"), level_MAP.getIdString(), "https://t.me/Skido_bot/geomapper");
                buttonCacheRepository.save(button0_3_1_shop0);
                row0_3_admin0.add(button0_3_1_shop0);



                //////////КАРТА
                level_MAP.updateLevel(Users, GEOMAP.name(), level_ADMIN_ADMIN, false);

                levelCacheRepository.save(level_MAP);
                Message message00_1__ = new Message(level_MAP, Map.of(RU, "Перейдите на страницу редактрирования местоположения вашего объекта https://skidozona.by/geomapper"));
                messageCacheRepository.save(message00_1__);
                level_MAP.addMessage(message00_1__);
                ButtonRow row0_0_geo = new ButtonRow(level_MAP);
                buttonRowRepository.save(row0_0_geo);
                level_MAP.addRow(row0_0_geo);
                Button button0_geo = new Button(row0_0_geo, Map.of(RU, "Акции"), "123");
                button0_geo.setWebApp("https://t.me/Skido_bot/geomapper");
                buttonCacheRepository.save(button0_geo);
                row0_0_geo.add(button0_geo);



                ///////////////////////// СПИСОК ПРОДАВЦОВ

                level_SELLERS.updateLevel(Users, SELLERS.name(), level_ADMIN_ADMIN, false);

                levelCacheRepository.save(level_SELLERS);
                Message message460_1 = new Message(level_SELLERS, Map.of(RU, "Список моих продавцов: "));
                messageCacheRepository.save(message460_1);
                level_SELLERS.addMessage(message460_1);
//                ButtonRow row460_0 = new ButtonRow(level_SELLERS);
//                buttonRowRepository.save(row460_0);
//                Button button460_0_0 = new Button(row460_0, Map.of(RU, "Добавить нового продавца"), level_SELLERS_ADD.getIdString());
//                buttonCacheRepository.save(button460_0_0);
//                row460_0.add(button460_0_0);


                level_SELLERS_ADD.updateLevel(Users, SELLERS_ADD.name(), level_SELLERS, false);

                levelCacheRepository.save(level_SELLERS_ADD);
                ButtonRow row462_0_0 = new ButtonRow(level_SELLERS_ADD);
                buttonRowRepository.save(row462_0_0);
                Button button462_0_0_0 = new Button(row462_0_0, Map.of(RU, "Добавить продавца"), level_SELLERS_ADD_APPROVE.getIdString());
                buttonCacheRepository.save(button462_0_0_0);
                row462_0_0.add(button462_0_0_0);
                Button button462_0_0_1 = new Button(row462_0_0, Map.of(RU, "Отмена"), level_SELLERS_ADD_DISMISS.getIdString());
                buttonCacheRepository.save(button462_0_0_1);
                row462_0_0.add(button462_0_0_1);
                level_SELLERS_ADD.addRow(row462_0_0);


                level_SELLERS_ADD_APPROVE.updateLevel(Users, SELLERS_ADD_APPROVE.name(), level_SELLERS_ADD, false);

                levelCacheRepository.save(level_SELLERS_ADD_APPROVE);
                Message message4601_1 = new Message(level_SELLERS_ADD_APPROVE, Map.of(RU, "Продавец добавлен"));
                messageCacheRepository.save(message4601_1);
                level_SELLERS_ADD_APPROVE.addMessage(message4601_1);


                level_SELLERS_ADD_DISMISS.updateLevel(Users, SELLERS_ADD_DISMISS.name(), level_SELLERS_ADD, false);

                levelCacheRepository.save(level_SELLERS_ADD_DISMISS);
                Message message4602_1 = new Message(level_SELLERS_ADD_DISMISS, Map.of(RU, "Добавление отменено"));
                messageCacheRepository.save(message4602_1);
                level_SELLERS_ADD_DISMISS.addMessage(message4602_1);

                level_SELLERS_REMOVE.updateLevel(Users, SELLERS_REMOVE.name(), level_SELLERS, false);

                levelCacheRepository.save(level_SELLERS_REMOVE);
                ButtonRow row463_0_0 = new ButtonRow(level_SELLERS_REMOVE);
                buttonRowRepository.save(row463_0_0);
                Button button463_0_0_0 = new Button(row463_0_0, Map.of(RU, "Удалить продавца"), level_SELLERS_REMOVE_APPROVE.getIdString());
                buttonCacheRepository.save(button463_0_0_0);
                row463_0_0.add(button463_0_0_0);
                Button button463_0_0_1 = new Button(row463_0_0, Map.of(RU, "Отмена"), level_SELLERS_REMOVE_DISMISS.getIdString());
                buttonCacheRepository.save(button463_0_0_1);
                row463_0_0.add(button463_0_0_1);
                level_SELLERS_REMOVE.addRow(row463_0_0);

                level_SELLERS_REMOVE_APPROVE.updateLevel(Users, SELLERS_REMOVE_APPROVE.name(), level_SELLERS_REMOVE, false);

                levelCacheRepository.save(level_SELLERS_REMOVE_APPROVE);
                Message message4603_1 = new Message(level_SELLERS_REMOVE_APPROVE, Map.of(RU, "Удаление отменено"));
                messageCacheRepository.save(message4603_1);
                level_SELLERS_REMOVE_APPROVE.addMessage(message4603_1);

                level_SELLERS_REMOVE_DISMISS.updateLevel(Users, SELLERS_REMOVE_DISMISS.name(), level_SELLERS_REMOVE, false);

                levelCacheRepository.save(level_SELLERS_REMOVE_DISMISS);
                Message message4604_1 = new Message(level_SELLERS_REMOVE_DISMISS, Map.of(RU, "Продавец удален"));
                messageCacheRepository.save(message4604_1);
                level_SELLERS_REMOVE_DISMISS.addMessage(message4604_1);


                ///////////////////////// СПИСОК ПАРТНЕРОВ

                level_PARTNERS.updateLevel(Users, PARTNERS.name(), level_ADMIN_ADMIN, false);

                levelCacheRepository.save(level_PARTNERS);
                Message message46_1 = new Message(level_PARTNERS, Map.of(RU, "Список моих партнеров: "));
                messageCacheRepository.save(message46_1);
                level_PARTNERS.addMessage(message46_1);
                ButtonRow row46_0 = new ButtonRow(level_PARTNERS);
                buttonRowRepository.save(row46_0);
                Button button46_0_0 = new Button(row46_0, Map.of(RU, "Найти нового партнера"), level_SEARCH_PARTNER.getIdString());
                buttonCacheRepository.save(button46_0_0);
                row46_0.add(button46_0_0);
                level_PARTNERS.addRow(row46_0);

                ButtonRow row46_1 = new ButtonRow(level_PARTNERS);
                buttonRowRepository.save(row46_1);
                Button button46_0_1 = new Button(row46_1, Map.of(RU, "Редактировать партнеров"), level_EDIT_PARTNER.getIdString());
                buttonCacheRepository.save(button46_0_1);
                row46_1.add(button46_0_1);
                level_PARTNERS.addRow(row46_1);

                ButtonRow row46_2 = new ButtonRow(level_PARTNERS);
                buttonRowRepository.save(row46_2);
                Button button46_0_2 = new Button(row46_2, Map.of(RU, "Войти в новую группу кэшбеков"), level_SEARCH_GROUP.getIdString());
                buttonCacheRepository.save(button46_0_2);
                row46_2.add(button46_0_2);
                level_PARTNERS.addRow(row46_2);


                level_WITHDRAW_PARTNER.updateLevel(Users, WITHDRAW_PARTNER.name(), level_PARTNERS, false);

                levelCacheRepository.save(level_WITHDRAW_PARTNER);

                level_WITHDRAW_PARTNER_GROUP.updateLevel(Users, WITHDRAW_PARTNER_GROUP.name(), level_PARTNERS, false);

                levelCacheRepository.save(level_WITHDRAW_PARTNER_GROUP);

                level_WITHDRAW_PARTNER_RESP.updateLevel(Users, WITHDRAW_PARTNER_RESP.name(), level_WITHDRAW_PARTNER, false);

                levelCacheRepository.save(level_WITHDRAW_PARTNER_RESP);
                Message message47_01 = new Message(level_WITHDRAW_PARTNER_RESP, Map.of(RU, "Введите сумму списания"));
                messageCacheRepository.save(message47_01);
                level_WITHDRAW_PARTNER_RESP.addMessage(message47_01);

                level_WITHDRAW_PARTNER_END.updateLevel(Users, WITHDRAW_PARTNER_END.name(), level_WITHDRAW_PARTNER_RESP, true);

                levelCacheRepository.save(level_WITHDRAW_PARTNER_END);
                Message message472_3_1 = new Message(level_WITHDRAW_PARTNER_END, Map.of(RU, "Вы списали долг WW для EE"));
                messageCacheRepository.save(message472_3_1);
                level_WITHDRAW_PARTNER_END.addMessage(message472_3_1);

///это я проверил 12.05.2021 отсюда
                level_SEARCH_PARTNER.updateLevel(Users, SEARCH_PARTNER.name(), level_PARTNERS, false);

                levelCacheRepository.save(level_SEARCH_PARTNER);
                Message message47_1 = new Message(level_SEARCH_PARTNER, Map.of(RU, "Найдите нужный магазин. Введите название "));
                messageCacheRepository.save(message47_1);
                level_SEARCH_PARTNER.addMessage(message47_1);



                level_EDIT_PARTNER.updateLevel(Users, EDIT_PARTNER.name(), level_PARTNERS, false);

                levelCacheRepository.save(level_EDIT_PARTNER);
                Message message47_1_1_01 = new Message(level_EDIT_PARTNER, Map.of(RU, "Редактировать партнеров"));
                messageCacheRepository.save(message47_1_1_01);
                level_EDIT_PARTNER.addMessage(message47_1_1_01);



                level_SEARCH_PARTNER_RESP_BUTTON.updateLevel(Users, SEARCH_PARTNER_RESP_BUTTON.name(), level_SEARCH_PARTNER, true);

                levelCacheRepository.save(level_SEARCH_PARTNER_RESP_BUTTON);
                Message message47_1_1_1 = new Message(level_SEARCH_PARTNER_RESP_BUTTON, Map.of(RU, "Результаты поиска новых партнеров"));
                messageCacheRepository.save(message47_1_1_1);
                level_SEARCH_PARTNER_RESP_BUTTON.addMessage(message47_1_1_1);


                level_SEARCH_PARTNER_RESP.updateLevel(Users, SEARCH_PARTNER_RESP.name(), level_SEARCH_PARTNER, true);

                levelCacheRepository.save(level_SEARCH_PARTNER_RESP);
                Message message47_1_1 = new Message(level_SEARCH_PARTNER_RESP, Map.of(RU, "Результаты поиска партнеров"));
                messageCacheRepository.save(message47_1_1);
                level_SEARCH_PARTNER_RESP.addMessage(message47_1_1);

                level_SEARCH_PARTNER_RATE.updateLevel(Users, SEARCH_PARTNER_RATE.name(), level_SEARCH_PARTNER_RESP, true);

                levelCacheRepository.save(level_SEARCH_PARTNER_RATE);
                Message message47_2_1 = new Message(level_SEARCH_PARTNER_RATE, Map.of(RU, "Введите размер кэшбека в процентах по рекомендациям партнера"));
                messageCacheRepository.save(message47_2_1);
                level_SEARCH_PARTNER_RATE.addMessage(message47_2_1);

                level_SEARCH_PARTNER_LIMIT.updateLevel(Users, SEARCH_PARTNER_LIMIT.name(), level_SEARCH_PARTNER_RATE, true);

                levelCacheRepository.save(level_SEARCH_PARTNER_LIMIT);
                Message message47_3_1 = new Message(level_SEARCH_PARTNER_LIMIT, Map.of(RU, "Введите размер лимита для партнера"));
                messageCacheRepository.save(message47_3_1);
                level_SEARCH_PARTNER_LIMIT.addMessage(message47_3_1);

                level_SEARCH_PARTNER_END.updateLevel(Users, SEARCH_PARTNER_END.name(), level_SEARCH_PARTNER_LIMIT, true);

                levelCacheRepository.save(level_SEARCH_PARTNER_END);
                Message message471_3_1 = new Message(level_SEARCH_PARTNER_END, Map.of(RU, "Вы установили лимит WW для  EE"));
                messageCacheRepository.save(message471_3_1);
                level_SEARCH_PARTNER_END.addMessage(message471_3_1);
/// 12.05.2021 досюда

                level_SEARCH_GROUP.updateLevel(Users, SEARCH_GROUP.name(), level_PARTNERS, false);

                levelCacheRepository.save(level_SEARCH_GROUP);
                Message message47_g_1 = new Message(level_SEARCH_GROUP, Map.of(RU, "Введите название группы"));
                messageCacheRepository.save(message47_g_1);
                level_SEARCH_GROUP.addMessage(message47_g_1);

                level_SEARCH_GROUP_RESP.updateLevel(Users, SEARCH_GROUP_RESP.name(), level_SEARCH_GROUP, true);

                levelCacheRepository.save(level_SEARCH_GROUP_RESP);
                Message message47_1_g_1 = new Message(level_SEARCH_GROUP_RESP, Map.of(RU, "Результаты поиска групп"));
                messageCacheRepository.save(message47_1_g_1);
                level_SEARCH_GROUP_RESP.addMessage(message47_1_g_1);

                level_SEARCH_GROUP_LIMIT.updateLevel(Users, SEARCH_GROUP_LIMIT.name(), level_SEARCH_GROUP_RESP, true);

                levelCacheRepository.save(level_SEARCH_GROUP_LIMIT);
                Message message47_2_g_1 = new Message(level_SEARCH_GROUP_LIMIT, Map.of(RU, "Введите размер лимита кэшбека по группе"));
                messageCacheRepository.save(message47_2_g_1);
                level_SEARCH_GROUP_LIMIT.addMessage(message47_2_g_1);

                level_SEARCH_GROUP_END.updateLevel(Users, SEARCH_GROUP_END.name(), level_SEARCH_GROUP_LIMIT, true);

                levelCacheRepository.save(level_SEARCH_GROUP_END);
                Message message4700_3_1 = new Message(level_SEARCH_GROUP_END, Map.of(RU, "Вы установили лимит WW для  EE"));
                messageCacheRepository.save(message4700_3_1);
                level_SEARCH_GROUP_END.addMessage(message4700_3_1);

                ///////выводится в цикле как результат поиска
                level_ADD_PARTNER.updateLevel(Users, ADD_PARTNER.name(), level_SEARCH_PARTNER_END, false);

                levelCacheRepository.save(level_ADD_PARTNER);
                ButtonRow row48_0 = new ButtonRow(level_ADD_PARTNER);
                buttonRowRepository.save(row48_0);
                level_ADD_PARTNER.addRow(row48_0);
                Button button48_0_0 = new Button(row48_0, Map.of(RU, "Добавить партнера"), level_B2NOB.getIdString());
                buttonCacheRepository.save(button48_0_0);
                row48_0.add(button48_0_0);


            ///////выводится в цикле как результат поиска
//                    level_ADD_GROUP.updateLevel(CHAT, ADD_GROUP.name(), level_SEARCH_GROUP_LIMIT, false);
//                    levelCacheRepository.save(level_ADD_GROUP);
//                    ButtonRow row48_g_0 = new ButtonRow();
//                    Button button48_g_0_0 = new Button(level_ADD_GROUP, "Добавить групппу", level_NEW_GRUPP.getIdString());
//                    buttonRepository.save(button48_g_0_0);
//                    level_ADD_GROUP.addRow(row48_g_0);



                ///////////////////////// СПИСОК АКЦИЙ МАГАЗИНА ACTIONS

                level_ACTIONS.updateLevel(Users, ACTIONS.name(), level_ADMIN_ADMIN, false);

                levelCacheRepository.save(level_ACTIONS);
                Message message25_1 = new Message(level_ACTIONS, Map.of(RU, "Список моих акций"));
                messageCacheRepository.save(message25_1);
                level_ACTIONS.addMessage(message25_1);
                ButtonRow row25_0 = new ButtonRow(level_ACTIONS);
                buttonRowRepository.save(row25_0);
                Button button25_0_0 = new Button(row25_0, Map.of(RU, "Создать новую акцию"), level_ACTION_TYPE.getIdString());
                buttonCacheRepository.save(button25_0_0);
                row25_0.add(button25_0_0);
                level_ACTIONS.addRow(row25_0);


                level_ACTION_TYPE.updateLevel(Users, ACTION_TYPE.name(), level_ACTIONS, false);

                levelCacheRepository.save(level_ACTION_TYPE);
                Message message26_1 = new Message(level_ACTION_TYPE, Map.of(RU, "Выберите тип акции:"));
                messageCacheRepository.save(message26_1);
                level_ACTION_TYPE.addMessage(message26_1);
                ButtonRow row26_0 = new ButtonRow(level_ACTION_TYPE);
                buttonRowRepository.save(row26_0);
                Button button26_0_0 = new Button(row26_0, Map.of(RU, ActionTypeEnum.BASIC_DEFAULT.getValue()), level_BASIC.getIdString());
                buttonCacheRepository.save(button26_0_0);
                row26_0.add(button26_0_0);
                level_ACTION_TYPE.addRow(row26_0);

                ButtonRow row26_1 = new ButtonRow(level_ACTION_TYPE);
                buttonRowRepository.save(row26_1);
                Button button26_1_0 = new Button(row26_1, Map.of(RU, ActionTypeEnum.COUPON_DEFAULT.getValue()), level_COUPON.getIdString());
                buttonCacheRepository.save(button26_1_0);
                row26_1.add(button26_1_0);
                level_ACTION_TYPE.addRow(row26_1);

                ButtonRow row26_2 = new ButtonRow(level_ACTION_TYPE);
                buttonRowRepository.save(row26_2);
                Button button26_2_0 = new Button(row26_2, Map.of(RU, ActionTypeEnum.LINK_TO_PRODUCT.getValue()), level_LINK_TO_PRODUCT.getIdString());
                buttonCacheRepository.save(button26_2_0);
                row26_2.add(button26_2_0);
                level_ACTION_TYPE.addRow(row26_2);

                ///BASIC
// проверил отсюда 12.05
                level_BASIC.updateLevel(Users, BASIC.name(), level_ACTION_TYPE, false);

                levelCacheRepository.save(level_BASIC);
                Message message27_0 = new Message(level_BASIC, Map.of(RU, "Введите название"));
                messageCacheRepository.save(message27_0);
                level_BASIC.addMessage(message27_0);


                level_SELECT_LEVEL_TYPE.updateLevel(Users, SELECT_LEVEL_TYPE.name(), level_BASIC, true);

                levelCacheRepository.save(level_SELECT_LEVEL_TYPE);
                Message message31_0 = new Message(level_SELECT_LEVEL_TYPE, Map.of(RU, "Использовать разные размеры скидки для различных уровней суммы покупок за прошлый месяц?"));
                messageCacheRepository.save(message31_0);
                level_SELECT_LEVEL_TYPE.addMessage(message31_0);
                ButtonRow row31_0 = new ButtonRow(level_SELECT_LEVEL_TYPE);
                buttonRowRepository.save(row31_0);
                Button button31_0_0 = new Button(row31_0, Map.of(RU, "Нет"), level_ONE_LEVEL_RATE.getIdString());
                buttonCacheRepository.save(button31_0_0);
                row31_0.add(button31_0_0);
                level_SELECT_LEVEL_TYPE.addRow(row31_0);
                ButtonRow row31_1 = new ButtonRow(level_SELECT_LEVEL_TYPE);
                buttonRowRepository.save(row31_1);
                Button button31_1_0 = new Button(row31_0, Map.of(RU, "Да, добавить уровень"), level_MULTI_ACTION_LEVEL.getIdString());
                buttonCacheRepository.save(button31_1_0);
                row31_1.add(button31_1_0);
                level_SELECT_LEVEL_TYPE.addRow(row31_1);

////////////////////////////////////////////////
                level_MULTI_ACTION_LEVEL.updateLevel(Users, MULTI_ACTION_LEVEL.name(), level_SELECT_LEVEL_TYPE, true);

                levelCacheRepository.save(level_MULTI_ACTION_LEVEL);
                Message message32_0 = new Message(level_MULTI_ACTION_LEVEL, Map.of(RU, "Введите размер уровня суммы"));
                messageCacheRepository.save(message32_0);
                level_MULTI_ACTION_LEVEL.addMessage(message32_0);

// используется как для перехода после кнопки ONE_ACTION_LEVEL, так и после отправки сообщения от MULTI_ACTION_LEVEL
                level_MULTI_LEVEL_RATE.updateLevel(Users, MULTI_LEVEL_RATE.name(), level_MULTI_ACTION_LEVEL, true);

                levelCacheRepository.save(level_MULTI_LEVEL_RATE);
                Message message28_0 = new Message(level_MULTI_LEVEL_RATE, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
                messageCacheRepository.save(message28_0);
                level_MULTI_LEVEL_RATE.addMessage(message28_0);
/////////////////////////////////////////////////
// Дублируется для зацикливания

                level_MULTI_LEVEL_QUESTION.updateLevel(Users, MULTI_LEVEL_QUESTION.name(), level_MULTI_LEVEL_RATE, true);

                levelCacheRepository.save(level_MULTI_LEVEL_QUESTION);
                Message message31_2_0 = new Message(level_MULTI_LEVEL_QUESTION, Map.of(RU, "Добавить уровень суммы покупок за прошлый месяц?"));
                messageCacheRepository.save(message31_2_0);
                level_MULTI_LEVEL_QUESTION.addMessage(message31_2_0);
                ButtonRow row31_2_0 = new ButtonRow(level_MULTI_LEVEL_QUESTION);
                buttonRowRepository.save(row31_2_0);
                Button button31_2_0_0 = new Button(row31_2_0, Map.of(RU, "Нет"), level_ACTION_RATE_WITHDRAW.getIdString());
                buttonCacheRepository.save(button31_2_0_0);
                row31_2_0.add(button31_2_0_0);
                level_MULTI_LEVEL_QUESTION.addRow(row31_2_0);
                ButtonRow row31_2_1 = new ButtonRow(level_MULTI_LEVEL_QUESTION);
                buttonRowRepository.save(row31_2_1);
                Button button31_2_1_0 = new Button(row31_2_0, Map.of(RU, "Да"), level_MULTI_ACTION_LEVEL.getIdString());
                buttonCacheRepository.save(button31_2_1_0);
                row31_2_1.add(button31_2_1_0);
                level_MULTI_LEVEL_QUESTION.addRow(row31_2_1);
// проверил досюда 12.05
///////////////////////////////////////////////////////////////////////////
                level_ONE_LEVEL_RATE.updateLevel(Users, ONE_LEVEL_RATE.name(), level_SELECT_LEVEL_TYPE, true);

                levelCacheRepository.save(level_ONE_LEVEL_RATE);
                Message message28_2_0 = new Message(level_ONE_LEVEL_RATE, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
                messageCacheRepository.save(message28_2_0);
                level_ONE_LEVEL_RATE.addMessage(message28_2_0);

                level_ACTION_RATE_WITHDRAW.updateLevel(Users, ACTION_RATE_WITHDRAW.name(), level_ONE_LEVEL_RATE, true);

                levelCacheRepository.save(level_ACTION_RATE_WITHDRAW);
                Message message29_0 = new Message(level_ACTION_RATE_WITHDRAW, Map.of(RU, "Введите в % максимальную долю списания кэшбека в стоимости последуюшей покупке"));
                messageCacheRepository.save(message29_0);
                level_ACTION_RATE_WITHDRAW.addMessage(message29_0);


                level_ACTION_RATE_PARTNER.updateLevel(Users, ACTION_RATE_PARTNER.name(), level_ACTION_RATE_WITHDRAW, true);

                levelCacheRepository.save(level_ACTION_RATE_PARTNER);
                Message message30_0 = new Message(level_ACTION_RATE_PARTNER, Map.of(RU, "Введите в % размер кешбека для партнера"));
                messageCacheRepository.save(message30_0);
                level_ACTION_RATE_PARTNER.addMessage(message30_0);

                level_ADD_ACTION_RATE_SOURCE.updateLevel(Users, ADD_ACTION_RATE_SOURCE.name(), level_ACTION_RATE_PARTNER, true);

                levelCacheRepository.save(level_ADD_ACTION_RATE_SOURCE);
                Message message38_0 = new Message(level_ADD_ACTION_RATE_SOURCE, Map.of(RU, "Выберите категорию либо товар для акции"));
                messageCacheRepository.save(message38_0);
                level_ADD_ACTION_RATE_SOURCE.addMessage(message38_0);

                menuCreator.createMenu(level_ADD_ACTION_RATE_SOURCE, MenuTypeEnum.LEVEL_CHOICER, Users);


                level_ADD_ACTION_RATE_TARGET.updateLevel(Users, ADD_ACTION_RATE_TARGET.name(), level_ADD_ACTION_RATE_SOURCE, true);

                levelCacheRepository.save(level_ADD_ACTION_RATE_TARGET);
                Message message39_0 = new Message(level_ADD_ACTION_RATE_TARGET, Map.of(RU, "Выберите категорию либо товар, на который можно тратить кешбек"));
                messageCacheRepository.save(message39_0);
                level_ADD_ACTION_RATE_TARGET.addMessage(message39_0);

                menuCreator.createMenu(level_ADD_ACTION_RATE_TARGET, MenuTypeEnum.LEVEL_CHOICER, Users);

                ////COUPON

                level_COUPON.updateLevel(Users, COUPON.name(), level_ACTION_TYPE, false);

                levelCacheRepository.save(level_COUPON);
                Message message33_0 = new Message(level_COUPON, Map.of(RU, "Введите название"));
                messageCacheRepository.save(message33_0);
                level_COUPON.addMessage(message33_0);


                level_COUPON_NUMBER.updateLevel(Users, COUPON_NUMBER.name(), level_COUPON, true);

                levelCacheRepository.save(level_COUPON_NUMBER);
                Message message34_0 = new Message(level_COUPON_NUMBER, Map.of(RU, "Введите количество товара, после покупки которого начнет действовать скидка"));
                messageCacheRepository.save(message34_0);
                level_COUPON_NUMBER.addMessage(message34_0);


                level_COUPON_RATE_WITHDRAW.updateLevel(Users, COUPON_RATE_WITHDRAW.name(), level_COUPON_NUMBER, true);

                levelCacheRepository.save(level_COUPON_RATE_WITHDRAW);
                Message message35_0 = new Message(level_COUPON_RATE_WITHDRAW, Map.of(RU, "Введите в % размер скидки, например 100"));
                messageCacheRepository.save(message35_0);
                level_COUPON_RATE_WITHDRAW.addMessage(message35_0);


                level_ADD_ACTION_COUPON_SOURCE.updateLevel(Users, ADD_ACTION_COUPON_SOURCE.name(), level_COUPON_RATE_WITHDRAW, true);

                levelCacheRepository.save(level_ADD_ACTION_COUPON_SOURCE);
                Message message40_0 = new Message(level_ADD_ACTION_COUPON_SOURCE, Map.of(RU, "Выберите категорию либо товар для акции"));
                messageCacheRepository.save(message40_0);
                level_ADD_ACTION_COUPON_SOURCE.addMessage(message40_0);


                menuCreator.createMenu(level_ADD_ACTION_COUPON_SOURCE, MenuTypeEnum.LEVEL_CHOICER, Users);

                level_ADD_ACTION_COUPON_TARGET.updateLevel(Users, ADD_ACTION_COUPON_TARGET.name(), level_ADD_ACTION_COUPON_SOURCE, true);

                levelCacheRepository.save(level_ADD_ACTION_COUPON_TARGET);
                Message message41_0 = new Message(level_ADD_ACTION_COUPON_TARGET, Map.of(RU, "Выберите категорию либо товар, который можно получить по купону"));
                messageCacheRepository.save(message41_0);
                level_ADD_ACTION_COUPON_TARGET.addMessage(message41_0);

                menuCreator.createMenu(level_ADD_ACTION_COUPON_TARGET, MenuTypeEnum.LEVEL_CHOICER, Users);


                ////LINK_TO_PRODUCT

                level_LINK_TO_PRODUCT.updateLevel(Users, LINK_TO_PRODUCT.name(), level_ACTION_TYPE, true);

                levelCacheRepository.save(level_LINK_TO_PRODUCT);
                Message message36_0 = new Message(level_LINK_TO_PRODUCT, Map.of(RU, "Введите название"));
                messageCacheRepository.save(message36_0);
                level_LINK_TO_PRODUCT.addMessage(message36_0);


                level_LINK_TO_PRODUCT_NUMBER.updateLevel(Users, LINK_TO_PRODUCT_NUMBER.name(), level_LINK_TO_PRODUCT, true);

                levelCacheRepository.save(level_LINK_TO_PRODUCT_NUMBER);
                Message message37_0 = new Message(level_LINK_TO_PRODUCT_NUMBER, Map.of(RU, "Введите количество товара, после покупки которого начнет действовать скидка"));
                messageCacheRepository.save(message37_0);
                level_LINK_TO_PRODUCT_NUMBER.addMessage(message37_0);


                level_ADD_ACTION_LINK_SOURCE.updateLevel(Users, ADD_ACTION_LINK_SOURCE.name(), level_LINK_TO_PRODUCT_NUMBER, true);

                levelCacheRepository.save(level_ADD_ACTION_LINK_SOURCE);
                Message message42_0 = new Message(level_ADD_ACTION_LINK_SOURCE, Map.of(RU, "Выберите категорию либо товар - главный"));
                messageCacheRepository.save(message42_0);
                level_ADD_ACTION_LINK_SOURCE.addMessage(message42_0);

                menuCreator.createMenu(level_ADD_ACTION_LINK_SOURCE, MenuTypeEnum.LEVEL_CHOICER, Users);

                level_ADD_ACTION_LINK_TARGET.updateLevel(Users, ADD_ACTION_LINK_TARGET.name(), level_ADD_ACTION_LINK_SOURCE, true);

                levelCacheRepository.save(level_ADD_ACTION_LINK_TARGET);
                Message message43_0 = new Message(level_ADD_ACTION_LINK_TARGET, Map.of(RU, "Выберите категорию либо товар, который можно получить по скидке в паре"));
                messageCacheRepository.save(message43_0);
                level_ADD_ACTION_LINK_TARGET.addMessage(message43_0);

                menuCreator.createMenu(level_ADD_ACTION_LINK_TARGET, MenuTypeEnum.LEVEL_CHOICER, Users);

                //////////МАГАЗИНЫ ДЛЯ ПРОДАВЦА

                level_ADMIN_SHOPS.updateLevel(Users, ADMIN_SHOPS.name(), level_ADMIN_ADMIN, false);

                levelCacheRepository.save(level_ADMIN_SHOPS);
                Message message01_1 = new Message(level_ADMIN_SHOPS, Map.of(RU, "Список моих магазинов"));
                messageCacheRepository.save(message01_1);
                level_ADMIN_SHOPS.addMessage(message01_1);

                ///////////////////////// СПИСОК ТОВАРОВ

                level_GOODS_LIST.updateLevel(Users, GOODS_LIST.name(), level_ADMIN_ADMIN, false);

                levelCacheRepository.save(level_GOODS_LIST);
                Message message52_1 = new Message(level_GOODS_LIST, Map.of(RU, "Список моих товаров"));
                messageCacheRepository.save(message52_1);
                level_GOODS_LIST.addMessage(message52_1);
                Message message52_2_1 = new Message(level_GOODS_LIST, 0,
                        Map.of(RU, "Вы можете загрузить список товаров файл Excel, " +
                                "для этого скачайте по ссылке http://skidozona.by, " +
                                "отредактируйте и загрузите на сервер, прикрепив документ"));
                messageCacheRepository.save(message52_2_1);
                level_GOODS_LIST.addMessage(message52_2_1);
                ButtonRow row52_0 = new ButtonRow(level_GOODS_LIST);
                buttonRowRepository.save(row52_0);
                Button button52_0_0 = new Button(row52_0, Map.of(RU, "Добавить товары через бот"), level_ADD_GOODS.getIdString());
                buttonCacheRepository.save(button52_0_0);
                row52_0.add(button52_0_0);
                level_GOODS_LIST.addRow(row52_0);

                /////menuCreator.createMenu(level_GOODS_LIST, MenuTypeEnum.SEARCH);


                level_SHOP_BOTS.updateLevel(Users, SHOP_BOTS.name(), level_ADMIN_ADMIN, false);

                levelCacheRepository.save(level_SHOP_BOTS);
                Message message52_12 = new Message(level_SHOP_BOTS, Map.of(RU, "Мои боты: "));
                messageCacheRepository.save(message52_12);
                level_SHOP_BOTS.addMessage(message52_12);


                //ЗДЕСЬ ВЫХОДИТ СПИСОК БОТОВ
                level0_1_1.updateLevel(Users, "level0_1_1", level_INITIALIZE, false);
//                level.addMessage(message);
//                ButtonRow row = new ArrayList<>();
//                Button button = new Button(level, "Ответить " + buyerChat.getOwner().getBuyer().getName(), RESPONSE_BUYER_MESSAGE.name() + buyerChatId);
//                row.add(button);
//                level.addRow(row);

                level0_1_3.updateLevel(Users, "level0_1_3", level_INITIALIZE, false);//true
                level0_1_3.addMessage(new Message(null, Map.of(RU, "Отправлено!")));

                level_RESPONSE_BUYER_MESSAGE.updateLevel(Users, RESPONSE_BUYER_MESSAGE.name(), level_INITIALIZE, false);
                levelCacheRepository.save(level_RESPONSE_BUYER_MESSAGE);

                level_RESPONSE_SHOP_MESSAGE.updateLevel(Users, RESPONSE_SHOP_MESSAGE.name(), level_INITIALIZE, false);
                levelCacheRepository.save(level_RESPONSE_SHOP_MESSAGE);

                level_NON_RESPONSE.updateLevel(Users, NON_RESPONSE.name(), level_INITIALIZE, false);
                levelCacheRepository.save(level_NON_RESPONSE);

                level0_1_4.updateLevel(Users, SEND_SHOP_MESSAGE.name(), level0_1_4, true);
                levelCacheRepository.save(level0_1_4);

                level0_1_5.updateLevel(Users, SEND_BUYER_MESSAGE.name(), level0_1_5, true);
                levelCacheRepository.save(level0_1_5);




                //////////Монитор

                level_MONITOR.updateLevel(Users, MONITOR.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_MONITOR);
                Message message123_10 = new Message(level_MONITOR, Map.of(RU, "Пришлите ссылку из карточки товара на Wildberries, Ozon, LaModa, 21 Век"));
                messageCacheRepository.save(message123_10);
                level_MONITOR.addMessage(message123_10);


                level_MONITOR_PRICE.updateLevel(Users, MONITOR_PRICE.name(), level_MONITOR, true);

                levelCacheRepository.save(level_MONITOR_PRICE);
                Message message123_11 = new Message(level_MONITOR_PRICE, Map.of(RU, "Товар успешно найден. Текущая цена ЦЦЦ."));
                messageCacheRepository.save(message123_11);
                level_MONITOR_PRICE.addMessage(message123_11);
                Message message123_12 = new Message(level_MONITOR_PRICE, Map.of(RU, "Пришлите целевую цену, при достижении которой будет отправлено уведомление"));
                messageCacheRepository.save(message123_12);
                level_MONITOR_PRICE.addMessage(message123_12);


                level_MONITOR_RESP.updateLevel(Users, MONITOR_RESP.name(), level_MONITOR_PRICE, true);

                levelCacheRepository.save(level_MONITOR_RESP);//Цена на ТТТ снизилась до ППП
                Message message123_13 = new Message(level_MONITOR_RESP, Map.of(RU, "Закладка сохранена!"));
                messageCacheRepository.save(message123_13);
                level_MONITOR_RESP.addMessage(message123_13);


                //////////КЭШБЕКИ

                level_CASHBACKS.updateLevel(Users, CASHBACKS.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_CASHBACKS);
                Message message1_1 = new Message(level_CASHBACKS, Map.of(RU, "Список активных кэшбеков:"));
                messageCacheRepository.save(message1_1);
                level_CASHBACKS.addMessage(message1_1);
                ButtonRow row01_0 = new ButtonRow(level_CASHBACKS);
                buttonRowRepository.save(row01_0);
                Button button01_0_0 = new Button(row01_0, Map.of(RU, "Рекомендовать/ запрос кэшбека"), level_CONNECT.getIdString());
                buttonCacheRepository.save(button01_0_0);
                row01_0.add(button01_0_0);

                level_CASHBACKS.addRow(row01_0);


                //////////СВЯЗАТЬ

                level_CONNECT.updateLevel(Users, CONNECT.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_CONNECT);
                Message message2_1 = new Message(level_CONNECT, 0, Map.of(RU, "ссылка"), IOUtils.toByteArray(qrInputStream("ссылка")), "ссылка");
                messageCacheRepository.save(message2_1);
                level_CONNECT.addMessage(message2_1);
                Message message2_2 = new Message(level_CONNECT, Map.of(RU, "Или перешлите ссылку:"));
                messageCacheRepository.save(message2_2);
                level_CONNECT.addMessage(message2_2);
                Message message2_3 = new Message(level_CONNECT, Map.of(RU, "https://t.me/Skido_Bot?start=userId"));
                messageCacheRepository.save(message2_3);
                level_CONNECT.addMessage(message2_3);


                //////////СВЯЗАТЬ С МАГАЗИНОМ
                level_CONNECT_SHOP.updateLevel(Users, CONNECT_SHOP.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_CONNECT_SHOP);

                //////////ЗАКЛАДКИ

                level_BOOKMARKS.updateLevel(Users, BOOKMARKS.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_BOOKMARKS);
                Message message3_1 = new Message(level_BOOKMARKS, Map.of(RU, "Список закладок:"));
                messageCacheRepository.save(message3_1);
                level_BOOKMARKS.addMessage(message3_1);
                ButtonRow row3_0 = new ButtonRow(level_BOOKMARKS);
                buttonRowRepository.save(row3_0);
                Button button3_0_0 = new Button(row3_0, Map.of(RU, "Показать магазину"), level_CONNECT.getIdString());
                buttonCacheRepository.save(button3_0_0);
                row3_0.add(button3_0_0);
                level_BOOKMARKS.addRow(row3_0);


                level_ADD_BOOKMARK.updateLevel(Users, ADD_BOOKMARK.name(), level_SEARCH, false);

                levelCacheRepository.save(level_ADD_BOOKMARK);
                Message message3_1_1 = new Message(level_ADD_BOOKMARK, Map.of(RU, "Закладка на товар добавлена!"));
                messageCacheRepository.save(message3_1_1);
                level_ADD_BOOKMARK.addMessage(message3_1_1);
                ButtonRow row3_1 = new ButtonRow(level_ADD_BOOKMARK);
                buttonRowRepository.save(row3_1);
                Button button3_1_1 = new Button(row3_1, Map.of(RU, "К поиску"), level_SEARCH.getIdString());
                buttonCacheRepository.save(button3_1_1);
                row3_1.add(button3_1_1);
                level_ADD_BOOKMARK.addRow(row3_1);


                //////////КОРЗИНА

                level_BASKET.updateLevel(Users, BASKET.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_BASKET);
                Message message4_1 = new Message(level_BASKET, Map.of(RU, "Товары в корзине:"));
                messageCacheRepository.save(message4_1);
                level_BASKET.addMessage(message4_1);
                ButtonRow row4_0 = new ButtonRow(level_BASKET);
                buttonRowRepository.save(row4_0);
                Button button4_0_0 = new Button(row4_0, Map.of(RU, "Показать магазину"), level_CONNECT.getIdString());
                buttonCacheRepository.save(button4_0_0);
                row4_0.add(button4_0_0);
                level_BASKET.addRow(row4_0);


                ////////// АРХИВ КОРЗИН

                level_BASKET_ARCHIVE.updateLevel(Users, BASKET_ARCHIVE.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_BASKET_ARCHIVE);
                Message message400_1 = new Message(level_BASKET_ARCHIVE, Map.of(RU, "Архив корзин:"));
                messageCacheRepository.save(message400_1);
                level_BASKET_ARCHIVE.addMessage(message400_1);
                ButtonRow row400_0 = new ButtonRow(level_BASKET_ARCHIVE);
                buttonRowRepository.save(row400_0);
                Button button400_0_0 = new Button(row400_0, Map.of(RU, "Показать магазину"), level_CONNECT.getIdString());
                buttonCacheRepository.save(button400_0_0);
                row400_0.add(button400_0_0);
                level_BASKET_ARCHIVE.addRow(row400_0);




                level_ADD_BASKET.updateLevel(Users, ADD_BASKET.name(), level_SEARCH, false);

                levelCacheRepository.save(level_ADD_BASKET);
                Message message4_1_1 = new Message(level_ADD_BASKET, Map.of(RU, "Товар добавлен в корзину!"));
                messageCacheRepository.save(message4_1_1);
                level_ADD_BASKET.addMessage(message4_1_1);
                ButtonRow row4_1 = new ButtonRow(level_ADD_BASKET);
                buttonRowRepository.save(row4_1);
                Button button4_1_1 = new Button(row4_1, Map.of(RU, "К поиску"), level_SEARCH.getIdString());
                buttonCacheRepository.save(button4_1_1);
                row4_1.add(button4_1_1);
                level_ADD_BASKET.addRow(row4_1);


                //////////МАГАЗИНЫ ДЛЯ ПОКУПАТЕЛЯ

                level_MY_SHOPS.updateLevel(Users, MY_SHOPS.name(), level_SEARCH_RESULT_PRODUCT, true);

                levelCacheRepository.save(level_MY_SHOPS);
                Message message001_1 = new Message(level_MY_SHOPS, Map.of(RU, "Список моих магазинов"));
                messageCacheRepository.save(message001_1);
                level_MY_SHOPS.addMessage(message001_1);
                ButtonRow row001_0 = new ButtonRow(level_MY_SHOPS);
                buttonRowRepository.save(row001_0);
                Button button001_0_0 = new Button(row001_0, Map.of(RU, "Рекомендовать/ запрос кэшбека"), level_CONNECT.getIdString());
                buttonCacheRepository.save(button001_0_0);
                row001_0.add(button001_0_0);
//                Button button001_0_1 = new Button(level_CONSTRUCT_ADD, "Мои боты", level_SHOP_BOTS.getIdString());
//                buttonRepository.save(button001_0_1);
//                row001_0.add(button001_0_1);
                level_MY_SHOPS.addRow(row001_0);


                //////////ПОИСК ТОВАРА ПО КАТЕГОРИЯМ

                level_SEARCH.updateLevel(Users, SEARCH.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_SEARCH);
                Message message15_1 = new Message(level_SEARCH, Map.of(RU, "Введите название для поиска:"));
                messageCacheRepository.save(message15_1);
                level_SEARCH.addMessage(message15_1);

                menuCreator.createMenu(level_SEARCH, MenuTypeEnum.SEARCH_LEVEL_CHOICER, Users);

                level_SEARCH_RESULT.updateLevel(Users, SEARCH_RESULT.name(), level_SEARCH, false);

                levelCacheRepository.save(level_SEARCH_RESULT);
                Message message52_1_1 = new Message(level_SEARCH_RESULT, Map.of(RU, "Результат поиска"));
                messageCacheRepository.save(message52_1_1);
                level_SEARCH_RESULT.addMessage(message52_1_1);


                level_SEARCH_RESULT_PRODUCT.updateLevel(Users, SEARCH_RESULT_PRODUCT.name(), level_SEARCH_RESULT, false);

                levelCacheRepository.save(level_SEARCH_RESULT_PRODUCT);
                Message message521_1_1 = new Message(level_SEARCH_RESULT_PRODUCT, Map.of(RU, "Подробнее: "));
                messageCacheRepository.save(message521_1_1);
                level_SEARCH_RESULT_PRODUCT.addMessage(message521_1_1);

                //////////СОЗДАНИЕ МАГАЗИНА С ТОВАРАМИ

                level_CONSTRUCT.updateLevel(Users, CONSTRUCT.name(), level_INITIALIZE, false);
                levelCacheRepository.save(level_CONSTRUCT);
                Message message6_1 = new Message(level_CONSTRUCT, Map.of(RU, "Введите название магазина/сервиса"));
                messageCacheRepository.save(message6_1);
                level_CONSTRUCT.addMessage(message6_1);


//                level_CONSTRUCT_MIN_BILL_SHARE.updateLevel(Users, CONSTRUCT_MIN_BILL_SHARE.name(), level_CONSTRUCT, true);
//                levelCacheRepository.save(level_CONSTRUCT_MIN_BILL_SHARE);
//                Message message6_0_1 = new Message(level_CONSTRUCT_MIN_BILL_SHARE, Map.of(LanguageEnum.ru, "Введите размер минимальной скидки по чеку, %"));
//                messageCacheRepository.save(message6_0_1);
//                level_CONSTRUCT_MIN_BILL_SHARE.addMessage(message6_0_1);



                level_CONSTRUCT_MIN_BILL_SHARE.updateLevel(Users, CONSTRUCT_MIN_BILL_SHARE.name(), level_CONSTRUCT, true);

                levelCacheRepository.save(level_CONSTRUCT_MIN_BILL_SHARE);
                Message message3100_0 = new Message(level_CONSTRUCT_MIN_BILL_SHARE, Map.of(RU, "Использовать разные размеры скидки для различных уровней суммы покупок за прошлый месяц?"));
                messageCacheRepository.save(message3100_0);
                level_CONSTRUCT_MIN_BILL_SHARE.addMessage(message3100_0);
                ButtonRow row3100_0 = new ButtonRow(level_CONSTRUCT_MIN_BILL_SHARE);
                buttonRowRepository.save(row3100_0);
                Button button3100_0_0 = new Button(row3100_0, Map.of(RU, "Нет"), level_ONE_LEVEL_RATE_BASIC.getIdString());
                buttonCacheRepository.save(button3100_0_0);
                row3100_0.add(button3100_0_0);
                level_CONSTRUCT_MIN_BILL_SHARE.addRow(row3100_0);
                ButtonRow row3100_1 = new ButtonRow(level_CONSTRUCT_MIN_BILL_SHARE);
                buttonRowRepository.save(row3100_1);
                Button button3100_1_0 = new Button(row3100_0, Map.of(RU, "Да, добавить уровень"), level_MULTI_ACTION_LEVEL_BASIC.getIdString());
                buttonCacheRepository.save(button3100_1_0);
                row3100_1.add(button3100_1_0);
                level_CONSTRUCT_MIN_BILL_SHARE.addRow(row3100_1);

                level_MULTI_ACTION_LEVEL_BASIC.updateLevel(Users, MULTI_ACTION_LEVEL_BASIC.name(), level_CONSTRUCT_MIN_BILL_SHARE, true);

                levelCacheRepository.save(level_MULTI_ACTION_LEVEL_BASIC);
                Message message3200_0 = new Message(level_MULTI_ACTION_LEVEL_BASIC, Map.of(RU, "Введите размер уровня суммы"));
                messageCacheRepository.save(message3200_0);
                level_MULTI_ACTION_LEVEL_BASIC.addMessage(message3200_0);

                level_MULTI_LEVEL_RATE_BASIC.updateLevel(Users, MULTI_LEVEL_RATE_BASIC.name(), level_MULTI_ACTION_LEVEL_BASIC, true);

                levelCacheRepository.save(level_MULTI_LEVEL_RATE_BASIC);
                Message message2800_0 = new Message(level_MULTI_LEVEL_RATE_BASIC, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
                messageCacheRepository.save(message2800_0);
                level_MULTI_LEVEL_RATE_BASIC.addMessage(message2800_0);

                level_MULTI_LEVEL_QUESTION_BASIC.updateLevel(Users, MULTI_LEVEL_QUESTION_BASIC.name(), level_MULTI_LEVEL_RATE_BASIC, true);

                levelCacheRepository.save(level_MULTI_LEVEL_QUESTION_BASIC);
                Message message3100_2_0 = new Message(level_MULTI_LEVEL_QUESTION_BASIC, Map.of(RU, "Добавить уровень суммы покупок за прошлый месяц?"));
                messageCacheRepository.save(message3100_2_0);
                level_MULTI_LEVEL_QUESTION_BASIC.addMessage(message3100_2_0);
                ButtonRow row3100_2_0 = new ButtonRow(level_MULTI_LEVEL_QUESTION_BASIC);
                buttonRowRepository.save(row3100_2_0);
                Button button3100_2_0_0 = new Button(row3100_2_0, Map.of(RU, "Нет"), level_ACTION_RATE_WITHDRAW_BASIC.getIdString());
                buttonCacheRepository.save(button3100_2_0_0);
                row3100_2_0.add(button3100_2_0_0);
                level_MULTI_LEVEL_QUESTION_BASIC.addRow(row3100_2_0);
                ButtonRow row3100_2_1 = new ButtonRow(level_MULTI_LEVEL_QUESTION_BASIC);
                buttonRowRepository.save(row3100_2_1);
                Button button31_200_1_0 = new Button(row3100_2_0, Map.of(RU, "Да"), level_MULTI_ACTION_LEVEL_BASIC.getIdString());
                buttonCacheRepository.save(button31_200_1_0);
                row3100_2_1.add(button31_200_1_0);
                level_MULTI_LEVEL_QUESTION_BASIC.addRow(row3100_2_1);

                level_ONE_LEVEL_RATE_BASIC.updateLevel(Users, ONE_LEVEL_RATE_BASIC.name(), level_CONSTRUCT_MIN_BILL_SHARE, true);

                levelCacheRepository.save(level_ONE_LEVEL_RATE_BASIC);
                Message message2800_2_0 = new Message(level_ONE_LEVEL_RATE_BASIC, Map.of(RU, "Введите в % размер начисляемого кэшбека"));
                messageCacheRepository.save(message2800_2_0);
                level_ONE_LEVEL_RATE_BASIC.addMessage(message2800_2_0);

                level_ACTION_RATE_WITHDRAW_BASIC.updateLevel(Users, ACTION_RATE_WITHDRAW_BASIC.name(), level_ONE_LEVEL_RATE_BASIC, true);

                levelCacheRepository.save(level_ACTION_RATE_WITHDRAW_BASIC);
                Message message2900_0 = new Message(level_ACTION_RATE_WITHDRAW_BASIC, Map.of(RU, "Введите в % максимальную долю списания кэшбека в стоимости последуюшей покупке"));
                messageCacheRepository.save(message2900_0);
                level_ACTION_RATE_WITHDRAW_BASIC.addMessage(message2900_0);


                level_CONSTRUCT_SARAFAN_SHARE.updateLevel(Users, CONSTRUCT_SARAFAN_SHARE.name(), level_ACTION_RATE_WITHDRAW_BASIC, true);
                levelCacheRepository.save(level_CONSTRUCT_SARAFAN_SHARE);
                Message message6_1_1 = new Message(level_CONSTRUCT_SARAFAN_SHARE, Map.of(RU, "Введите размер скидки клиенту, который посоветовал другу Ваш магазин, %"));
                messageCacheRepository.save(message6_1_1);
                level_CONSTRUCT_SARAFAN_SHARE.addMessage(message6_1_1);

                level_CONSTRUCT_ADD.updateLevel(Users, CONSTRUCT_ADD.name(), level_CONSTRUCT_SARAFAN_SHARE, true);
                levelCacheRepository.save(level_CONSTRUCT_ADD);
                Message message6_2_1 = new Message(level_CONSTRUCT_ADD, 0,
                        Map.of(RU, "Вы можете загрузить товары файлом Excel, " +
                                "для этого скачайте по ссылке http://skidozona.by, " +
                                "отредактируйте и загрузите на сервер, прикрепив документ"));
                messageCacheRepository.save(message6_2_1);
                level_CONSTRUCT_ADD.addMessage(message6_2_1);
                ButtonRow row5_0 = new ButtonRow(level_CONSTRUCT_ADD);
                buttonRowRepository.save(row5_0);
                Button button5_0_0 = new Button(row5_0, Map.of(RU, "Добавить товары через бот"), level_ADD_GOODS.getIdString());
                buttonCacheRepository.save(button5_0_0);
                row5_0.add(button5_0_0);
                Button button5_0_1 = new Button(row5_0, Map.of(RU, "Добавить бот"), level_ADD_BOT.getIdString());
                buttonCacheRepository.save(button5_0_1);
                row5_0.add(button5_0_1);
                level_CONSTRUCT_ADD.addRow(row5_0);


                //////////СОЗДАНИЕ ВВОДА ТОВАРОВ
// проверил 12.05 отсюда
                level_ADD_GOODS.updateLevel(Users, ADD_GOODS.name(), level_CONSTRUCT_ADD, false);

                levelCacheRepository.save(level_ADD_GOODS);
                Message message7_0 = new Message(level_ADD_GOODS, 0,
                        Map.of(RU,
                                "Вы можете добавлять товары, просто загрузив в бот файлом Excel http://skidozona.by, а также" +
                                        " в панели управления на сайте, перейдя по этой ссылке,"));
                messageCacheRepository.save(message7_0);
                level_ADD_GOODS.addMessage(message7_0);

                Message message7_3 = new Message(level_ADD_GOODS, 1, Map.of(RU, "https://skidozona.by/admin/id=22222"));
                messageCacheRepository.save(message7_3);
                level_ADD_GOODS.addMessage(message7_3);

                ButtonRow row7_0 = new ButtonRow(level_ADD_GOODS);
                buttonRowRepository.save(row7_0);

                level_ADD_GOODS.addRow(row7_0);
                Button button7_0_2 = new Button(row7_0, Map.of(RU, "Добавить ещё"), level_GOODS_LIST.getIdString());
                buttonCacheRepository.save(button7_0_2);
                row7_0.add(button7_0_2);

                menuCreator.createMenu(level_ADD_GOODS, MenuTypeEnum.ADD_GOODS, Users);

                level_ADD_GOODS_NAME.updateLevel(Users, ADD_GOODS_NAME.name(), level_ADD_GOODS, false);

                levelCacheRepository.save(level_ADD_GOODS_NAME);
                Message message14_2 = new Message(level_ADD_GOODS_NAME, Map.of(RU, "Введите название"));
                messageCacheRepository.save(message14_2);
                level_ADD_GOODS_NAME.addMessage(message14_2);

                level_ADD_GOODS_PHOTO.updateLevel(Users, ADD_GOODS_PHOTO.name(), level_ADD_GOODS_NAME, true);

                levelCacheRepository.save(level_ADD_GOODS_PHOTO);
                Message message8_1 = new Message(level_ADD_GOODS_PHOTO, Map.of(RU, "Пришлите фото"));
                messageCacheRepository.save(message8_1);
                level_ADD_GOODS_PHOTO.addMessage(message8_1);

                level_ADD_GOODS_DESCRIPTION.updateLevel(Users, ADD_GOODS_DESCRIPTION.name(), level_ADD_GOODS_PHOTO, true);

                levelCacheRepository.save(level_ADD_GOODS_DESCRIPTION);
                Message message9_1 = new Message(level_ADD_GOODS_DESCRIPTION, Map.of(RU, "Пришлите описание"));
                messageCacheRepository.save(message9_1);
                level_ADD_GOODS_DESCRIPTION.addMessage(message9_1);

                level_ADD_GOODS_PRICE.updateLevel(Users, ADD_GOODS_PRICE.name(), level_ADD_GOODS_DESCRIPTION, true);

                levelCacheRepository.save(level_ADD_GOODS_PRICE);
                Message message10_1 = new Message(level_ADD_GOODS_PRICE, Map.of(RU, "Пришлите цену"));
                messageCacheRepository.save(message10_1);
                level_ADD_GOODS_PRICE.addMessage(message10_1);


                level_ADD_GOODS_END.updateLevel(Users, ADD_GOODS_END.name(), level_ADD_GOODS_PRICE, true);

                levelCacheRepository.save(level_ADD_GOODS_END);
                Message message010_1 = new Message(level_ADD_GOODS_END, Map.of(RU, "Сохранено"));
                messageCacheRepository.save(message010_1);
                level_ADD_GOODS_END.addMessage(message010_1);
// проверил досюда 12.05
                //////////ДОБАВИТЬ БОТ

                level_ADD_BOT.updateLevel(Users, ADD_BOT.name(), level_CONSTRUCT_ADD, false);

                levelCacheRepository.save(level_ADD_BOT);
                Message message16_1 = new Message(level_ADD_BOT, Map.of(RU, "Выберите шаблон бота:"));
                messageCacheRepository.save(message16_1);
                level_ADD_BOT.addMessage(message16_1);
                ButtonRow row16_0 = new ButtonRow(level_ADD_BOT);
                buttonRowRepository.save(row16_0);
                level_ADD_BOT.addRow(row16_0);
                Button button16_0_0 = new Button(row16_0, Map.of(RU, "Такси бот"), level_ADD_TAXI_BOT.getIdString());
                buttonCacheRepository.save(button16_0_0);
                row16_0.add(button16_0_0);

//                Button button16_0_01 = new Button(row16_0, "Выбрать шаблон и перейти к редактированию", level_ADD_TAXI_BOT.getIdString());
//                buttonRepository.save(button16_0_01);
//                row16_0.add(button16_0_01);
//
//
//                    Button button16_0_1 = new Button(level_ADD_BOT, "Парикмахер бот", level_PARIK_BOT.getIdString());
//                    buttonRepository.save(button16_0_1);
//                    row16_0.add(button16_0_1);
//                    level_ADD_BOT.addRow(row16_0);
//                    ButtonRow row16_1 = new ArrayList<>();
//                    Button button16_1_0 = new Button(level_ADD_BOT, "Магазин одежды", level_CLOTHES_BOT.getIdString());
//                    buttonRepository.save(button16_1_0);
//                    row16_1.add(button16_1_0);
//                    Button button16_1_1 = new Button(level_ADD_BOT, "Онлайн-курсы", level_COURSES_BOT.getIdString());
//                    buttonRepository.save(button16_1_1);
//                    row16_1.add(button16_1_1);
//                    level_ADD_BOT.addRow(row16_1);
                //////////ШАБЛОН БОТА ТАКСИ

                level_ADD_TAXI_BOT.updateLevel(Users, ADD_TAXI_BOT.name(), level_ADD_BOT, false, false, true);

                levelCacheRepository.save(level_ADD_TAXI_BOT);
                Message message17_1 = new Message(level_ADD_TAXI_BOT, Map.of(RU, "Здравствуйте! Пришлите геолокацию, куда необходимо подать машину:"));
                messageCacheRepository.save(message17_1);
                level_ADD_TAXI_BOT.addMessage(message17_1);
                Message message17_2 = new Message(level_ADD_TAXI_BOT, 1, null, IOUtils.toByteArray((new ClassPathResource(MANAGEMENT_FILE)).getInputStream()), "как отправить геолокацию");
                messageCacheRepository.save(message17_2);
                level_ADD_TAXI_BOT.addMessage(message17_2);
//                        ButtonRow row17_0 = new ArrayList<>();
//                        Button button17_0_0 = new Button(level17, "Такси бот", TAXI_BOT.name());
//                        buttonRepository.save(button17_0_0);
//                        row17_0.add(button17_0_0);
//                        level17.addRow(row17_0);
//                        ButtonRow row17_1 = new ArrayList<>();
//                        Button button17_1_0 = new Button(level17, "Вернуться к выбору шаблона", ADD_BOT.name());
//                        buttonRepository.save(button17_1_0);
//                        row17_1.add(button17_1_0);
//                        level17.addRow(row17_1);

                level_TAXI_LOCATION.updateLevel(Users, TAXI_LOCATION.name(), level_ADD_TAXI_BOT, true, false, true);

                levelCacheRepository.save(level_TAXI_LOCATION);
                Message message18_1 = new Message(level_TAXI_LOCATION, Map.of(RU, "Отлично! машина может прибыть через 11 минут"));
                messageCacheRepository.save(message18_1);
                level_TAXI_LOCATION.addMessage(message18_1);
                ButtonRow row191_0 = new ButtonRow(level_TAXI_LOCATION);
                buttonRowRepository.save(row191_0);
                level_TAXI_LOCATION.addRow(row191_0);
                Button button191_0_0 = new Button(row191_0, Map.of(RU, "Подтвердите заказ такси!"), level_TAXI_LOCATION.getIdString());
                buttonCacheRepository.save(button191_0_0);
                row191_0.add(button191_0_0);


                level_TAXI_SUBMIT.updateLevel(Users, TAXI_SUBMIT.name(), level_TAXI_LOCATION, true, true, true);

                levelCacheRepository.save(level_TAXI_SUBMIT);
                Message message191_1 = new Message(level_TAXI_SUBMIT, Map.of(RU, "Ваша заявка принята!"));
                messageCacheRepository.save(message191_1);
                level_TAXI_SUBMIT.addMessage(message191_1);

//                        ButtonRow row18_0 = new ArrayList<>();
//                        Button button18_0_0 = new Button(level_ADD_TAXI_BOT, "Отправить заявку", level_SUBMIT_BOT.getIdString());
//                        buttonRepository.save(button18_0_0);
//                        row18_0.add(button18_0_0);
//                        level_ADD_TAXI_BOT.addRow(row18_0);
//
//                        ButtonRow row18_0 = new ArrayList<>();
//                        Button button18_0_0 = new Button(level_TAXI_LOCATION, "Выбрать шаблон и перейти к редактированию", ADD_TAXI_BOT.name());
//                        buttonRepository.save(button18_0_0);
//                        row18_0.add(button18_0_0);
//                        level_TAXI_LOCATION.addRow(row18_0);
//                        ButtonRow row18_1 = new ArrayList<>();
//                        Button button18_1_0 = new Button(level_TAXI_LOCATION, "Вернуться к выбору шаблона", ADD_BOT.name());
//                        buttonRepository.save(button18_1_0);
//                        row18_1.add(button18_1_0);
//                        level_TAXI_LOCATION.addRow(row18_1);


                level_EDIT_BUTTON_NAME.updateLevel(Users, EDIT_BUTTON_NAME.name(), level_TAXI_LOCATION, false);
                //level_EDIT_BUTTON_NAME.setBotLevel(true);
                levelCacheRepository.save(level_EDIT_BUTTON_NAME);
                Message message = new Message(level_EDIT_BUTTON_NAME, Map.of(RU, "Введите новое название кнопки"));
                messageCacheRepository.save(message);
                level_EDIT_BUTTON_NAME.addMessage(message);

                level_EDIT_MESSAGE.updateLevel(Users, EDIT_MESSAGE.name(), level_TAXI_LOCATION, false);
                //level_EDIT_MESSAGE.setBotLevel(true);
                levelCacheRepository.save(level_EDIT_MESSAGE);
                Message addMessage = new Message(level_EDIT_MESSAGE, Map.of(RU, "Введите новый текст сообщения"));
                messageCacheRepository.save(addMessage);
                level_EDIT_MESSAGE.addMessage(addMessage);

                level_NEW_LEVEL_BUTTON.updateLevel(Users, NEW_LEVEL_BUTTON.name(), level_TAXI_LOCATION, false);
                //level_NEW_LEVEL_BUTTON.setBotLevel(true);
                levelCacheRepository.save(level_NEW_LEVEL_BUTTON);
                Message addMessage2 = new Message(level_NEW_LEVEL_BUTTON, Map.of(RU, "Введите название кнопки для перехода"));
                messageCacheRepository.save(addMessage2);
                level_NEW_LEVEL_BUTTON.addMessage(addMessage2);

                level_NEW_LEVEL_INPUT_BUTTON.updateLevel(Users, NEW_LEVEL_INPUT_BUTTON.name(), level_TAXI_LOCATION, false);
                //level_NEW_LEVEL_INPUT_BUTTON.setBotLevel(true);
                levelCacheRepository.save(level_NEW_LEVEL_INPUT_BUTTON);
                Message addMessage3 = new Message(level_NEW_LEVEL_INPUT_BUTTON, Map.of(RU, "Введите название кнопки для перехода"));
                messageCacheRepository.save(addMessage3);
                level_NEW_LEVEL_INPUT_BUTTON.addMessage(addMessage3);

                level_NEW_LEVEL_END_BUTTON.updateLevel(Users, NEW_LEVEL_END_BUTTON.name(), level_TAXI_LOCATION, false);
                //level_NEW_LEVEL_END_BUTTON.setBotLevel(true);
                levelCacheRepository.save(level_NEW_LEVEL_END_BUTTON);
                Message addMessage4 = new Message(level_NEW_LEVEL_END_BUTTON, Map.of(RU, "Введите название кнопки для перехода"));
                messageCacheRepository.save(addMessage4);
                level_NEW_LEVEL_END_BUTTON.addMessage(addMessage4);

//                            Button yesButton = new Button(level_EDIT_MESSAGE,"Да", EDIT_MESSAGE_RESP.name());
//                            buttonRepository.save(yesButton);
//                            ButtonRow editRow = new ArrayList<>();
//                            editRow.add(yesButton);
//                            Button noButton = new Button(level_EDIT_MESSAGE,"Нет", SAVE_USER_PARAMETER.name());
//                            buttonRepository.save(noButton);
//                            editRow.add(noButton);
//                            level_EDIT_MESSAGE.addRow(editRow);
//                            levelCacheRepository.save(level_EDIT_MESSAGE);
//
//                            Level saveParameterLevel.updateLevel(CHAT, SAVE_USER_PARAMETER.name(), level_TAXI_LOCATION, true);
//                            Message messageSaveParameter = new Message(saveParameterLevel, Map.of(LanguageEnum.ru, "Сохранить ввод пользователя в параметр?", null, null, null, null);
//                            saveParameterLevel.addMessage(messageSaveParameter);
//                            Button yesButton2 = new Button(saveParameterLevel,"Да", SAVE_BOT_PARAMETER.name());
//                            buttonRepository.save(yesButton2);
//                            ButtonRow editRow2 = new ArrayList<>();
//                            editRow2.add(yesButton2);
//                            Button noButton2 = new Button(saveParameterLevel,"Нет", "no");
//                            buttonRepository.save(noButton2);
//                            editRow2.add(noButton2);
//                            saveParameterLevel.addRow(editRow2);
//                            levelCacheRepository.save(saveParameterLevel);
//
//                            Level addParameterNameLevel.updateLevel(CHAT, SAVE_BOT_PARAMETER.name(), saveParameterLevel, false);
//                            Message addParameterNameMessage = new Message(addParameterNameLevel, Map.of(LanguageEnum.ru, "Введите название параметра. Вы сможете использовать его в сообщениях и подписях кнопокс %, например - %name", null, null, null, null);
//                            addParameterNameLevel.addMessage(addParameterNameMessage);
//                            levelCacheRepository.save(addParameterNameLevel);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ BK, BM, CB -И ПОЛЛУЧАТЕЛь PI

                level_PSHARE2P.updateLevel(Users, PSHARE2P.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_PSHARE2P);
                Message message19_1 = new Message(level_PSHARE2P, Map.of(RU, "Вы получили рекомендацию от X"));
                messageCacheRepository.save(message19_1);
                level_PSHARE2P.addMessage(message19_1);

                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ PI -И ПОЛЛУЧАТЕЛЯ PI НЕТ ЕЩЕ В СИСТЕМЕ

                level_P2NOP.updateLevel(Users, P2NOP.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_P2NOP);
                Message message20_1 = new Message(level_P2NOP, Map.of(RU, "Добро пожаловать в систему Skidozona!"));
                messageCacheRepository.save(message20_1);
                level_P2NOP.addMessage(message20_1);


                level_P2NOP_RESP.updateLevel(Users, P2NOP_RESP.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_P2NOP_RESP);
                Message message20_1_1 = new Message(level_P2NOP_RESP, Map.of(RU, "Пользователю Y успешно добавлен в систему!"));
                messageCacheRepository.save(message20_1_1);
                level_P2NOP_RESP.addMessage(message20_1_1);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ PI -И ПОЛЛУЧАТЕЛь PI УЖЕ ЕСТЬ В СИСТЕМЕ

                level_P2P.updateLevel(Users, P2P.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_P2P);
                Message message21_1 = new Message(level_P2P, Map.of(RU, "Пользователь X пытался добавить Вас в систему"));
                messageCacheRepository.save(message21_1);
                level_P2P.addMessage(message21_1);


                level_P2P_RESP.updateLevel(Users, P2P_RESP.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_P2P_RESP);
                Message message21_1_1 = new Message(level_P2P_RESP, Map.of(RU, "Пользователь Y уже есть в системе"));
                messageCacheRepository.save(message21_1_1);
                level_P2P_RESP.addMessage(message21_1_1);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ BK, BM, CB, PI-И ПОЛУЧАТЕЛь BI

                level_P2B.updateLevel(Users, P2B.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_P2B);
                Message message22_1 = new Message(level_P2B, Map.of(RU, "Пользователь X имеет закладки:"));
                messageCacheRepository.save(message22_1);
                level_P2B.addMessage(message22_1);

                Message message22_2 = new Message(level_P2B, 1, Map.of(RU, "Пользователь X имеет корзину:"));
                messageCacheRepository.save(message22_2);
                level_P2B.addMessage(message22_2);

                Message message22_3 = new Message(level_P2B, 2, Map.of(RU, "Пользователь X имеет кэшбеки:"));
                messageCacheRepository.save(message22_3);
                level_P2B.addMessage(message22_3);

                ButtonRow row22_0 = new ButtonRow(level_P2B);
                buttonRowRepository.save(row22_0);
                level_P2B.addRow(row22_0);
                Button button22_0_0 = new Button(row22_0, Map.of(RU, "Предложить кэшбек вручную"), level_P2B_PROPOSE_CASHBACK.getIdString());
                buttonCacheRepository.save(button22_0_0);
                row22_0.add(button22_0_0);
                level_P2B.addRow(row22_0);

                ButtonRow row22_01 = new ButtonRow(level_P2B);
                buttonRowRepository.save(row22_01);
                level_P2B.addRow(row22_01);
                Button button22_0_1 = new Button(row22_01, Map.of(RU, "Списать кэшбек вручную"), level_P2B_WRITEOFF_CASHBACK.getIdString());
                buttonCacheRepository.save(button22_0_1);
                row22_01.add(button22_0_1);
                level_P2B.addRow(row22_01);

                ButtonRow row22_1 = new ButtonRow(level_P2B);
                buttonRowRepository.save(row22_1);
                level_P2B.addRow(row22_1);
                Button button22_1_1 = new Button(row22_1, Map.of(RU, "Начислить купон вручную"), level_P2B_CHARGE_COUPON.getIdString());
                buttonCacheRepository.save(button22_1_1);
                row22_1.add(button22_1_1);
                level_P2B.addRow(row22_1);

                ButtonRow row22_2 = new ButtonRow(level_P2B);
                buttonRowRepository.save(row22_2);
                level_P2B.addRow(row22_2);
                Button button22_2_0 = new Button(row22_2, Map.of(RU, "Списать купон вручную"), level_P2B_WRITEOFF_COUPON.getIdString());
                buttonCacheRepository.save(button22_2_0);
                row22_2.add(button22_2_0);
                level_P2B.addRow(row22_2);

                ButtonRow row22_4 = new ButtonRow(level_P2B);
                buttonRowRepository.save(row22_4);
                level_P2B.addRow(row22_4);
                Button button22_4_0 = new Button(row22_4, Map.of(RU, "Начислить купон по корзине"), level_P2B_CHARGE_COUPON_BASKET.getIdString());
                buttonCacheRepository.save(button22_4_0);
                row22_4.add(button22_4_0);
                level_P2B.addRow(row22_4);

                ButtonRow row22_3 = new ButtonRow(level_P2B);
                buttonRowRepository.save(row22_3);
                level_P2B.addRow(row22_3);
                Button button22_3_0 = new Button(row22_3, Map.of(RU, "Списать купон по корзине"), level_P2B_WRITEOFF_COUPON_BASKET.getIdString());
                buttonCacheRepository.save(button22_3_0);
                row22_3.add(button22_3_0);
                level_P2B.addRow(row22_3);

                ButtonRow row22_5 = new ButtonRow(level_P2B);
                buttonRowRepository.save(row22_5);
                Button button22_1_0 = new Button(row22_5, Map.of(RU, "Подтвердить кэшбек по корзине"), level_P2B_CHARGE_BASKET_CASHBACK.getIdString());
                buttonCacheRepository.save(button22_1_0);
                row22_5.add(button22_1_0);
                level_P2B.addRow(row22_5);


                level_NEGATIVE_SUM.updateLevel(Users, NEGATIVE_SUM.name(), level_P2B, true);

                levelCacheRepository.save(level_NEGATIVE_SUM);
                Message message22_000 = new Message(level_NEGATIVE_SUM, Map.of(RU, "Сумма должна быть положительной"));
                messageCacheRepository.save(message22_000);
                level_NEGATIVE_SUM.addMessage(message22_000);

                level_NEGATIVE_COUNT.updateLevel(Users, NEGATIVE_COUNT.name(), level_P2B, true);

                levelCacheRepository.save(level_NEGATIVE_COUNT);
                Message message22_111 = new Message(level_NEGATIVE_COUNT, Map.of(RU, "Количество должно быть положительным"));
                messageCacheRepository.save(message22_111);
                level_NEGATIVE_COUNT.addMessage(message22_111);

                ////НАЧИСЛЕНИЕ КЭШБЕКА

                level_P2B_PROPOSE_CASHBACK.updateLevel(Users, P2B_PROPOSE_CASHBACK.name(), level_P2B, true);

                levelCacheRepository.save(level_P2B_PROPOSE_CASHBACK);
                Message message22_0_0_1 = new Message(level_P2B_PROPOSE_CASHBACK, Map.of(RU, "Введите сумму начисленного кэшбека:"));
                messageCacheRepository.save(message22_0_0_1);
                level_P2B_PROPOSE_CASHBACK.addMessage(message22_0_0_1);

                level_P2B_PROPOSE_CASHBACK_RESP.updateLevel(Users, P2B_PROPOSE_CASHBACK_RESP.name(), level_P2B_PROPOSE_CASHBACK, true);

                levelCacheRepository.save(level_P2B_PROPOSE_CASHBACK_RESP);
                Message message22_0_1_1 = new Message(level_P2B_PROPOSE_CASHBACK_RESP, Map.of(RU, "Клиенту начислено X"));
                messageCacheRepository.save(message22_0_1_1);
                level_P2B_PROPOSE_CASHBACK_RESP.addMessage(message22_0_1_1);

                /////СПИСАНИЕ КЭШБЕКА

                level_P2B_WRITEOFF_CASHBACK.updateLevel(Users, P2B_WRITEOFF_CASHBACK.name(), level_P2B, false);

                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK);
                Message message22_1_0 = new Message(level_P2B_WRITEOFF_CASHBACK, Map.of(RU, "Введите сумму списания кэшбека:"));
                messageCacheRepository.save(message22_1_0);
                level_P2B_WRITEOFF_CASHBACK.addMessage(message22_1_0);

                level_P2B_WRITEOFF_CASHBACK_RESP.updateLevel(Users, P2B_WRITEOFF_CASHBACK_RESP.name(), level_P2B_WRITEOFF_CASHBACK, false);//true

                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_RESP);
                Message message22_1_3 = new Message(level_P2B_WRITEOFF_CASHBACK_RESP, Map.of(RU, "Запрос на списание отправлен"));
                messageCacheRepository.save(message22_1_3);
                level_P2B_WRITEOFF_CASHBACK_RESP.addMessage(message22_1_3);

                level_P2B_WRITEOFF_CASHBACK_REQUEST.updateLevel(Users, P2B_WRITEOFF_CASHBACK_REQUEST.name(), level_P2B_WRITEOFF_CASHBACK, true);

                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_REQUEST);
                Message message22_1_2 = new Message(level_P2B_WRITEOFF_CASHBACK_REQUEST, Map.of(RU, "Магазин Y предлагает списать X"));
                messageCacheRepository.save(message22_1_2);
                level_P2B_WRITEOFF_CASHBACK_REQUEST.addMessage(message22_1_2);
                ButtonRow row22_0_0 = new ButtonRow(level_P2B_WRITEOFF_CASHBACK_REQUEST);
                buttonRowRepository.save(row22_0_0);
                Button button22_0_0_0 = new Button(row22_0_0, Map.of(RU, "Списать"), level_P2B_WRITEOFF_CASHBACK_APPROVE.getIdString());
                buttonCacheRepository.save(button22_0_0_0);
                row22_0_0.add(button22_0_0_0);
                Button button22_0_0_1 = new Button(row22_0_0, Map.of(RU, "Отклонить"), level_P2B_WRITEOFF_CASHBACK_DISMISS.getIdString());
                buttonCacheRepository.save(button22_0_0_1);
                row22_0_0.add(button22_0_0_1);
                level_P2B_WRITEOFF_CASHBACK_REQUEST.addRow(row22_0_0);

                level_P2B_WRITEOFF_CASHBACK_APPROVE.updateLevel(Users, P2B_WRITEOFF_CASHBACK_APPROVE.name(), level_P2B, false);

                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_APPROVE);
                Message message22_1_1 = new Message(level_P2B_WRITEOFF_CASHBACK_APPROVE, Map.of(RU, "Списано кешбека X"));
                messageCacheRepository.save(message22_1_1);
                level_P2B_WRITEOFF_CASHBACK_APPROVE.addMessage(message22_1_1);

                level_P2B_WRITEOFF_CASHBACK_DISMISS.updateLevel(Users, P2B_WRITEOFF_CASHBACK_DISMISS.name(), level_P2B, false);

                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_DISMISS);
                Message message22_1_4 = new Message(level_P2B_WRITEOFF_CASHBACK_DISMISS, Map.of(RU, "Списание отклонено"));
                messageCacheRepository.save(message22_1_4);
                level_P2B_WRITEOFF_CASHBACK_DISMISS.addMessage(message22_1_4);

                ///// ПОДТВЕРЖДЕНИЕ ПО КОРЗИНЕ

                level_P2B_CHARGE_BASKET_CASHBACK.updateLevel(Users, P2B_CHARGE_BASKET_CASHBACK.name(), level_P2B, true);

                levelCacheRepository.save(level_P2B_CHARGE_BASKET_CASHBACK);
                Message message22_2_0 = new Message(level_P2B_CHARGE_BASKET_CASHBACK, Map.of(RU, "Сумма списания кэшбека по корзине X, сумма начисления кэшбека Y"));
                messageCacheRepository.save(message22_2_0);
                level_P2B_CHARGE_BASKET_CASHBACK.addMessage(message22_2_0);
                ButtonRow row22_2_0 = new ButtonRow(level_P2B_CHARGE_BASKET_CASHBACK);
                buttonRowRepository.save(row22_2_0);
                Button button22_2_0_0 = new Button(row22_2_0, Map.of(RU, "Подтвердить!"), level_P2B_APPROVE_BASKET_CASHBACK.getIdString());
                buttonCacheRepository.save(button22_2_0_0);
                row22_2_0.add(button22_2_0_0);
                Button button22_2_0_1 = new Button(row22_2_0, Map.of(RU, "Начислить вручную"), level_P2B_PROPOSE_CASHBACK.getIdString());
                buttonCacheRepository.save(button22_2_0_1);
                row22_2_0.add(button22_2_0_1);
                level_P2B_CHARGE_BASKET_CASHBACK.addRow(row22_2_0);


                level_P2B_APPROVE_BASKET_CASHBACK.updateLevel(Users, P2B_APPROVE_BASKET_CASHBACK.name(), level_P2B, false);//true

                levelCacheRepository.save(level_P2B_APPROVE_BASKET_CASHBACK);
                Message message22_2_1 = new Message(level_P2B_APPROVE_BASKET_CASHBACK, Map.of(RU, "Сумма списания кэшбека по корзине X, сумма начисления кэшбека Y"));
                messageCacheRepository.save(message22_2_1);
                level_P2B_APPROVE_BASKET_CASHBACK.addMessage(message22_2_1);


//                Level level_P2B_WRITEOFF_CASHBACK_RESP.updateLevel(CHAT, P2B_WRITEOFF_CASHBACK_RESP.name(), level_P2B_WRITEOFF_CASHBACK, true);
//                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_RESP);
//                Message message22_1_3 = new Message(level_P2B_WRITEOFF_CASHBACK_RESP, Map.of(LanguageEnum.ru, "Запрос на списание отправлен", null, null, null, null);
//                messageRepository.save(message22_1_3);
//                level_P2B_WRITEOFF_CASHBACK_RESP.addMessage(message22_1_3);
//
//                Level level_P2B_WRITEOFF_CASHBACK_REQUEST.updateLevel(CHAT, P2B_WRITEOFF_CASHBACK_REQUEST.name(), level_P2B_WRITEOFF_CASHBACK, true);
//                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_REQUEST);
//                Message message22_1_2 = new Message(level_P2B_WRITEOFF_CASHBACK_REQUEST, Map.of(LanguageEnum.ru, "Магазин Y предлагает списать X", null, null, null, null);
//                messageRepository.save(message22_1_2);
//                level_P2B_WRITEOFF_CASHBACK_REQUEST.addMessage(message22_1_2);
//                ButtonRow row22_0_0 = new ArrayList<>();
//                Button button22_0_0_0 = new Button(level_P2B, "Списать", P2B_WRITEOFF_CASHBACK_APPROVE.name());
//                buttonRepository.save(button22_0_0_0);
//                row22_0_0.add(button22_0_0_0);
//                Button button22_0_0_1 = new Button(level_P2B, "Отклонить", P2B_WRITEOFF_CASHBACK_DISMISS.name());
//                buttonRepository.save(button22_0_0_1);
//                row22_0_0.add(button22_0_0_1);
//                level_P2B.addRow(row22_0_0);
//
//                Level level_P2B_WRITEOFF_CASHBACK_APPROVE.updateLevel(CHAT, P2B_WRITEOFF_CASHBACK_APPROVE.name(), level_P2B, true);
//                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_APPROVE);
//                Message message22_1_1 = new Message(level_P2B_WRITEOFF_CASHBACK_APPROVE, Map.of(LanguageEnum.ru, "Списано кешбека X", null, null, null, null);
//                messageRepository.save(message22_1_1);
//                level_P2B_WRITEOFF_CASHBACK_APPROVE.addMessage(message22_1_1);
//
//                Level level_P2B_WRITEOFF_CASHBACK_DISMISS.updateLevel(CHAT, P2B_WRITEOFF_CASHBACK_DISMISS.name(), level_P2B, true);
//                levelCacheRepository.save(level_P2B_WRITEOFF_CASHBACK_DISMISS);
//                Message message22_1_4 = new Message(level_P2B_WRITEOFF_CASHBACK_DISMISS, Map.of(LanguageEnum.ru, "Списание отклонено", null, null, null, null);
//                messageRepository.save(message22_1_4);
//                level_P2B_WRITEOFF_CASHBACK_DISMISS.addMessage(message22_1_4);


                /////НАЧИСЛЕНИЕ КУПОНА

                level_P2B_CHARGE_COUPON.updateLevel(Users, P2B_CHARGE_COUPON.name(), level_P2B, false);

                levelCacheRepository.save(level_P2B_CHARGE_COUPON);
                Message message22_3_0 = new Message(level_P2B_CHARGE_COUPON, Map.of(RU, "Выберите акцию:"));
                messageCacheRepository.save(message22_3_0);
                level_P2B_CHARGE_COUPON.addMessage(message22_3_0);

                /////НАЧИСЛЕНИЕ КУПОНА

                level_P2B_CHARGE_COUPON_BASKET.updateLevel(Users, P2B_CHARGE_COUPON_BASKET.name(), level_P2B, false);

                levelCacheRepository.save(level_P2B_CHARGE_COUPON_BASKET);
                Message message220_3_0 = new Message(level_P2B_CHARGE_COUPON_BASKET, Map.of(RU, "Выберите акцию:"));
                messageCacheRepository.save(message220_3_0);
                level_P2B_CHARGE_COUPON_BASKET.addMessage(message220_3_0);


                level_P2B_CHARGE_COUPON_REQUEST.updateLevel(Users, P2B_CHARGE_COUPON_REQUEST.name(), level_P2B_CHARGE_COUPON, true);

                levelCacheRepository.save(level_P2B_CHARGE_COUPON_REQUEST);
                Message message220_3_1 = new Message(level_P2B_CHARGE_COUPON_REQUEST, Map.of(RU, "Введите количество начисленных купонов по акции X"));
                messageCacheRepository.save(message220_3_1);
                level_P2B_CHARGE_COUPON_REQUEST.addMessage(message220_3_1);


                level_P2B_CHARGE_COUPON_RESP.updateLevel(Users, P2B_CHARGE_COUPON_RESP.name(), level_P2B_CHARGE_COUPON_REQUEST, true);

                levelCacheRepository.save(level_P2B_CHARGE_COUPON_RESP);
                Message message22_3_1 = new Message(level_P2B_CHARGE_COUPON_RESP, Map.of(RU, "начислено количество X"));
                messageCacheRepository.save(message22_3_1);
                level_P2B_CHARGE_COUPON_RESP.addMessage(message22_3_1);


                /////СПИСАНИЕ КУПОНА

                level_P2B_WRITEOFF_COUPON_BASKET.updateLevel(Users, P2B_WRITEOFF_COUPON_BASKET.name(), level_P2B, true);

                levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_BASKET);
                Message message2201_4_0 = new Message(level_P2B_WRITEOFF_COUPON_BASKET, Map.of(RU, "Выберите акцию:"));
                messageCacheRepository.save(message2201_4_0);
                level_P2B_WRITEOFF_COUPON_BASKET.addMessage(message2201_4_0);

                /////СПИСАНИЕ КУПОНА

                level_P2B_WRITEOFF_COUPON.updateLevel(Users, P2B_WRITEOFF_COUPON.name(), level_P2B, true);

                levelCacheRepository.save(level_P2B_WRITEOFF_COUPON);
                Message message220_4_0 = new Message(level_P2B_WRITEOFF_COUPON, Map.of(RU, "Выберите акцию:"));
                messageCacheRepository.save(message220_4_0);
                level_P2B_WRITEOFF_COUPON.addMessage(message220_4_0);

                level_P2B_WRITEOFF_COUPON_SELECT_ACTION.updateLevel(Users, P2B_WRITEOFF_COUPON_SELECT_ACTION.name(), level_P2B, true);

                levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_SELECT_ACTION);
                Message message22_4_0 = new Message(level_P2B_WRITEOFF_COUPON_SELECT_ACTION, Map.of(RU, "Введите количество списаний X"));
                messageCacheRepository.save(message22_4_0);
                level_P2B_WRITEOFF_COUPON_SELECT_ACTION.addMessage(message22_4_0);

                level_P2B_WRITEOFF_COUPON_REQUEST.updateLevel(Users, P2B_WRITEOFF_COUPON_REQUEST.name(), level_P2B_WRITEOFF_COUPON_SELECT_ACTION, true);

                levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_REQUEST);
                Message message22_4_2 = new Message(level_P2B_WRITEOFF_COUPON_REQUEST, Map.of(RU, "Магазин Y предлагает списать X"));
                messageCacheRepository.save(message22_4_2);
                level_P2B_WRITEOFF_COUPON_REQUEST.addMessage(message22_4_2);
                ButtonRow row22_4_0 = new ButtonRow(level_P2B_WRITEOFF_COUPON_REQUEST);
                buttonRowRepository.save(row22_4_0);
                Button button22_4_0_0 = new Button(row22_4_0, Map.of(RU, "Списать"), level_P2B_PROPOSE_CASHBACK.getIdString());
                buttonCacheRepository.save(button22_4_0_0);
                row22_4_0.add(button22_4_0_0);
                Button button22_4_0_1 = new Button(row22_4_0, Map.of(RU, "Отклонить"), level_P2B_WRITEOFF_CASHBACK_DISMISS.getIdString());
                buttonCacheRepository.save(button22_4_0_1);
                row22_4_0.add(button22_4_0_1);
                level_P2B_WRITEOFF_COUPON_REQUEST.addRow(row22_4_0);

                level_P2B_WRITEOFF_COUPON_RESP.updateLevel(Users, P2B_WRITEOFF_COUPON_RESP.name(), level_P2B_WRITEOFF_COUPON_REQUEST, true);

                levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_RESP);
                Message message22_4_1 = new Message(level_P2B_WRITEOFF_COUPON_RESP, Map.of(RU, "Списано количество X"));
                messageCacheRepository.save(message22_4_1);
                level_P2B_WRITEOFF_COUPON_RESP.addMessage(message22_4_1);

                level_P2B_WRITEOFF_COUPON_APPROVE.updateLevel(Users, P2B_WRITEOFF_COUPON_APPROVE.name(), level_P2B_WRITEOFF_COUPON_RESP, false);

                levelCacheRepository.save(level_P2B_WRITEOFF_COUPON_APPROVE);
                Message message221_4_1 = new Message(level_P2B_WRITEOFF_COUPON_APPROVE, Map.of(RU, "Списано количество X"));
                messageCacheRepository.save(message221_4_1);
                level_P2B_WRITEOFF_COUPON_APPROVE.addMessage(message221_4_1);

//            ButtonRow row22_0 = new ArrayList<>();
//            Button button49_0_0 = new Button(level_TAXI_LOCATION, "Выбрать шаблон и перейти к редактированию", ADD_TAXI_BOT.name());
//            buttonRepository.save(button49_0_0);
//            row18_0.add(button49_0_0);
//            level_TAXI_LOCATION.addRow(row22_0);
//            ButtonRow row22_1 = new ArrayList<>();
//            Button button49_0_1 = new Button(level_TAXI_LOCATION, "Вернуться к выбору шаблона", ADD_BOT.name());
//            buttonRepository.save(button49_0_1);
//            row22_1.add(button49_0_1);
//            level_TAXI_LOCATION.addRow(row22_1);


                level_P2B_RESP.updateLevel(Users, P2B_RESP.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_P2B_RESP);
                Message message22_11_1 = new Message(level_P2B_RESP, Map.of(RU, "Пользователь X имеет закладки:"));
                messageCacheRepository.save(message22_11_1);
                level_P2B_RESP.addMessage(message22_11_1);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ BI -И ПОЛУЧАТЕЛь BI
                //ЕСЛИ УЖЕ ЕСТЬ ПАРТНЕР
                level_B2B.updateLevel(Users, B2B.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_B2B);
                Message message24_1_1 = new Message(level_B2B, Map.of(RU, "Баланс кешбека с партнером X - вы/он должен Y. Списать кэбек?"));
                messageCacheRepository.save(message24_1_1);
                level_B2B.addMessage(message24_1_1);

                ButtonRow row24_1_0 = new ButtonRow(level_B2B);
                buttonRowRepository.save(row24_1_0);
                Button button24_1_0_0 = new Button(row24_1_0, Map.of(RU, "Списать кэбек партнера?/Списать Ваш кэбек?"), level_APPROVE_NEW_PARTNER.getIdString());
                buttonCacheRepository.save(button24_1_0_0);
                row24_1_0.add(button24_1_0_0);
                Button button24_1_0_1 = new Button(row24_1_0, Map.of(RU, "Нет"), level_DISCARD_NEW_PARTNER.getIdString());
                buttonCacheRepository.save(button24_1_0_1);
                row24_1_0.add(button24_1_0_1);
                level_B2B.addRow(row24_1_0);


                //ЕСЛИ НОВЫЙ ПАРТНЕР
                level_B2NOB.updateLevel(Users, B2NOB.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_B2NOB);
                Message message24_1 = new Message(level_B2NOB, Map.of(RU, "Добавить X  партнёром?"));
                messageCacheRepository.save(message24_1);
                level_B2NOB.addMessage(message24_1);


                ButtonRow row24_0 = new ButtonRow(level_B2NOB);
                buttonRowRepository.save(row24_0);
                Button button24_0_0 = new Button(row24_0, Map.of(RU, "Да"), level_APPROVE_NEW_PARTNER.getIdString());
                buttonCacheRepository.save(button24_0_0);
                row24_0.add(button24_0_0);
                level_B2NOB.addRow(row24_0);
                Button button24_0_1 = new Button(row24_0, Map.of(RU, "Нет"), level_DISCARD_NEW_PARTNER.getIdString());
                buttonCacheRepository.save(button24_0_1);
                row24_0.add(button24_0_1);
                level_B2NOB.addRow(row24_0);

                level_DISCARD_NEW_PARTNER.updateLevel(Users, DISCARD_NEW_PARTNER.name(), level_B2NOB, false);

                levelCacheRepository.save(level_DISCARD_NEW_PARTNER);
                Message message44_1 = new Message(level_DISCARD_NEW_PARTNER, Map.of(RU, "Запрос отклонен"));
                messageCacheRepository.save(message44_1);
                level_DISCARD_NEW_PARTNER.addMessage(message44_1);


                level_APPROVE_NEW_PARTNER.updateLevel(Users, APPROVE_NEW_PARTNER.name(), level_B2NOB, false);

                levelCacheRepository.save(level_APPROVE_NEW_PARTNER);
                Message message45_1 = new Message(level_APPROVE_NEW_PARTNER, Map.of(RU, "Введите размер скидки для партнера при переходе по рекомендации"));
                messageCacheRepository.save(message45_1);
                level_APPROVE_NEW_PARTNER.addMessage(message45_1);


//                level_APPROVE_NEW_PARTNER.updateLevel(Users, APPROVE_NEW_PARTNER.name(), level_B2NOB, false);
//
//                levelCacheRepository.save(level_APPROVE_NEW_PARTNER);
//                Message message45_1 = new Message(level_APPROVE_NEW_PARTNER, Map.of(LanguageEnum.ru, "Введите размер скидки для партнера при переходе по рекомендации"));
//                messageCacheRepository.save(message45_1);
//                level_APPROVE_NEW_PARTNER.addMessage(message45_1);


                //ЕСЛИ УЖЕ НОВАЯ ГРУППА
                level_NEW_GRUPP.updateLevel(Users, NEW_GRUPP.name(), level_INITIALIZE, false);

                levelCacheRepository.save(level_NEW_GRUPP);
                Message message49_1 = new Message(level_NEW_GRUPP, Map.of(RU, "Добавить Группу W?"));
                messageCacheRepository.save(message49_1);
                level_NEW_GRUPP.addMessage(message49_1);

                ButtonRow row49_0 = new ButtonRow(level_NEW_GRUPP);
                buttonRowRepository.save(row49_0);
                Button button49_0_0 = new Button(row49_0, Map.of(RU, "Да"), level_APPROVE_NEW_GRUPP.getIdString());
                buttonCacheRepository.save(button49_0_0);
                row49_0.add(button49_0_0);
                level_NEW_GRUPP.addRow(row49_0);
                Button button49_0_1 = new Button(row49_0, Map.of(RU, "Нет"), level_DISCARD_NEW_GRUPP.getIdString());
                buttonCacheRepository.save(button49_0_1);
                row49_0.add(button49_0_1);
                level_NEW_GRUPP.addRow(row49_0);

                level_DISCARD_NEW_GRUPP.updateLevel(Users, DISCARD_NEW_GRUPP.name(), level_NEW_GRUPP, false);

                levelCacheRepository.save(level_DISCARD_NEW_GRUPP);
                Message message50_1 = new Message(level_DISCARD_NEW_GRUPP, Map.of(RU, "Запрос отклонен"));
                messageCacheRepository.save(message50_1);
                level_DISCARD_NEW_GRUPP.addMessage(message50_1);


                level_APPROVE_NEW_GRUPP.updateLevel(Users, APPROVE_NEW_GRUPP.name(), level_NEW_GRUPP, false);

                levelCacheRepository.save(level_APPROVE_NEW_GRUPP);
                Message message51_1 = new Message(level_APPROVE_NEW_GRUPP, Map.of(RU, "Введите лимит выплаты кэшбека группы"));
                messageCacheRepository.save(message51_1);
                level_APPROVE_NEW_GRUPP.addMessage(message51_1);

                System.out.println("PRE addFinalButton");

                addFinalButton(level_INITIALIZE);

                System.out.println("POST addFinalButton");

                mapLevels();
            } catch (IOException | WriterException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isBotLevel = false;

    private Bot bot = null;


    private void mapLevels() {

        levelDTO_INITIALIZE222 = levelMapper.toDto(level_INITIALIZE222);
        levelDTO_INITIALIZE0 = levelMapper.toDto(level_INITIALIZE0);

        System.out.println("mapLevels---------level_INITIALIZE" + level_INITIALIZE);

        levelDTO_INITIALIZE = levelMapper.toDto(level_INITIALIZE);

        System.out.println("mapLevels---------levelDTO_INITIALIZE " + levelDTO_INITIALIZE);

        levelDTO_ADMIN = levelMapper.toDto(level_ADMIN);
        levelDTO_ADMIN_ADMIN = levelMapper.toDto(level_ADMIN_ADMIN);

        levelDTO_LANGUAGES = levelMapper.toDto(level_LANGUAGES);
        levelDTO_LANGUAGER = levelMapper.toDto(level_LANGUAGER);
        levelDTO_MAP = levelMapper.toDto(level_MAP);


        levelDTO_MONITOR = levelMapper.toDto(level_MONITOR);
        levelDTO_MONITOR_PRICE = levelMapper.toDto(level_MONITOR_PRICE);
        levelDTO_MONITOR_RESP = levelMapper.toDto(level_MONITOR_RESP);

        levelDTO_GOODS_LIST = levelMapper.toDto(level_GOODS_LIST);
        levelDTO_SHOP_BOTS = levelMapper.toDto(level_SHOP_BOTS);
        levelDTO_PARTNERS = levelMapper.toDto(level_PARTNERS);
        levelDTO_SELLERS = levelMapper.toDto(level_SELLERS);
        levelDTO_SELLERS_REMOVE = levelMapper.toDto(level_SELLERS_REMOVE);
        levelDTO_SELLERS_ADD = levelMapper.toDto(level_SELLERS_ADD);
        levelDTO_SELLERS_ADD_APPROVE = levelMapper.toDto(level_SELLERS_ADD_APPROVE);
        levelDTO_SELLERS_ADD_DISMISS = levelMapper.toDto(level_SELLERS_ADD_DISMISS);
        levelDTO_SELLERS_REMOVE_APPROVE = levelMapper.toDto(level_SELLERS_REMOVE_APPROVE);
        levelDTO_SELLERS_REMOVE_DISMISS = levelMapper.toDto(level_SELLERS_REMOVE_DISMISS);
        levelDTO_BASIC = levelMapper.toDto(level_BASIC);
        levelDTO_WITHDRAW_PARTNER = levelMapper.toDto(level_WITHDRAW_PARTNER);
        levelDTO_WITHDRAW_PARTNER_GROUP = levelMapper.toDto(level_WITHDRAW_PARTNER_GROUP);
        levelDTO_WITHDRAW_PARTNER_RESP = levelMapper.toDto(level_WITHDRAW_PARTNER_RESP);
        levelDTO_WITHDRAW_PARTNER_END = levelMapper.toDto(level_WITHDRAW_PARTNER_END);
        levelDTO_SEARCH_PARTNER = levelMapper.toDto(level_SEARCH_PARTNER);
        levelDTO_SEARCH_PARTNER_RESP = levelMapper.toDto(level_SEARCH_PARTNER_RESP);
        levelDTO_SEARCH_PARTNER_RESP_BUTTON = levelMapper.toDto(level_SEARCH_PARTNER_RESP_BUTTON);
        levelDTO_SEARCH_PARTNER_RATE = levelMapper.toDto(level_SEARCH_PARTNER_RATE);
        levelDTO_SEARCH_PARTNER_LIMIT = levelMapper.toDto(level_SEARCH_PARTNER_LIMIT);
        levelDTO_SEARCH_PARTNER_END = levelMapper.toDto(level_SEARCH_PARTNER_END);
        levelDTO_SEARCH_GROUP = levelMapper.toDto(level_SEARCH_GROUP);
        levelDTO_SEARCH_GROUP_RESP = levelMapper.toDto(level_SEARCH_GROUP_RESP);
        levelDTO_SEARCH_GROUP_LIMIT = levelMapper.toDto(level_SEARCH_GROUP_LIMIT);
        levelDTO_SEARCH_GROUP_END = levelMapper.toDto(level_SEARCH_GROUP_END);
        levelDTO_ADD_PARTNER = levelMapper.toDto(level_ADD_PARTNER);
        levelDTO_ADD_GROUP = levelMapper.toDto(level_ADD_GROUP);
        levelDTO_ACTIONS = levelMapper.toDto(level_ACTIONS);
        levelDTO_BASKETS_FOR_SHOP = levelMapper.toDto(level_BASKETS_FOR_SHOP);
        levelDTO_ACTION_TYPE = levelMapper.toDto(level_ACTION_TYPE);
        levelDTO_SELECT_LEVEL_TYPE = levelMapper.toDto(level_SELECT_LEVEL_TYPE);
        levelDTO_MULTI_ACTION_LEVEL = levelMapper.toDto(level_MULTI_ACTION_LEVEL);
        levelDTO_MULTI_ACTION_LEVEL_BASIC = levelMapper.toDto(level_MULTI_ACTION_LEVEL_BASIC);
        levelDTO_MULTI_LEVEL_RATE = levelMapper.toDto(level_MULTI_LEVEL_RATE);
        levelDTO_MULTI_LEVEL_RATE_BASIC = levelMapper.toDto(level_MULTI_LEVEL_RATE_BASIC);
        levelDTO_MULTI_LEVEL_QUESTION = levelMapper.toDto(level_MULTI_LEVEL_QUESTION);
        levelDTO_MULTI_LEVEL_QUESTION_BASIC = levelMapper.toDto(level_MULTI_LEVEL_QUESTION_BASIC);
        levelDTO_ONE_LEVEL_RATE = levelMapper.toDto(level_ONE_LEVEL_RATE);
        levelDTO_ONE_LEVEL_RATE_BASIC = levelMapper.toDto(level_ONE_LEVEL_RATE_BASIC);
        levelDTO_ACTION_RATE_WITHDRAW = levelMapper.toDto(level_ACTION_RATE_WITHDRAW);
        levelDTO_ACTION_RATE_WITHDRAW_BASIC = levelMapper.toDto(level_ACTION_RATE_WITHDRAW_BASIC);
        levelDTO_ACTION_RATE_PARTNER = levelMapper.toDto(level_ACTION_RATE_PARTNER);
        levelDTO_ADD_ACTION_RATE_SOURCE = levelMapper.toDto(level_ADD_ACTION_RATE_SOURCE);
        levelDTO_ADD_ACTION_COUPON_TARGET = levelMapper.toDto(level_ADD_ACTION_COUPON_TARGET);
        levelDTO_COUPON = levelMapper.toDto(level_COUPON);
        levelDTO_COUPON_NUMBER = levelMapper.toDto(level_COUPON_NUMBER);
        levelDTO_COUPON_RATE_WITHDRAW = levelMapper.toDto(level_COUPON_RATE_WITHDRAW);
        levelDTO_ADD_ACTION_COUPON_SOURCE = levelMapper.toDto(level_ADD_ACTION_COUPON_SOURCE);
        levelDTO_ADD_ACTION_RATE_TARGET = levelMapper.toDto(level_ADD_ACTION_RATE_TARGET);
        levelDTO_LINK_TO_PRODUCT = levelMapper.toDto(level_LINK_TO_PRODUCT);
        levelDTO_LINK_TO_PRODUCT_NUMBER = levelMapper.toDto(level_LINK_TO_PRODUCT_NUMBER);
        levelDTO_ADD_ACTION_LINK_SOURCE = levelMapper.toDto(level_ADD_ACTION_LINK_SOURCE);
        levelDTO_ADD_ACTION_LINK_TARGET = levelMapper.toDto(level_ADD_ACTION_LINK_TARGET);
        levelDTO_MY_SHOPS = levelMapper.toDto(level_MY_SHOPS);
        levelDTO_CASHBACKS = levelMapper.toDto(level_CASHBACKS);
        levelDTO_CONNECT = levelMapper.toDto(level_CONNECT);
        levelDTO_CONNECT_SHOP = levelMapper.toDto(level_CONNECT_SHOP);
        levelDTO_BOOKMARKS = levelMapper.toDto(level_BOOKMARKS);
        levelDTO_ADD_BOOKMARK = levelMapper.toDto(level_ADD_BOOKMARK);
        levelDTO_BASKET = levelMapper.toDto(level_BASKET);
        levelDTO_BASKET_ARCHIVE = levelMapper.toDto(level_BASKET_ARCHIVE);
        levelDTO_ADD_BASKET = levelMapper.toDto(level_ADD_BASKET);
        levelDTO_SEARCH = levelMapper.toDto(level_SEARCH);
        levelDTO_SEARCH_RESULT = levelMapper.toDto(level_SEARCH_RESULT);
        levelDTO_SEARCH_RESULT_PRODUCT = levelMapper.toDto(level_SEARCH_RESULT_PRODUCT);
        levelDTO_CONSTRUCT = levelMapper.toDto(level_CONSTRUCT);
        levelDTO_ADMIN_SHOPS = levelMapper.toDto(level_ADMIN_SHOPS);
        levelDTO_CONSTRUCT_SARAFAN_SHARE = levelMapper.toDto(level_CONSTRUCT_SARAFAN_SHARE);
        levelDTO_CONSTRUCT_MIN_BILL_SHARE = levelMapper.toDto(level_CONSTRUCT_MIN_BILL_SHARE);
        levelDTO_CONSTRUCT_ADD = levelMapper.toDto(level_CONSTRUCT_ADD);
        levelDTO_ADD_GOODS = levelMapper.toDto(level_ADD_GOODS);
        levelDTO_ADD_GOODS_NAME = levelMapper.toDto(level_ADD_GOODS_NAME);
        levelDTO_ADD_GOODS_PHOTO = levelMapper.toDto(level_ADD_GOODS_PHOTO);
        levelDTO_ADD_GOODS_DESCRIPTION = levelMapper.toDto(level_ADD_GOODS_DESCRIPTION);
        levelDTO_ADD_GOODS_PRICE = levelMapper.toDto(level_ADD_GOODS_PRICE);
        levelDTO_ADD_GOODS_END = levelMapper.toDto(level_ADD_GOODS_END);
        levelDTO_ADD_BOT = levelMapper.toDto(level_ADD_BOT);
        levelDTO_ADD_TAXI_BOT = levelMapper.toDto(level_ADD_TAXI_BOT);
        levelDTO_TAXI_LOCATION = levelMapper.toDto(level_TAXI_LOCATION);
        levelDTO_TAXI_SUBMIT = levelMapper.toDto(level_TAXI_SUBMIT);
        levelDTO_PSHARE2P = levelMapper.toDto(level_PSHARE2P);
        levelDTO_P2NOP = levelMapper.toDto(level_P2NOP);
        levelDTO_P2NOP_RESP = levelMapper.toDto(level_P2NOP_RESP);
        levelDTO_P2P = levelMapper.toDto(level_P2P);
        levelDTO_P2P_RESP = levelMapper.toDto(level_P2P_RESP);
        levelDTO_P2B = levelMapper.toDto(level_P2B);
        levelDTO_NEGATIVE_SUM = levelMapper.toDto(level_NEGATIVE_SUM);
        levelDTO_NEGATIVE_COUNT = levelMapper.toDto(level_NEGATIVE_COUNT);
        levelDTO_P2B_PROPOSE_CASHBACK = levelMapper.toDto(level_P2B_PROPOSE_CASHBACK);
        levelDTO_P2B_PROPOSE_CASHBACK_RESP = levelMapper.toDto(level_P2B_PROPOSE_CASHBACK_RESP);
        levelDTO_P2B_WRITEOFF_CASHBACK = levelMapper.toDto(level_P2B_WRITEOFF_CASHBACK);
        levelDTO_P2B_WRITEOFF_CASHBACK_RESP = levelMapper.toDto(level_P2B_WRITEOFF_CASHBACK_RESP);
        levelDTO_P2B_WRITEOFF_CASHBACK_REQUEST = levelMapper.toDto(level_P2B_WRITEOFF_CASHBACK_REQUEST);
        levelDTO_P2B_WRITEOFF_CASHBACK_APPROVE = levelMapper.toDto(level_P2B_WRITEOFF_CASHBACK_APPROVE);
        levelDTO_P2B_WRITEOFF_CASHBACK_DISMISS = levelMapper.toDto(level_P2B_WRITEOFF_CASHBACK_DISMISS);
        levelDTO_P2B_CHARGE_BASKET_CASHBACK = levelMapper.toDto(level_P2B_CHARGE_BASKET_CASHBACK);
        levelDTO_P2B_APPROVE_BASKET_CASHBACK = levelMapper.toDto(level_P2B_APPROVE_BASKET_CASHBACK);
        levelDTO_P2B_CHARGE_COUPON = levelMapper.toDto(level_P2B_CHARGE_COUPON);
        levelDTO_P2B_CHARGE_COUPON_REQUEST = levelMapper.toDto(level_P2B_CHARGE_COUPON_REQUEST);
        levelDTO_P2B_CHARGE_COUPON_RESP = levelMapper.toDto(level_P2B_CHARGE_COUPON_RESP);
        levelDTO_P2B_CHARGE_COUPON_BASKET = levelMapper.toDto(level_P2B_CHARGE_COUPON_BASKET);
        levelDTO_P2B_WRITEOFF_COUPON = levelMapper.toDto(level_P2B_WRITEOFF_COUPON);
        levelDTO_P2B_WRITEOFF_COUPON_SELECT_ACTION = levelMapper.toDto(level_P2B_WRITEOFF_COUPON_SELECT_ACTION);
        levelDTO_P2B_WRITEOFF_COUPON_BASKET = levelMapper.toDto(level_P2B_WRITEOFF_COUPON_BASKET);
        levelDTO_P2B_WRITEOFF_COUPON_REQUEST = levelMapper.toDto(level_P2B_WRITEOFF_COUPON_REQUEST);
        levelDTO_P2B_WRITEOFF_COUPON_RESP = levelMapper.toDto(level_P2B_WRITEOFF_COUPON_RESP);
        levelDTO_P2B_WRITEOFF_COUPON_APPROVE = levelMapper.toDto(level_P2B_WRITEOFF_COUPON_APPROVE);
        levelDTO_P2B_RESP = levelMapper.toDto(level_P2B_RESP);
        levelDTO_B2B = levelMapper.toDto(level_B2B);
        levelDTO_B2NOB = levelMapper.toDto(level_B2NOB);
        levelDTO_DISCARD_NEW_PARTNER = levelMapper.toDto(level_DISCARD_NEW_PARTNER);
        levelDTO_APPROVE_NEW_PARTNER = levelMapper.toDto(level_APPROVE_NEW_PARTNER);
        levelDTO_NEW_GRUPP = levelMapper.toDto(level_NEW_GRUPP);
        levelDTO_DISCARD_NEW_GRUPP = levelMapper.toDto(level_DISCARD_NEW_GRUPP);
        levelDTO_APPROVE_NEW_GRUPP = levelMapper.toDto(level_APPROVE_NEW_GRUPP);
        levelDTO0_1_1 = levelMapper.toDto(level0_1_1);
        levelDTO_RESPONSE_BUYER_MESSAGE = levelMapper.toDto(level_RESPONSE_BUYER_MESSAGE);
        levelDTO_RESPONSE_SHOP_MESSAGE = levelMapper.toDto(level_RESPONSE_SHOP_MESSAGE);
        levelDTO_NON_RESPONSE = levelMapper.toDto(level_NON_RESPONSE);
        levelDTO0_1_3 = levelMapper.toDto(level0_1_3);
        levelDTO0_1_4 = levelMapper.toDto(level0_1_4);
        levelDTO0_1_5 = levelMapper.toDto(level0_1_5);
        levelDTO_EDIT_BUTTON_NAME = levelMapper.toDto(level_EDIT_BUTTON_NAME);
        levelDTO_EDIT_MESSAGE = levelMapper.toDto(level_EDIT_MESSAGE);
        levelDTO_NEW_LEVEL_END_BUTTON = levelMapper.toDto(level_NEW_LEVEL_END_BUTTON);
        levelDTO_NEW_LEVEL_INPUT_BUTTON = levelMapper.toDto(level_NEW_LEVEL_INPUT_BUTTON);
        levelDTO_NEW_LEVEL_BUTTON = levelMapper.toDto(level_NEW_LEVEL_BUTTON);
    }

    private void addFinalButton(Level level) {

        List<ButtonRow> buttonRows = buttonRowRepository.findAllByLevel_Id(level.getId());

        if (buttonRows != null && !buttonRows.isEmpty()) {
            //buttonRowRepository.save(level.getButtonRows());
            buttonRows.forEach(buttonRow -> {
                buttonRowCacheRepository.saveAll(buttonRowMapper.toDto(buttonRow));
            });


            ButtonRow backRow = new ButtonRow(level);
            buttonRowCacheRepository.save(backRow);

            Button backButton = new Button(backRow, Map.of(RU, "В начало"), level_INITIALIZE.getIdString());
            buttonCacheRepository.save(backButton);
            backRow.add(backButton);
            buttonRowCacheRepository.save(backRow);
            //level.addRow(backRow);
        }
        levelCacheRepository.save(level);

        Set<Level> levels = levelRepositoryJPA.findAllByParentLevelId(level.getId());
        for (Level childLevel : levels) {

            if (childLevel.isBotLevel()) {
                if (!level.isBotLevel()) {
                    bot = new Bot(b -> {
                        b.setShop(SHOP);
                        b.setInitialLevel(childLevel.getId());
                    });
                    botRepository.save(bot);
                }
                childLevel.setBot(bot);
            }
            addFinalButton(childLevel);
        }
    }

    public List<Message> retrieveMessages(LevelDTO level) {

//        System.out.println("levewl" + level);
//        System.out.println("retrieveMessages------------------------------level.getId() " + level.getId());
        //List<Message> messageList =
        return messageCacheRepository.findAllByLevel_Id(level.getId());
        //level.setMessages(messageList);
    }

    public List<ButtonRow> retrieveButtonRows(LevelDTO level) {

//        System.out.println("retrieveButtonRows----------------------------level.getId() " + level.getId());
//        System.out.println("buttonRowCacheRepository.findAllByLevel_Id(level.getId()).size()+++++++" + buttonRowCacheRepository.findAllByLevel_Id(level.getId()).size());

        return buttonRowCacheRepository.findAllByLevel_Id(level.getId()).stream()
//                .peek(rowDTO -> System.out.println("row.getId()---" + row.getId()))
//                .peek(rowDTO -> System.out.println("row.getId()+++" + buttonCacheRepository.findByButtonRowId(row.getId())))
//                .peek(rowDTO -> row.getButtonList()
//                        .addAll(buttonCacheRepository.findByButtonRowId(row.getId())))
                .collect(Collectors.toList());
        //level.setButtonRows(buttonRowList);
    }

    public LevelDTOWrapper convertToLevel(LevelDTO level, boolean retrieveMessages, boolean retrieveButtonRows) {
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

        LevelDTO level = levelCacheRepository.findById(levelId);
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


    public LevelDTO cloneLevel(LevelDTO oldLevel, User users) {
        return cloneLevel(oldLevel, users, true, true);
    }


    public LevelDTO cloneLevel(LevelDTO oldLevel, User users, boolean copyButtons, boolean copyMessages) {

        LevelDTO newLevel = new LevelDTO();
        newLevel.setUser(users.getId());
        newLevel.setCallName(oldLevel.getCallName());
        newLevel.setSourceIsMessage(oldLevel.isSourceIsMessage());
        newLevel.setParentLevelId(oldLevel.getParentLevelId());
        newLevel.setBotLevel(oldLevel.isBotLevel());
        newLevel.setTerminateBotLevel(oldLevel.isTerminateBotLevel());
        newLevel.setChatId(users.getChatId());

        levelCacheRepository.save(newLevel);

        if (copyMessages) {
            newLevel.setMessages(retrieveMessages(oldLevel).parallelStream().map(oldMessage -> {
                MessageDTO newMessage = new Message(
                        newLevel,
                        oldMessage.getLevelID(),
                        oldMessage.getNameEN(),
                        oldMessage.getNameRU(),
                        oldMessage.getNameDE(),
                        oldMessage.getImage(),
                        oldMessage.getImageDescription(),
                        oldMessage.getLongitude(),
                        oldMessage.getLatitude());

                messageCacheRepository.save(newMessage);
                return newMessage;
            }).collect(Collectors.toList()));
        }
        //newLevel.childLevels = new ArrayList<>(childLevels);
        System.out.println("copyButtons***" + copyButtons);

        if (copyButtons) {
            newLevel.setButtonRows(retrieveButtonRows(oldLevel).parallelStream().map(oldButtonRow -> {

                ButtonRowDTO newButtonRow = new ButtonRowDTO(newLevel);
                buttonRowCacheRepository.save(newButtonRow);

                System.out.println("oldButtonRow**" + oldButtonRow.getId() + "   at level " + newLevel);

                List<Button> buttonList = oldButtonRow.getButtonList().stream().map(oldButton -> {
                    try {
                        System.out.println("oldButton**" + oldButton.getId() + "+++++callback-" + oldButton.getCallback());

                        ButtonDTO newButton = oldButton.clone(newButtonRow);

                        System.out.println("newButton**" + oldButton.getId() + "+++++callback-" + oldButton.getCallback());

                        buttonCacheRepository.save(newButton);
                        return newButton;
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    return oldButton;
                }).collect(Collectors.toList());

//                newButtonRow.setButtonList(buttonList);
//                buttonRowCacheRepository.save(newButtonRow);

                return newButtonRow;
            }).collect(Collectors.toList()));
        }
        levelCacheRepository.save(newLevel);

        levelCacheRepository.saveAll(newLevel);
        return newLevel;
    }

 */
}
