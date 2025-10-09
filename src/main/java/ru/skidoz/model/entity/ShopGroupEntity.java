package ru.skidoz.model.entity;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class ShopGroupEntity extends AbstractEntity  implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private Integer limit;

    @ManyToMany(mappedBy = "shopGroupSet")
    List<ShopEntity> shopSet = new ArrayList<>();

//    @OneToMany(mappedBy="currentConversationShopGroup")
//    private List<ShopEntity> currentConversationShopGroupShopList = new ArrayList<>();


    @OneToMany(mappedBy="shopGroup")
    private List<PurchaseShopGroupEntity> purchaseShopGroupList = new ArrayList<>();

    /*public ShopGroup(Consumer<ShopGroup> builder){
        builder.accept(this);
    }*/

    public ShopGroupEntity(Integer id) {
        super.id = id;
    }

    public ShopGroupEntity() {
        super();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopGroupEntity)) return false;
        if (!super.equals(o)) return false;
        ShopGroupEntity action = (ShopGroupEntity) o;
        return super.getId().equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }

}

