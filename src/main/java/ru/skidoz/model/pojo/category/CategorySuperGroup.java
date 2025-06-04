package ru.skidoz.model.pojo.category;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.side.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public class CategorySuperGroup extends AbstractGroup implements Cloneable {

    private List<CategoryGroup> categoryGroupSet = new ArrayList<>();

    private List<Product> productSet = new ArrayList<>();

    public CategorySuperGroup(Consumer<CategorySuperGroup> builder){
        super();
        builder.accept(this);
    }

    public CategorySuperGroup() {
        super();
    }

    @Override
    public String toString() {
        return "CategorySuperGroup {" +
                "id=" + super.getId() +
                ", name=" + super.getNameRU() +
                ", Alias=" + super.getAlias() +
                ", categoryGroupSet=" + categoryGroupSet +
                ", productSet=" + productSet +
                '}';
    }

    @Override
    public CategorySuperGroup clone() throws CloneNotSupportedException {
        Object obj = super.clone();

        CategorySuperGroup categorySuperGroup = (CategorySuperGroup) obj;
        categorySuperGroup.setCategoryGroupSet(this.getCategoryGroupSet());
        return categorySuperGroup;
    }
}
