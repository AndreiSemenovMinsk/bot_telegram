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
@Table(name = "purchase_shop_group",
        indexes = {
                @Index(name = "IDX_PURCHASE_SHOP_GROUP_COL_ID", columnList = "id"),
                @Index(name = "IDX_PURCHASE_SHOP_GROUP_COL_USER", columnList = "user_id"),
                @Index(name = "IDX_PURCHASE_SHOP_GROUP_COL_SHOP", columnList = "shop_group_id"),
                @Index(name = "IDX_PURCHASE_SHOP_GROUP_COL_USER_SHOP", columnList = "user_id,shop_group_id")})
public class PurchaseShopGroupEntity extends AbstractEntity  implements Serializable {

    // OneToOne - потому что может быть только одна дефолтовая акция
    @NotNull
    @OneToOne
    private PurchaseEntity purchase;

    @NotNull
    private boolean manual;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "buyerId", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "shopId", nullable = false, insertable = false, updatable = false)
    private ShopGroupEntity shopGroup;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "shopId", nullable = false, insertable = false, updatable = false)
    private ShopEntity shop;

    public PurchaseShopGroupEntity(Consumer<PurchaseShopGroupEntity> builder){
        builder.accept(this);
    }

    public PurchaseShopGroupEntity() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseShopGroupEntity)) return false;
        if (!super.equals(o)) return false;
        PurchaseShopGroupEntity action = (PurchaseShopGroupEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

