package ru.skidoz.model.pojo.search.menu;


import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CategoryFilterProduct extends AbstractDTO {

    //from 0 to 1000
    private Integer value;
    private Integer rawValue;
    //    private Product product;
    private Integer product;
    private FilterPoint filterPoint;
    private Integer hashCode;

    public CategoryFilterProduct(Consumer<CategoryFilterProduct> builder) {
        super();
        builder.accept(this);
    }

    public CategoryFilterProduct() {
        super();
    }

    /*@Override
    public boolean equals(Object o) {
        return Objects.equals(hashCode, ((CategoryFilterProduct) o).getHashCode());
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }*/
}
