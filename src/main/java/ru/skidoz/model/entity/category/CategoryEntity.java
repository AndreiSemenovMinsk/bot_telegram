package ru.skidoz.model.entity.category;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.entity.AbstractGroupEntity;
import ru.skidoz.model.entity.FilterPointEntity;
import ru.skidoz.model.entity.ProductEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class CategoryEntity extends AbstractGroupEntity implements Serializable {

    @NotNull
    private boolean actual;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "categoryGroupId", nullable = false)
    private CategoryGroupEntity categoryGroup;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "categoryGroupId", nullable = false)
    private CategorySuperGroupEntity categorySuperGroup;


    @ManyToOne
    //@JoinColumn(name = "categoryGroupId", nullable = false)
    private CategoryEntity parentCategory;

    @OneToMany(mappedBy="parentCategory")
    private List<CategoryEntity> childCategorySet = new ArrayList<>();

    /*@ManyToMany
    @JoinTable(name = "category_product",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Prd> productSet = new HashSet<>();*/

    @OneToMany(mappedBy="category")
    private List<ProductEntity> productSet = new ArrayList<>();

/*
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "category_filter_point",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "filter_point_id")
    )
    private Set<FilterPoint> filterPointSet = new HashSet<>();
*/

    @OneToMany(mappedBy="category")
    private List<FilterPointEntity> filterPointList = new ArrayList<>();

    public CategoryEntity(Consumer<CategoryEntity> builder){
        super();
        builder.accept(this);
    }

    public CategoryEntity() {
        super();
    }

    public void addProduct(ProductEntity product) {
        this.productSet.add(product);
    }

    public void addFilterPoint(FilterPointEntity filterPoint) {
        this.filterPointList.add(filterPoint);
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + super.getId() +
//                ", name=" + super.getName() +
                '}';
    }
}

