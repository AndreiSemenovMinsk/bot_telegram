package ru.skidoz.model.entity;

import java.io.Serializable;

import java.util.Objects;
import java.util.function.Consumer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "partner_group",
        uniqueConstraints = @UniqueConstraint(
                name = "partnerGroupConstraint",
                columnNames = {"shopGroupId", "shopId"}))
public class PartnerGroupEntity extends AbstractEntity  implements Serializable {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "shopId", nullable = false)
    private ShopEntity shop;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "shopGroupId", nullable = false)
    private ShopGroupEntity shopGroup;
    // почему нет rate ставки на группу?

    @NotNull
    private String name;

    @NotNull
    private Integer sum;

    public PartnerGroupEntity(Consumer<PartnerGroupEntity> builder){
        builder.accept(this);
    }

    public PartnerGroupEntity() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartnerGroupEntity)) return false;
        if (!super.equals(o)) return false;
        PartnerGroupEntity action = (PartnerGroupEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }

    @Override
    public String toString() {
        return "PartnerGroup{" +
                "id=" + id +
                ", shop=" + shop.getId() +
                ", shopGroup=" + shopGroup.getId() +
                ", name='" + name + '\'' +
                ", sum=" + sum +
                '}';
    }
}

