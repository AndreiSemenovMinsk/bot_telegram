package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "cashback",
        indexes = {
                @Index(name = "IDX_CASHBACK_COL_ID", columnList = "id"),
                @Index(name = "IDX_CASHBACK_COL_USER", columnList = "users_id"),
                @Index(name = "IDX_CASHBACK_COL_SHOP", columnList = "shop_id"),
                @Index(name = "IDX_CASHBACK_COL_USER_SHOP", columnList = "users_id,shop_id")})
public class CashbackEntity extends AbstractEntity  implements Serializable {

    private Integer radius;

    //не OneToOne - потому что может быть несколько акций разных на одну покупку
    @NotNull
    @ManyToOne
    private PurchaseEntity purchase;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "buyerId", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "shopId", nullable = false, insertable = false, updatable = false)
    private ShopEntity shop;

    @NotNull
    @ManyToOne
    private ActionEntity action;

    public CashbackEntity(Consumer<CashbackEntity> builder){
        builder.accept(this);
    }

    public CashbackEntity() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashbackEntity)) return false;
        if (!super.equals(o)) return false;
        CashbackEntity action = (CashbackEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

