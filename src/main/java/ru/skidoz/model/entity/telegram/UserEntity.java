package ru.skidoz.model.entity.telegram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.AbstractEntity;
import ru.skidoz.model.entity.BasketEntity;
import ru.skidoz.model.entity.BookmarkEntity;
import ru.skidoz.model.entity.BotEntity;
import ru.skidoz.model.entity.BuyerBotEntity;
import ru.skidoz.model.entity.CashbackEntity;
import ru.skidoz.model.entity.CashbackWriteOffEntity;
import ru.skidoz.model.entity.PurchaseEntity;
import ru.skidoz.model.entity.RecommendationEntity;
import ru.skidoz.model.entity.ScheduleBuyerEntity;
import ru.skidoz.model.entity.ShopEntity;
import ru.skidoz.model.entity.category.LanguageEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author andrey.semenov
 */
@Entity
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_USER_COL_LOGIN", columnNames = {"login"}),
                @UniqueConstraint(name = "UC_USER_COL_CHAT", columnNames = {"chatId"}),
                @UniqueConstraint(name = "UC_USER_COL_VIBER", columnNames = {"viber"})},
        indexes = {
                //@Index(name = "IDX_USER_COL_ID", columnList = "id"),
                @Index(name = "IDX_USER_COL_LOGIN", columnList = "login"),
                @Index(name = "IDX_USER_COL_CHAT", columnList = "chatId"),
                @Index(name = "IDX_USER_COL_VIBER", columnList = "viber")})
public class UserEntity extends AbstractEntity {

    private Long expirationTimeMilliseconds;

    private String refreshToken;

    private String accessToken;

    @NotNull
    @Size(max=30)
    private String name;

    @Size(max=50)
    private String email;

    private LanguageEnum language;

    @Size(max=20)
    private String login;

    private char[] password;

    private String sessionId;

    @JsonIgnore
    private byte[] runner;

    @Size(max=30)
    private String viber;

    private char[] viberLast;

    private String viberAvatar;

    private Integer currentAdminShop;

    /*@OneToOne(mappedBy = "currentConstructShopUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)*/
    private Integer currentConstructShop;

    /*@OneToOne(mappedBy = "currentConversationShopUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)*/
    private Integer currentConversationShop;

    private Integer currentSearchAbstractGroupEntity;

    private boolean shopOwner;

    private String role;

    private Integer refreshCounter;
    private Integer currentConversationShopId;

    @OneToMany(mappedBy="buyer")
    private List<RecommendationEntity> givenRecommendationList = new ArrayList<>();

    @OneToMany(mappedBy="friend")
    private List<RecommendationEntity> takenRecommendationList = new ArrayList<>();

    @OneToMany(mappedBy="users")
    //@LazyCollection(LazyCollectionOption.FALSE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<BuyerBotEntity> buyerBotList = new ArrayList<>();

    @OneToMany(mappedBy= "adminUser")
    //@LazyCollection(LazyCollectionOption.FALSE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<ShopEntity> shopList = new ArrayList<>();

    /*@ManyToMany(mappedBy = "sellerSet")
    Set<Shop> shopSet = new HashSet<>();*/

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopEntity sellerShop;

    @OneToMany(mappedBy="buyer")
    private List<PurchaseEntity> purchaseList = new ArrayList<>();

    @OneToMany(mappedBy="users")
    private List<BasketEntity> basketList = new ArrayList<>();

    @OneToMany(mappedBy="users")
    private List<BookmarkEntity> bookmarksList = new ArrayList<>();

    @OneToMany(mappedBy="users")
    private List<CashbackEntity> cashbackList = new ArrayList<>();

    @OneToMany(mappedBy="users")
    private List<CashbackWriteOffEntity> cashbackWriteOffList = new ArrayList<>();

    @OneToMany(mappedBy="users")
    private List<ScheduleBuyerEntity> scheduleBuyerList = new ArrayList<>();
/*
    @OneToMany(mappedBy="currentConversationUser")
    private List<Shop> currentConversationBuyerShopList = new ArrayList<>();
*/

    private Long chatId;
    //@ManyToOne(fetch = FetchType.LAZY)
    private Integer currentLevelId;
    private Integer currentLevelBeforeConfigId;
    @ManyToOne
    private LevelEntity currentLevelBeforeInterruption;
    @ManyToOne(fetch = FetchType.LAZY)
    private BotEntity currentChangingBot;
    @ManyToOne(fetch = FetchType.LAZY)
    private ButtonEntity currentChangingButton;
    @ManyToOne(fetch = FetchType.LAZY)
    private MessageEntity currentChangingMessage;


    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<LevelEntity> levels = new ArrayList<>();
/*
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Parameter> parameters = new ArrayList<>();*/

    public UserEntity(Long chatId, String name) {
        this.chatId = chatId;
        this.name = name;
    }

    public UserEntity(Integer id) {
        super.id = id;
    }

    public UserEntity(Consumer<UserEntity> builder){
        builder.accept(this);
    }

    public UserEntity() {

    }

    public void addLevel(LevelEntity level){
        levels.add(level);
    }
/*
    public void addParameter(Parameter parameter){
        parameters.add(parameter);
    }*/


    public void addScheduleBuyer(ScheduleBuyerEntity scheduleBuyer) {
        this.scheduleBuyerList.add(scheduleBuyer);
    }

    public void addGivenRecomendation(RecommendationEntity recommendation) {
        this.givenRecommendationList.add(recommendation);
    }

    public void addTakenRecomendation(RecommendationEntity recommendation) {
        this.takenRecommendationList.add(recommendation);
    }

    public void addPurchase(PurchaseEntity purchase) {
        this.purchaseList.add(purchase);
    }

    public void addBasket(BasketEntity basket) {
        this.basketList.add(basket);
    }

    public void addBookmarks(BookmarkEntity bookmark) {
        this.bookmarksList.add(bookmark);
    }

    public void addCashback(CashbackEntity cashback) {
        this.cashbackList.add(cashback);
    }

    public void addCashbackWriteOff(CashbackWriteOffEntity cashbackWriteOff) {
        this.cashbackWriteOffList.add(cashbackWriteOff);
    }

    public void addShop(ShopEntity shop) {
        this.shopList.add(shop);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login=" + login +
                ", password=" + Arrays.toString(password) +
                ", sessionId='" + sessionId + '\'' +
                ", currentLevelId=" + currentLevelId +
                ", currentAdminShop=" + currentAdminShop +
                ", currentConstructShop=" + currentConstructShop +
                ", currentConversationShop=" + currentConversationShop +
                ", chatId=" + chatId +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        System.out.println("(o instanceof Users)+++++++"+(o instanceof UserEntity));
        System.out.println("super.equals(o)+++++++++++"+super.equals(o));

        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        if (!super.equals(o)) return false;
        UserEntity users = (UserEntity) o;

        System.out.println("super.id+++" + super.id + "+++++users.getId()+" + users.getId());
        System.out.println(super.id.equals(users.getId()));

        return super.id.equals(users.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}
