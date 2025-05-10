package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Partner extends AbstractDTO implements Serializable {

    @NotNull
    private Integer/*Shop*/ creditor;

    @NotNull
    private Integer/*Shop*/ debtor;

    @NotNull
    private BigDecimal lim;

    @NotNull
    private Integer rate;

    @NotNull
    private BigDecimal sum;

    public Partner(Consumer<Partner> builder){
        super();
        builder.accept(this);
    }

    public Partner() {
        super();
    }

}

