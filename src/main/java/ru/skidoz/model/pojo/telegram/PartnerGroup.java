package ru.skidoz.model.pojo.telegram;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.Consumer;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class PartnerGroup extends AbstractDTO implements Serializable {

    @NotNull
    private Integer/*ShopGroup*/ debtor;

    @NotNull
    private Integer/*Shop*/ creditor;

    @NotNull
    private BigDecimal lim;
    // почему нет rate ставки на группу?

    @NotNull
    private String name;

    @NotNull
    private BigDecimal sum;

    public PartnerGroup(Consumer<PartnerGroup> builder){
        super();
        builder.accept(this);
    }

    public PartnerGroup() {
        super();
    }

}

