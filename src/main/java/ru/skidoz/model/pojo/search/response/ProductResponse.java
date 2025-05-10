package ru.skidoz.model.pojo.search.response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.side.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductResponse extends AbstractDTO {

    private List<Product> productResponseDTOS = new ArrayList<>();
    private Integer elementNumber;

    private Integer pageNumber;

    public ProductResponse(Consumer<ProductResponse> builder) {
        super();
        builder.accept(this);
    }

    public ProductResponse() {
        super();
    }
}
