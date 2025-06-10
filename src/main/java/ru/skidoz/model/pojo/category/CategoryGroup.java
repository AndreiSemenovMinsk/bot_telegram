package ru.skidoz.model.pojo.category;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.entity.ProductEntity;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public class CategoryGroup extends AbstractGroup implements Cloneable {

    @NotNull
    private Integer/*CategorySuperGroupDTO*/ categorySuperGroup;

    private List<Category> categorySet = new ArrayList<>();

    private List<ProductEntity> productSet = new ArrayList<>();

    public CategoryGroup(Consumer<CategoryGroup> builder){
        super();
        builder.accept(this);
    }

    public CategoryGroup() {
        super();
    }

    @Override
    public String toString() {
        return "CategoryGroupDTO {" +
                " id=" + super.getId() +
                ", name=" + super.getNameRU() +
                ", super.getAlias=" + super.getAlias() +
                ", Alias=" + getAlias() +
                ", categorySet=" + categorySet +
                ", productSet=" + productSet +
                '}';
    }


    @Override
    public CategoryGroup clone() throws CloneNotSupportedException {
        Object obj = super.clone();

        CategoryGroup categoryGroup = (CategoryGroup) obj;
        categoryGroup.setCategorySet(this.getCategorySet());
        return categoryGroup;
    }
}
