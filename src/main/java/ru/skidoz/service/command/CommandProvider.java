package ru.skidoz.service.command;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import ru.skidoz.service.command_impl.Actions;
import ru.skidoz.service.command_impl.AddButton;
import ru.skidoz.service.command_impl.BasketCommand;
import ru.skidoz.service.command_impl.BasketsForShop;
import ru.skidoz.service.command_impl.Bookmarks;
import ru.skidoz.service.command_impl.BasketArchive;
import ru.skidoz.service.command_impl.Cashbacks;
import ru.skidoz.service.command_impl.Connect;
import ru.skidoz.service.command_impl.Initialize;
import ru.skidoz.service.command_impl.GoodsList;
import ru.skidoz.service.command_impl.Languager;
import ru.skidoz.service.command_impl.Partners;
import ru.skidoz.service.command_impl.action.AddActionSource;
import ru.skidoz.service.command_impl.action.basic.*;
import ru.skidoz.service.command_impl.action.coupon.AddActionCoupon;
import ru.skidoz.service.command_impl.action.coupon.CouponNumber;
import ru.skidoz.service.command_impl.action.coupon.CouponRateWithdraw;
import ru.skidoz.service.command_impl.add_goods.*;
import ru.skidoz.service.command_impl.buyer_bot.*;
import ru.skidoz.service.command_impl.communication.basket.cashback.P2BApproveBasketCashback;
import ru.skidoz.service.command_impl.communication.basket.cashback.P2BChargeBasketCashback;
import ru.skidoz.service.command_impl.communication.basket.coupon.P2BChargeCouponBasket;
import ru.skidoz.service.command_impl.communication.basket.coupon.P2BWriteOffCouponBasket;
import ru.skidoz.service.command_impl.communication.manual.cashback.P2BProposeCashbackResp;
import ru.skidoz.service.command_impl.communication.manual.cashback.P2BWriteOffCashbackProposedSum;
import ru.skidoz.service.command_impl.communication.manual.cashback.P2BWriteOffCashbackRequest;
import ru.skidoz.service.command_impl.communication.manual.coupon.*;
import ru.skidoz.service.command_impl.construct_shop.AdminShop;
import ru.skidoz.service.command_impl.construct_shop.ConstructAdd;
import ru.skidoz.service.command_impl.construct_shop.ConstructSarafanShare;
import ru.skidoz.service.command_impl.construct_shop.ConstructSetMinBillShare;
import ru.skidoz.service.command_impl.monitor_price.MonitorPrice;
import ru.skidoz.service.command_impl.monitor_price.MonitorResp;
import ru.skidoz.service.command_impl.search_goods.*;
import ru.skidoz.service.command_impl.search_group.SearchGroup0;
import ru.skidoz.service.command_impl.search_group.SearchGroupLimit4;
import ru.skidoz.service.command_impl.search_group.SearchGroupResp1;
import ru.skidoz.service.command_impl.search_group.SearchGroupResp2;
import ru.skidoz.service.command_impl.search_partner.*;
import ru.skidoz.service.command_impl.sellers.*;
import ru.skidoz.service.command_impl.shop_bot.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommandProvider {

    @Autowired
    private Initialize initialize;
    @Autowired
    private BasketCommand basketCommand;
    @Autowired
    private BasketArchive basketArchive;
    @Autowired
    private Bookmarks bookmarks;
    @Autowired
    private Cashbacks cashbacks;
    @Autowired
    private SearchCommand searchCommand;
    @Autowired
    private SearchResult searchResult;
    @Autowired
    private AddBookmark addBookmark;
    @Autowired
    private AddBasket addBasket;
    @Autowired
    private MyShops myShops;
    @Autowired
    private ShopBots shopBots;
    @Autowired
    private ResponseBuyerMessage responseBuyerMessage;
    @Autowired
    private AddButton addButton;
    /*@Autowired
    private AddTaxiBot addTaxiBot;*/
    @Autowired
    private Connect connect;
    @Autowired
    private ConnectShop connectShop;
    @Autowired
    private EditButtonName editButtonName;
    @Autowired
    private EditMessage editMessage;
    @Autowired
    private NewLevelEndButton newLevelEndButton;
    @Autowired
    private NewLevelInputButton newLevelInputButton;
    @Autowired
    private NewLevelButton newLevelButton;
    @Autowired
    private SendBuyerMessage sendBuyerMessage;
    @Autowired
    private SendShopMessage sendShopMessage;
    @Autowired
    private ResponseShopMessage responseShopMessage;
    @Autowired
    private NonResponse nonResponse;
    @Autowired
    private Actions actions;
    @Autowired
    private AdminShop adminShop;
    @Autowired
    private ConstructSetMinBillShare constructSetMinBillShare;
    @Autowired
    private ConstructSarafanShare constructSarafanShare;
    @Autowired
    private ConstructAdd constructAdd;
    @Autowired
    private BasketsForShop basketsForShop;
    @Autowired
    private GoodsList goodsList;
    @Autowired
    private Partners partners;
    @Autowired
    private Sellers sellers;
    @Autowired
    private SellersAdd sellersAdd;
    @Autowired
    private SellersAddApprove sellersAddApprove;
    @Autowired
    private SellersRemove sellersRemove;
    @Autowired
    private SellersRemoveApprove sellersRemoveApprove;
    @Autowired
    private WithdrawPartner2_1 withdrawPartner2_1;
    @Autowired
    private WithdrawPartnerResp3_1 withdrawPartnerResp3_1;
    @Autowired
    private WithdrawPartnerGroup withdrawPartnerGroup;
    @Autowired
    private WithdrawPartnerEnd4 withdrawPartnerEnd4;
    @Autowired
    private SearchPartner0 searchPartner0;
    @Autowired
    private EditPartner0 editPartner0;
    @Autowired
    private SearchPartnerResp1 searchPartnerResp1;
    @Autowired
    private SearchPartnerResp2 searchPartnerResp2;
    @Autowired
    private SearchPartnerRate3 searchPartnerRate3;
    @Autowired
    private SearchPartnerLimit4 searchPartnerLimit4;
    @Autowired
    private SearchGroup0 searchGroup0;
    @Autowired
    private SearchGroupResp1 searchGroupResp1;
    @Autowired
    private SearchGroupResp2 searchGroupResp2;
    @Autowired
    private SearchGroupLimit4 searchGroupLimit4;
    @Autowired
    private P2BProposeCashbackResp p2BProposeCashbackResp;
    @Autowired
    private P2BWriteOffCashbackRequest p2BWriteOffCashbackRequest;
    @Autowired
    private P2BWriteOffCashbackProposedSum p2BWriteOffCashbackProposedSum;
    @Autowired
    private P2BChargeBasketCashback p2BChargeBasketCashback;
    @Autowired
    private P2BApproveBasketCashback p2BApproveBasketCashback;
    @Autowired
    private P2BWriteOffCouponRequest p2BWriteOffCouponRequest;
    @Autowired
    private P2BWriteOffCouponResp p2BWriteOffCouponResp;
    @Autowired
    private P2BWriteOffCouponApprove p2BWriteOffCouponApprove;
    @Autowired
    private P2BWriteOffCoupon p2BWriteOffCoupon;
    @Autowired
    private P2BWriteOffCouponBasket p2BWriteOffCouponBasket;
    @Autowired
    private P2BChargeCouponBasket p2BChargeCouponBasket;
    @Autowired
    private P2BChargeCoupon p2BChargeCoupon;
    @Autowired
    private P2BChargeCouponRequest p2BChargeCouponRequest;
    @Autowired
    private P2BChargeCouponResp p2BChargeCouponResp;
    @Autowired
    private AddGoods addGoods;
    /*@Autowired
    private AddGoodsName addGoodsName;*/
    @Autowired
    private AddGoodsPhoto addGoodsPhoto;
    @Autowired
    private AddGoodsDescription addGoodsDescription;
    @Autowired
    private AddGoodsPrice addGoodsPrice;
    @Autowired
    private AddGoodsEnd addGoodsEnd;
    @Autowired
    private AddActionBasic addActionBasic;
    @Autowired
    private ActionNameBasic actionNameBasic;
    @Autowired
    private MultiLevelRate multiLevelRate;
    @Autowired
    private MultiLevelRateBasic multiLevelRateBasic;
    @Autowired
    private MultiLevelQuestion multiLevelQuestion;
    @Autowired
    private MultiLevelQuestionBasic multiLevelQuestionBasic;
    @Autowired
    private ActionRateWithdrawBasic actionRateWithdrawBasic;
    @Autowired
    private ActionRatePartner actionRatePartner;
    @Autowired
    private AddActionSource addActionSource;
//    @Autowired
//    private AddActionTarget addActionTarget;
    @Autowired
    private AddActionCoupon addActionCoupon;
    @Autowired
    private CouponNumber couponNumber;
    @Autowired
    private CouponRateWithdraw couponRateWithdraw;
    @Autowired
    private Languager languager;
    @Autowired
    private MonitorResp monitorResp;
    @Autowired
    private MonitorPrice monitorPrice;

    private final Map<String, Command> commands = new HashMap<>();

    @PostConstruct
    private void putCommands(){

        commands.put(CommandName.INITIALIZE.name(), initialize);
        commands.put(CommandName.BASKET.name(), basketCommand);
        commands.put(CommandName.BASKET_ARCHIVE.name(), basketArchive);
        commands.put(CommandName.BOOKMARKS.name(), bookmarks);
        commands.put(CommandName.CASHBACKS.name(), cashbacks);
        commands.put(CommandName.MY_SHOPS.name(), myShops);
        commands.put(CommandName.SHOP_BOTS.name(), shopBots);
        commands.put(CommandName.RESPONSE_BUYER_MESSAGE.name(), responseBuyerMessage);
        commands.put(CommandName.CONNECT.name(), connect);

        commands.put(CommandName.LANGUAGER.name(), languager);

        commands.put(CommandName.CONNECT_SHOP.name(), connectShop);

        commands.put(CommandName.ADD_BUTTON.name(), addButton);
        //commands.put(CommandName.ADD_TAXI_BOT, addTaxiBot);

        commands.put(CommandName.EDIT_BUTTON_NAME.name(), editButtonName);
        commands.put(CommandName.EDIT_MESSAGE.name(), editMessage);

        commands.put(CommandName.NEW_LEVEL_BUTTON.name(), newLevelButton);
        commands.put(CommandName.NEW_LEVEL_INPUT_BUTTON.name(), newLevelInputButton);
        commands.put(CommandName.NEW_LEVEL_END_BUTTON.name(), newLevelEndButton);

        commands.put(CommandName.SEND_BUYER_MESSAGE.name(), sendBuyerMessage);
        commands.put(CommandName.SEND_SHOP_MESSAGE.name(), sendShopMessage);
        commands.put(CommandName.RESPONSE_SHOP_MESSAGE.name(), responseShopMessage);
        commands.put(CommandName.NON_RESPONSE.name(), nonResponse);
        //commands.put(CommandName.ADMIN, new Admin());
        commands.put(CommandName.ADMIN_SHOPS.name(), adminShop);
        commands.put(CommandName.CONSTRUCT_MIN_BILL_SHARE.name(), constructSetMinBillShare);

        commands.put(CommandName.ACTION_RATE_WITHDRAW_BASIC.name(), actionRateWithdrawBasic);

        commands.put(CommandName.CONSTRUCT_SARAFAN_SHARE.name(), constructSarafanShare);
        commands.put(CommandName.CONSTRUCT_ADD.name(), constructAdd);


        commands.put(CommandName.SEARCH.name(), searchCommand);
        commands.put(CommandName.SEARCH_RESULT.name(), searchResult);
        commands.put(CommandName.ADD_BOOKMARK.name(), addBookmark);
        commands.put(CommandName.ADD_BASKET.name(), addBasket);

        commands.put(CommandName.ACTIONS.name(), actions);
        commands.put(CommandName.BASKETS_FOR_SHOP.name(), basketsForShop);
        commands.put(CommandName.GOODS_LIST.name(), goodsList);
        commands.put(CommandName.PARTNERS.name(), partners);
        commands.put(CommandName.WITHDRAW_PARTNER.name(), withdrawPartner2_1);
        commands.put(CommandName.WITHDRAW_PARTNER_RESP.name(), withdrawPartnerResp3_1);
        commands.put(CommandName.WITHDRAW_PARTNER_GROUP.name(), withdrawPartnerGroup);
        commands.put(CommandName.WITHDRAW_PARTNER_END.name(), withdrawPartnerEnd4);

        commands.put(CommandName.SEARCH_PARTNER.name(), searchPartner0);
        commands.put(CommandName.EDIT_PARTNER.name(), editPartner0);
        commands.put(CommandName.SEARCH_PARTNER_RESP.name(), searchPartnerResp1);
        commands.put(CommandName.SEARCH_PARTNER_RESP_BUTTON.name(), searchPartnerResp2);
        commands.put(CommandName.SEARCH_PARTNER_LIMIT.name(), searchPartnerRate3);
        commands.put(CommandName.SEARCH_PARTNER_END.name(), searchPartnerLimit4);

        commands.put(CommandName.SEARCH_GROUP.name(), searchGroup0);
        commands.put(CommandName.SEARCH_GROUP_RESP.name(), searchGroupResp1);
        commands.put(CommandName.SEARCH_GROUP_LIMIT.name(), searchGroupResp2);
        commands.put(CommandName.SEARCH_GROUP_END.name(), searchGroupLimit4);

        commands.put(CommandName.P2B_PROPOSE_CASHBACK_RESP.name(), p2BProposeCashbackResp);
        commands.put(CommandName.P2B_WRITEOFF_CASHBACK_REQUEST.name(), p2BWriteOffCashbackRequest);
        commands.put(CommandName.P2B_WRITEOFF_CASHBACK_APPROVE.name(), p2BWriteOffCashbackProposedSum);
        commands.put(CommandName.P2B_CHARGE_BASKET_CASHBACK.name(), p2BChargeBasketCashback);
        commands.put(CommandName.P2B_APPROVE_BASKET_CASHBACK.name(), p2BApproveBasketCashback);

        commands.put(CommandName.P2B_CHARGE_COUPON.name(), p2BChargeCoupon);
        commands.put(CommandName.P2B_CHARGE_COUPON_BASKET.name(), p2BChargeCouponBasket);
        commands.put(CommandName.P2B_CHARGE_COUPON_REQUEST.name(), p2BChargeCouponRequest);
        commands.put(CommandName.P2B_CHARGE_COUPON_RESP.name(), p2BChargeCouponResp);
        commands.put(CommandName.P2B_WRITEOFF_COUPON.name(), p2BWriteOffCoupon);
        commands.put(CommandName.P2B_WRITEOFF_COUPON_BASKET.name(), p2BWriteOffCouponBasket);
        commands.put(CommandName.P2B_WRITEOFF_COUPON_REQUEST.name(), p2BWriteOffCouponRequest);
        commands.put(CommandName.P2B_WRITEOFF_COUPON_RESP.name(), p2BWriteOffCouponResp);
        commands.put(CommandName.P2B_WRITEOFF_COUPON_APPROVE.name(), p2BWriteOffCouponApprove);



        commands.put(CommandName.ADD_GOODS.name(), addGoods);
        //commands.put(CommandName.ADD_GOODS_NAME, addGoodsName);
        commands.put(CommandName.ADD_GOODS_PHOTO.name(), addGoodsPhoto);
        commands.put(CommandName.ADD_GOODS_DESCRIPTION.name(), addGoodsDescription);
        commands.put(CommandName.ADD_GOODS_PRICE.name(), addGoodsPrice);
        commands.put(CommandName.ADD_GOODS_END.name(), addGoodsEnd);

        commands.put(CommandName.BASIC.name(), addActionBasic);
        commands.put(CommandName.SELECT_LEVEL_TYPE.name(), actionNameBasic);
        commands.put(CommandName.MULTI_LEVEL_RATE.name(), multiLevelRate);
        commands.put(CommandName.MULTI_LEVEL_RATE_BASIC.name(), multiLevelRateBasic);
        commands.put(CommandName.MULTI_LEVEL_QUESTION.name(), multiLevelQuestion);
        commands.put(CommandName.MULTI_LEVEL_QUESTION_BASIC.name(), multiLevelQuestionBasic);
        /*commands.put(CommandName.ONE_LEVEL_RATE.name(), oneLevelRate);
        commands.put(CommandName.ONE_LEVEL_RATE_BASIC.name(), oneLevelRateBasic);*/
        commands.put(CommandName.ACTION_RATE_PARTNER.name(), actionRatePartner);

        commands.put(CommandName.ADD_ACTION_RATE_SOURCE.name(), addActionSource);
        commands.put(CommandName.ADD_ACTION_LINK_SOURCE.name(), addActionSource);
        commands.put(CommandName.ADD_ACTION_COUPON_SOURCE.name(), addActionSource);
        commands.put(CommandName.ADD_ACTION_RATE_TARGET.name(), addActionSource);
        commands.put(CommandName.ADD_ACTION_LINK_TARGET.name(), addActionSource);
        commands.put(CommandName.ADD_ACTION_COUPON_TARGET.name(), addActionSource);

//        commands.put(CommandName.ADD_ACTION_TARGET.name(), addActionTarget);

        commands.put(CommandName.COUPON.name(), addActionCoupon);
        commands.put(CommandName.COUPON_NUMBER.name(), couponNumber);
        commands.put(CommandName.COUPON_RATE_WITHDRAW.name(), couponRateWithdraw);

        commands.put(CommandName.SELLERS.name(), sellers);
        commands.put(CommandName.SELLERS_ADD.name(), sellersAdd);
        commands.put(CommandName.SELLERS_ADD_APPROVE.name(), sellersAddApprove);
        commands.put(CommandName.SELLERS_REMOVE.name(), sellersRemove);
        commands.put(CommandName.SELLERS_REMOVE_APPROVE.name(), sellersRemoveApprove);

        commands.put(CommandName.MONITOR_PRICE.name(), monitorPrice);
        commands.put(CommandName.MONITOR_RESP.name(), monitorResp);
    }

    public void putCommand(String commandName, Command search){

        commands.put(commandName, search);
    }

    public Command getCommand(String commandName) {

/*
        if (commandName.startsWith("@" + RESPONSE_BUYER_MESSAGE.name())){
            commandName = RESPONSE_BUYER_MESSAGE.name();
        }
        if (commandName.startsWith("@" + RESPONSE_SHOP_MESSAGE.name())){
            commandName = RESPONSE_SHOP_MESSAGE.name();
        }*/
        /*if (commandName.startsWith(SUBMIT_BOT.name())){
            commandName = SUBMIT_BOT.name();
        }*/
    	Command command;
        command = commands.get(commandName);

        System.out.println("commandName* " +commandName+ " command* " + command);

        return command;
    }

    public Boolean commandExists(String commandName) {

        return commands.containsKey(commandName);
    }

    public void getAll(){
        commands.forEach((k,v) -> System.out.println("key: "+k+" value:"+v));
    }
}
