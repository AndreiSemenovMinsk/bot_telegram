package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

//@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@Entity
@Table(name = "basket")
public class BasketEntity extends AbstractEntity implements Serializable {

    @NotNull
    private String note;

    @NotNull
    @ManyToOne
    private ShopEntity shop;

    @NotNull
    private Boolean temp = false;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "buyerId", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "basket", orphanRemoval = true)
    private List<BasketProductEntity> basketProductList = new ArrayList<>();

    /*@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "basket_product",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Prd> productSet = new HashSet<>();*/

    public BasketEntity(Consumer<BasketEntity> builder) {
        builder.accept(this);
    }

    public BasketEntity(Integer id) {
        super.id = id;
    }

    public BasketEntity() {
        super();
    }

    public void addBasketProduct(BasketProductEntity basketProduct) {
        basketProductList.add(basketProduct);
    }

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", temp='" + temp + '\'' +
                ", users='" + user.getId() + '\'' +
                ", shop='" + shop.getId() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketEntity)) return false;
        if (!super.equals(o)) return false;
        BasketEntity action = (BasketEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

