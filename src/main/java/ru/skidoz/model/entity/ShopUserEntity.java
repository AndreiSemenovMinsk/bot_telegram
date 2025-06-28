package ru.skidoz.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.skidoz.model.entity.telegram.UserEntity;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "shop_user",
        indexes = { @Index(name = "IDX_SHOP_USER", columnList = "user_id,shop_id")})
public class ShopUserEntity extends AbstractEntity  implements Serializable {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ShopEntity shop;

    public ShopUserEntity() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShopUserEntity shop = (ShopUserEntity) o;
        return super.id.equals(shop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "ShopUser {" +
                "id=" + id +
                ", user=" + user +
                ", shop=" + shop +
                '}';
    }
}
