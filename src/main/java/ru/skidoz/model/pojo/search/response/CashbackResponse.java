package ru.skidoz.model.pojo.search.response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.side.Cashback;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CashbackResponse extends AbstractDTO {

    private List<Cashback> cashbackResponseDTOS = new ArrayList<>();
    private Integer elementNumber;

    public CashbackResponse(Consumer<CashbackResponse> builder) {
        super();
        builder.accept(this);
    }

    public CashbackResponse() {
        super();
    }
}
