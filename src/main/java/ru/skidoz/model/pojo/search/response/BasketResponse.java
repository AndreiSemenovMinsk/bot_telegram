package ru.skidoz.model.pojo.search.response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.side.Basket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BasketResponse extends AbstractDTO {

    private List<Basket> basketResponseDTOS = new ArrayList<>();
    private Integer elementNumber;

    public BasketResponse(Consumer<BasketResponse> builder) {
        super();
        builder.accept(this);
    }

    public BasketResponse() {
        super();
    }
}
