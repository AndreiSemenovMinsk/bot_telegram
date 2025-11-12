package ru.skidoz.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.skidoz.model.entity.telegram.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

//@EqualsAndHashCode(callSuper = false)//(callSuper = true)
@Data
@Entity
@Table(name = "shop_group_add_vote",
        uniqueConstraints = {},
        indexes = {})
public class ShopGroupAddVoteEntity extends AbstractEntity  implements Serializable {


    @NotNull
    private boolean approved = false;

    @ManyToOne
    @JoinColumn(name = "adding_initiator_shop_id")
    @NotNull
    private ShopEntity addingInitiatorShop;

    @ManyToOne
    @JoinColumn(name = "adding_shop_id")
    @NotNull
    private ShopEntity addingShop;

    @ManyToOne
    @JoinColumn(name = "voter_shop_id")
    @NotNull
    private ShopEntity voterShop;

    @ManyToOne
    @JoinColumn(name = "shop_group_id")
    @NotNull
    private ShopGroupEntity shopGroup;

    private int secretCode;


    public ShopGroupAddVoteEntity() {
        super();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShopGroupAddVoteEntity shop = (ShopGroupAddVoteEntity) o;
        return super.id.equals(shop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "Shop{" +

                '}';
    }
}
