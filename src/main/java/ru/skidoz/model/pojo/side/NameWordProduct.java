package ru.skidoz.model.pojo.side;


import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NameWordProduct extends AbstractDTO {

    private Integer nameWord;

    private Integer product;

    public NameWordProduct(Consumer<NameWordProduct> builder) {
        super();
        builder.accept(this);
    }

    public NameWordProduct() {
        super();
    }

}
