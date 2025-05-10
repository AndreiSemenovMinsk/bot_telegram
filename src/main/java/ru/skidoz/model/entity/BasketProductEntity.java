package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class BasketProductEntity extends AbstractEntity implements Serializable {

    @NotNull
    @ManyToOne
    private BasketEntity basket;

    @NotNull
    @ManyToOne
    private PrdEntity product;

    @NotNull
    private Integer productAmount;

    public BasketProductEntity(Consumer<BasketProductEntity> builder){
        builder.accept(this);
    }

    public BasketProductEntity() {

    }

    @Override
    public String toString() {
        return "BasketProduct{" +
                "id=" + id +
                ", productAmount=" + productAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketProductEntity)) return false;
        if (!super.equals(o)) return false;
        BasketProductEntity action = (BasketProductEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

