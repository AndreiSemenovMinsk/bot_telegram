package ru.skidoz.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "partner",
        uniqueConstraints = @UniqueConstraint(
                name = "partnerConstraint",
                columnNames = {"creditorId", "debtorId"}))
@AllArgsConstructor
public class PartnerEntity extends AbstractEntity  implements Serializable {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creditorId", nullable = false)
    private ShopEntity creditor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "debtorId", nullable = false)
    private ShopEntity debtor;

    @NotNull
    private BigDecimal lim;

    @NotNull
    private Integer rate;

    @NotNull
    private BigDecimal sum;

    public PartnerEntity(Consumer<PartnerEntity> builder){
        builder.accept(this);
    }

    public PartnerEntity() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartnerEntity)) return false;
        if (!super.equals(o)) return false;
        PartnerEntity action = (PartnerEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }

    @Override
    public String toString() {
        return "Partner{" +
                "id=" + id +
                ", creditor=" + creditor.getId() +
                ", debtor=" + debtor.getId() +
                ", lim=" + lim +
                ", rate=" + rate +
                ", sum=" + sum +
                '}';
    }
}

