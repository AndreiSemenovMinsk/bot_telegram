package ru.skidoz.model.entity;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

//@EqualsAndHashCode(callSuper = false)//(callSuper = true)
@Data
@Entity
@Table(name = "shop",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_SHOP_COL_NAME", columnNames = {"name"}),
                @UniqueConstraint(name = "UC_SHOP_COL_LOGIN", columnNames = {"login"})},
        indexes = {
                //@Index(name = "IDX_SHOP_COL_ID", columnList = "id"),
                @Index(name = "IDX_SHOP_NAME_USER", columnList = "admin_user_id,name"),
                @Index(name = "IDX_SHOP_NAME", columnList = "name"),
                @Index(name = "IDX_SHOP_LAT", columnList = "lat"),
                @Index(name = "IDX_SHOP_LNG", columnList = "lng")})
public class ShopEntity extends AbstractEntity  implements Serializable {

    @NotNull
    @Size(max=30)
    private String name;

    @NotNull
    private Long chatId;

    //@OneToOne
    private Integer currentConstructShopUser;

    //@OneToOne
    private Long currentConversationShopUserChatId;

    private Integer currentChargeAction;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private ShopEntity currentConversationShop;
    private Integer currentConversationShop;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ShopGroupEntity currentConversationShopGroup;
    private Integer currentConversationShopGroup;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ProductEntity currentCreatingProduct;
    private Integer currentCreatingProduct;
    //@ManyToOne(fetch = FetchType.LAZY)
//    private ActionEntity currentCreatingAction;
    private Integer currentCreatingAction;

    private Integer initialLevelId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity adminUser;

    /*@ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "users_shop_set",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_user_id")
    )*/
    @OneToMany(mappedBy="sellerShop")
    private List<UserEntity> sellerSet = new ArrayList<>();

    @JsonIgnore
    private String secretHash;

    @Lob
    @JsonIgnore
    @Column(name = "excel"/*, columnDefinition="BYTEA"*/)
    private byte[] excel;

    @Lob
    @JsonIgnore
    private byte[] contractPDF;

    //@NotNull
    @Size(max=20)
    private String login;
    @JsonIgnore
    //@NotNull
    private char[] password;

    //@NotNull
    private String email;

    //@NotNull
    private String contacts;

    //@NotNull
    private String telSms;

    private Double lat;

    private Double lng;

    private String geo;

    //@NotNull
    private Integer sarafanShare;

    //@NotNull
    private Integer minBillShare;

    //@NotNull
    private Integer paymentBalance;

    //@NotNull
    private Integer cashbackBalance;

    @OneToOne
    private BotEntity bot;

    @OneToMany(mappedBy="shop")
    private List<ProductEntity> productList = new ArrayList<>();

    @OneToMany(mappedBy="shop")
    private List<ActionEntity> actionList = new ArrayList<>();

    /*@OneToMany(mappedBy="shop")
    private List<Bot> botList = new ArrayList<>();*/
    @OneToMany(mappedBy="shop")
    private List<BasketEntity> basketList = new ArrayList<>();

    @OneToMany(mappedBy="shop")
    private List<CashbackEntity> cashbackList = new ArrayList<>();

    @OneToMany(mappedBy="shop")
    private List<PurchaseEntity> purchaseList = new ArrayList<>();

    @OneToMany(mappedBy="shop")
    private List<CashbackWriteOffEntity> cashbackWriteOffList = new ArrayList<>();

//    @OneToMany(mappedBy="currentConversationShop")
//    private List<ShopEntity> currentConversationShopShopList = new ArrayList<>();



    @OneToMany(mappedBy="shop")
    private List<PurchaseShopGroupEntity> purchaseShopGroupList = new ArrayList<>();



    private boolean active;
/*
    @OneToMany(mappedBy="currentConversationShop")
    private List<Users> currentConversationShopUserList = new ArrayList<>();
*/
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "shop_group_shop_set",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_group_id")
    )
    private List<ShopGroupEntity> shopGroupSet = new ArrayList<>();


    @OneToMany(mappedBy="shop")
    private List<RecommendationEntity> recommendationList = new ArrayList<>();

    public ShopEntity() {
        super();
    }


    public String getSecretHash() {
        return secretHash;
    }

    public void setSecretHash(String secretHash) {
        this.secretHash = secretHash;
    }

    //private Shablon shablon;

    //private DiscountCart;

    public ShopEntity(Consumer<ShopEntity> builder){
        builder.accept(this);
    }

    public ShopEntity(Integer id){
        super.id = id;
    }

    public void addProduct(ProductEntity product) {
        this.productList.add(product);
    }

    public void addCashback(CashbackEntity cashback) {
        this.cashbackList.add(cashback);
    }

    public void addCashbackWriteOff(CashbackWriteOffEntity cashbackWriteOff) {
        this.cashbackWriteOffList.add(cashbackWriteOff);
    }

    public void addRecomendation(RecommendationEntity recommendation) {
        this.recommendationList.add(recommendation);
    }

    public void addPurchase(PurchaseEntity purchase) {
        this.purchaseList.add(purchase);
    }
/*
    public void addBot(Bot bot) {
        this.botList.add(bot);
    }*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShopEntity shop = (ShopEntity) o;
        return super.id.equals(shop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentConstructShopUser=" + currentConstructShopUser +
                ", currentConversationShopUser=" + currentConversationShopUserChatId +
                ", email='" + email + '\'' +
                ", contacts='" + contacts + '\'' +
                ", telSms='" + telSms + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", sarafanShare=" + sarafanShare +
                ", minBillShare=" + minBillShare +
                ", paymentBalance=" + paymentBalance +
                ", cashbackBalance=" + cashbackBalance +
                '}';
    }
}
