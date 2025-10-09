package ru.skidoz.model.pojo.main;


import java.time.Instant;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractSideDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class Purchase extends AbstractSideDTO {

    @JsonIgnore
    private Integer chatId;

    private String note;
    private Integer/*ShopDTO*/ shop;
    private Instant time;
    private Integer/*User*/ buyer;
    private Integer sum;
    private Integer initialSum;
    private Integer numberCoupon;
//    private List<Cashback> cashbackList = new ArrayList<>();

    public Purchase(Consumer<Purchase> builder){
        super();
        builder.accept(this);
    }

    public Purchase() {
        super();
    }

}
