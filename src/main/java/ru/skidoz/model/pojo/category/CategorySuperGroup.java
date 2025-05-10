package ru.skidoz.model.pojo.category;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.side.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public class CategorySuperGroup extends AbstractGroup implements Cloneable {

    private Set<CategoryGroup> categoryGroupSet = new HashSet<>();

    private Set<Product> productSet = new HashSet<>();

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
