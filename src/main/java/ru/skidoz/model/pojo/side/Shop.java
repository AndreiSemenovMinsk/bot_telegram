package ru.skidoz.model.pojo.side;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Shop extends AbstractDTO {

    //TODO - use only Jackson MAPPERS!!!

    @JsonIgnore
    private Long chatId;

    @ToString.Exclude
    @JsonIgnore
    private byte[] excel;

    private String name;
    private String email;
    private String contacts;
    private String telSms;
    private Double lat;
    private Double lng;
    private String geo;
    private boolean active;

    @ToString.Exclude
    @JsonIgnore
    private String secretHash;
    private Integer initialLevelId;

    private Integer currentConstructShopUser;
    private Long currentConversationShopUserChatId;
    private Integer currentChargeAction;
    private Integer/*Shop*/ currentConversationShop;


    private Integer/*ShopGroup*/ currentConversationShopGroup;
    private Integer/*Prd*/ currentCreatingProduct;
    private Integer/*Action*/ currentCreatingAction;
//    public Action getCurrentCreatingAction() {
//        return actionRepository.findById(currentCreatingAction);
//    }
    @NotNull
    private Integer/*Users*/ adminUser;
    private Integer/*Bot*/ bot;

    private List<Integer/*User*/> sellerSet = new ArrayList<>();

    private Integer sarafanShare;

    private Integer minBillShare;

    private Integer paymentBalance;

    private Integer cashbackBalance;

    private List<Product> productList = new ArrayList<>();

    private List<Integer>/*Action*/ actionList = new ArrayList<>();

    private List<Integer>/*Basket*/ basketList = new ArrayList<>();

    private List<Integer>/*Cashback*/ cashbackList = new ArrayList<>();

    private List<Integer>/*Purchase*/ purchaseList = new ArrayList<>();

    private List<Integer>/*CashbackWriteOff*/ cashbackWriteOffList = new ArrayList<>();

    private List<Integer>/*Partner*/ debtorList = new ArrayList<>();

    private List<Integer>/*Partner*/ creditorList = new ArrayList<>();

//    private List<Integer>/*Shop*/ currentConversationShopShopList = new ArrayList<>();

    private List<Integer>/*PurchaseShopGroup*/ purchaseShopGroupList = new ArrayList<>();

    private List<ShopUser>/*ShopUser*/ shopUserList = new ArrayList<>();

    public Shop(Consumer<Shop> builder) {
        super();
        builder.accept(this);
    }

    public Shop() {
        super();
    }

    public Shop(Integer id) {
        super();
        super.setId(id);
    }

//    public Product getCurrentCreatingProduct() {
//        return productRepository.findById(currentCreatingProduct).orElse(null);
//    }
//    public Integer getCurrentCreatingProductId() {
//        return currentCreatingProduct;
//    }
//    public Shop getCurrentConversationShop() { return shopRepository.findById(currentConversationShop); }
//
//    public Integer getCurrentConversationShopId() { return currentConversationShop; }
}
