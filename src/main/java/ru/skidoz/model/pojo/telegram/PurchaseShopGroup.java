package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseShopGroup extends AbstractDTO implements Serializable {

    @NotNull
    private boolean manual;

    @NotNull
    private Integer/*ShopDTO*/ shop;

    @NotNull
    private Integer/*purchaseDTO*/ purchase;

    @NotNull
    private Integer/*ShopGroupDTO*/ shopGroup;

    @NotNull
    private Integer/*User*/ user;

    @NotNull
    private int sum;

    public PurchaseShopGroup(Consumer<PurchaseShopGroup> builder){
        super();
        builder.accept(this);
    }

    public PurchaseShopGroup() {
        super();
    }

//    public User getUsers() {
//        return usersRepository.findById(users);
//    }
//    public Action getAction() {
//        return actionRepository.findById(action);
//    }
//    public Shop getShop() {
//        return shopRepository.findById(shop);
//    }

    @Override
    public String toString() {
        return "CashbackWriteOffDTO{" +
                "id=" + getId() +
                ", shop=" + shop +
                ", users=" + user +
                '}';
    }

}

