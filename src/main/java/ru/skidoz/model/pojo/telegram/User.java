package ru.skidoz.model.pojo.telegram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.Transient;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.main.Purchase;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.side.Shop;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author andrey.semenov
 */
@Setter
@Getter
public class User extends AbstractDTO {

    private Long expirationTimeMilliseconds;

    private String refreshToken;

    private String accessToken;

    private int refreshCounter;

    @NotNull
    @Size(max=30)
    private String name;
    private String alias;

    @Size(max=50)
    private String email;

    private LanguageEnum language;

    @Size(max=20)
    private String login;

    private char[] password;

    private String sessionId;

//    @JsonIgnore
//    private byte[] runner;

    @Size(max=30)
    private String viber;

    private char[] viberLast;

    private String viberAvatar;

    private Integer currentAdminShop;

    private Integer currentConstructShop;
    private Integer currentConstructShopGroup;
    private Integer currentAddingShopGroup;

    private Long currentConversationShop;
    private Integer currentConversationShopId;

    private Integer currentSearchAbstractGroupEntity;

    private boolean shopOwner;

    private String role;

    @Transient
    private Integer currentRunnerShop;
    private Integer firstRunnerShop;


    private Long chatId;
    private Integer currentLevelBeforeConfigId;

    private Integer/*LevelDTO*/ currentLevelBeforeInterruption;

    private Integer/*BotDTO*/ currentChangingBot;

    private Integer/*ButtonDTO*/ currentChangingButton;

    private Integer/*MessageDTO*/ currentChangingMessage;

    private List<Recommendation> givenRecommendationList = new ArrayList<>();

    private List<Recommendation> takenRecommendationList = new ArrayList<>();

    private List<BuyerBot> buyerBotList = new ArrayList<>();

    private List<Shop> shopList = new ArrayList<>();

    private Shop sellerShop;

    private List<Purchase> purchaseList = new ArrayList<>();

    private List<Basket> basketList = new ArrayList<>();

    private List<Bookmark> bookmarksList = new ArrayList<>();

    private List<Cashback> cashbackList = new ArrayList<>();

    private List<CashbackWriteOff> cashbackWriteOffList = new ArrayList<>();

    private List<ScheduleBuyer> scheduleBuyerList = new ArrayList<>();

    private List<Level> levels = new ArrayList<>();

    public User(Long chatId, String name) {
        super();
        this.chatId = chatId;
        this.name = name;
    }

    public User(Consumer<User> builder) {
        super();
        builder.accept(this);
    }

    public User() {
        super();
    }

    public void addLevel(Level level){
        levels.add(level);
    }


    public void addScheduleBuyer(ScheduleBuyer scheduleBuyer) {
        this.scheduleBuyerList.add(scheduleBuyer);
    }

    public void addGivenRecomendation(Recommendation recommendation) {
        this.givenRecommendationList.add(recommendation);
    }

    public void addTakenRecomendation(Recommendation recommendation) {
        this.takenRecommendationList.add(recommendation);
    }

    public void addPurchase(Purchase purchase) {
        this.purchaseList.add(purchase);
    }

    public void addBasket(Basket basket) {
        this.basketList.add(basket);
    }

    public void addBookmarks(Bookmark bookmark) {
        this.bookmarksList.add(bookmark);
    }

    public void addCashback(Cashback cashback) {
        this.cashbackList.add(cashback);
    }

    public void addCashbackWriteOff(CashbackWriteOff cashbackWriteOff) {
        this.cashbackWriteOffList.add(cashbackWriteOff);
    }

    public void addShop(Shop shop) {
        this.shopList.add(shop);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", login=" + login +
                ", password=" + Arrays.toString(password) +
                ", sessionId='" + sessionId + '\'' +
                ", currentAdminShop=" + currentAdminShop +
                ", currentConstructShop=" + currentConstructShop +
                ", currentConversationShop=" + currentConversationShop +
                ", currentChangingBot=" + currentChangingBot +
                ", currentChangingButton=" + currentChangingButton +
                ", currentChangingMessage=" + currentChangingMessage +
                ", chatId=" + chatId +
                '}';
    }

}
