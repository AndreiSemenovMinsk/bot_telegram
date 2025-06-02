package ru.skidoz.model.pojo.category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.search.menu.FilterPoint;
import ru.skidoz.model.pojo.side.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public class Category extends AbstractGroup implements Cloneable {

    @NotNull
    //@JoinColumn(name = "categoryGroupId", nullable = false)
    private Integer/*CatG*/ categoryGroup;

    @NotNull
    //@JoinColumn(name = "categoryGroupId", nullable = false)
    private Integer/*CatG*/ categorySuperGroup;

    //@JoinColumn(name = "categoryGroupId", nullable = false)
    private  Integer/*Cat*/ parentCategory;

//    @NotNull
//    @Size(max=50)
//    private String alias;

    @NotNull
    private boolean actual;

    private Set<Category> childCategorySet = new HashSet<>();

    @JsonIgnore
    private Set<Product> productSet = new HashSet<>();

    @JsonIgnore
    private List<FilterPoint> filterPointList = new ArrayList<>();

    public Category(Consumer<Category> builder){
        super();
        builder.accept(this);
    }

    public Category() {
        super();
    }

    /*public CategoryGroup getCategoryGroup() {

        System.out.println("categoryGroupRepository   +++++++++++++" + categoryGroupRepository + " categoryGroup" + categoryGroup);

        return categoryGroupRepository.findById(categoryGroup);
    }*/

    public Integer getCategoryGroupId() {
        return categoryGroup;
    }

//    public Category getParentCategory() {
//        return categoryRepository.findById(parentCategory);
//    }

    public Integer getParentCategoryId() {
        return parentCategory;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                " id=" + super.getId() +
                ", name=" + super.getName() +
                ", alias=" + super.getAlias() +
                ", productSet=" + productSet +
                '}';
    }

    @Override
    public Category clone() throws CloneNotSupportedException {
        Object obj = super.clone();

        Category category = (Category) obj;
        category.setChildCategorySet(this.getChildCategorySet());
        return category;
    }
}
