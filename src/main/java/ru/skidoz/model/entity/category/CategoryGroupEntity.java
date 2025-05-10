package ru.skidoz.model.entity.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import ru.skidoz.model.entity.AbstractGroupEntity;
import ru.skidoz.model.entity.PrdEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

//@Data
@Getter
@Setter
@Entity
public class CategoryGroupEntity extends AbstractGroupEntity implements Serializable {

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "categorySuperGroupId", nullable = false)
    private CategorySuperGroupEntity categorySuperGroup;

    @OneToMany(mappedBy="categoryGroup")
    private Set<CategoryEntity> categorySet = new HashSet<>();

    /*@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "category_group_product",
            joinColumns = @JoinColumn(name = "category_group_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Prd> productSet = new HashSet<>();*/

    @OneToMany(mappedBy="categoryGroup")
    private Set<PrdEntity> productSet = new HashSet<>();

    public CategoryGroupEntity(Consumer<CategoryGroupEntity> builder){
        builder.accept(this);
    }

    public CategoryGroupEntity() {
        super();
    }

    public void addProduct(CategoryEntity product) {
        this.categorySet.add(product);
    }

    @Override
    public String toString() {
        return "CatG{" +
                "id=" + super.getId() +
                ", name=" + super.getAlias() +
                '}';
    }
}

