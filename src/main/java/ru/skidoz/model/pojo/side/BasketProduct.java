package ru.skidoz.model.pojo.side;

import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BasketProduct extends AbstractDTO {

    private Integer/*BasketDTO*/ basket;
    private Integer/*ProductDTO*/ product;
    private Integer productAmount;
//    @Transient
//    private Integer/*ShopDTO*/ shop;
//    @Transient
//    private Integer/*UserDTO*/ buyer;

    public BasketProduct(Consumer<BasketProduct> builder) {
        super();
        builder.accept(this);
    }

    public BasketProduct() {
        super();
    }
}