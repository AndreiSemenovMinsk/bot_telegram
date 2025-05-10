package ru.skidoz.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class CashbackWriteOffEntity extends AbstractEntity  implements Serializable {

    private BigDecimal sum;

    private Integer numberCoupon;

    @NotNull
    private boolean approved;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "shopId", nullable = false, insertable = false, updatable = false)
    private ShopEntity shop;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "buyerId", nullable = false, insertable = false, updatable = false)
    private UserEntity user;


//    @ManyToOne(optional = true)
//    private Action action;

    public CashbackWriteOffEntity(Consumer<CashbackWriteOffEntity> builder){
        builder.accept(this);
    }

    public CashbackWriteOffEntity() {

    }

    @Override
    public String toString() {
        return "CashbackWriteOff{" +
                "id=" + id +
                ", sum=" + sum +
                ", numberCoupon=" + numberCoupon +
                ", approved=" + approved +
                ", shop=" + shop.getId() +
                ", users=" + user.getId() +
//                ", action=" + action.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashbackWriteOffEntity)) return false;
        if (!super.equals(o)) return false;
        CashbackWriteOffEntity action = (CashbackWriteOffEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

