package ru.skidoz.model.entity.category;

import ru.skidoz.model.entity.AbstractGroupEntity;
import ru.skidoz.model.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import jakarta.persistence.*;


//@Data
@Getter
@Setter
@Entity
//@Table(name = "category_super_group", uniqueConstraints=@UniqueConstraint(name="UC_categorySuperGroup_name", columnNames={"name"}))
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CategorySuperGroupEntity extends AbstractGroupEntity implements Serializable {

    @OneToMany(mappedBy="categorySuperGroup")
    private List<CategoryGroupEntity> categoryGroupSet = new ArrayList<>();

/*
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "category_super_group_product",
            joinColumns = @JoinColumn(name = "category_super_group_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Prd> productSet = new HashSet<>();*/

    @OneToMany(mappedBy="categorySuperGroup")
    private List<ProductEntity> productSet = new ArrayList<>();

    public CategorySuperGroupEntity(Consumer<CategorySuperGroupEntity> builder){
        builder.accept(this);
    }

    public CategorySuperGroupEntity() {
        super();
    }

    public void addProduct(CategoryGroupEntity categoryGroup) {
        this.categoryGroupSet.add(categoryGroup);
    }

    public List<ProductEntity> getProductSet() {
        return productSet;
    }

    public void setProductSet(List<ProductEntity> productSet) {
        this.productSet = productSet;
    }

    @Override
    public String toString() {
        return "CatSG{" +
                "id=" + super.getId() +
                '}';
    }
}

