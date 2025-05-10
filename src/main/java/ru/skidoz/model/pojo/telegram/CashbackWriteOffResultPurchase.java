package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CashbackWriteOffResultPurchase extends AbstractDTO implements Serializable {

    private BigDecimal sum;

    private Integer number;

    @NotNull
    private Integer previousPurchase;

    @NotNull
    private Integer cashbackWriteOff;

    public CashbackWriteOffResultPurchase(Consumer<CashbackWriteOffResultPurchase> builder){
        super();
        builder.accept(this);
    }

    public CashbackWriteOffResultPurchase() {
        super();
    }

    @Override
    public String toString() {
        return "CashbackWriteOffDTO{" +
                "id=" + getId() +
                ", sum=" + sum +
                ", number=" + number +
                '}';
    }

}

