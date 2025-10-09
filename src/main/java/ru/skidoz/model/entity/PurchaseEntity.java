package ru.skidoz.model.entity;

import ru.skidoz.model.entity.telegram.UserEntity;
import lombok.Data;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "purchase",
        indexes = {
                @Index(name = "IDX_PURCHASE_COL_ID", columnList = "id"),
                @Index(name = "IDX_PURCHASE_COL_USER", columnList = "buyer_id"),
                @Index(name = "IDX_PURCHASE_COL_SHOP", columnList = "shop_id"),
                @Index(name = "IDX_PURCHASE_COL_USER_SHOP", columnList = "buyer_id,shop_id")})
public class PurchaseEntity extends AbstractEntity  implements Serializable {

    @NotNull
    private Integer sum;

    private Integer numberCoupon;

    @NotNull
    @ManyToOne
    private UserEntity buyer;

    @NotNull
    @ManyToOne
    private ShopEntity shop;

    //не OneToOne - потому что может быть несколько акций разных на одну покупку
    @OneToMany(mappedBy="purchase")
    private List<CashbackEntity> cashbackList = new ArrayList<>();

    // OneToOne - потому что может быть только одна дефолтовая акция
    @OneToOne(mappedBy="purchase")
    private PurchaseShopGroupEntity purchaseShopGroup;

    public PurchaseEntity(Consumer<PurchaseEntity> builder){
        builder.accept(this);
    }

    public PurchaseEntity() {
        super();
    }

    public void addCashback(CashbackEntity cashback){
        cashbackList.add(cashback);
    }

    public void increaseNumberCoupon(Integer addNumberCoupon){
        numberCoupon += addNumberCoupon;
    }

    public void decreaseSum(Integer decreaseSum){
        this.sum = this.sum - decreaseSum;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", sum=" + sum +
                ", numberCoupon=" + numberCoupon +
                ", buyer=" + buyer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseEntity)) return false;
        if (!super.equals(o)) return false;
        PurchaseEntity action = (PurchaseEntity) o;
        return super.getId().equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}