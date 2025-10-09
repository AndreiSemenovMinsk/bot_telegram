package ru.skidoz.model.pojo.telegram;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import java.util.function.Consumer;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class PartnerGroup extends AbstractDTO implements Serializable {

    @NotNull
    private Integer/*ShopGroup*/ shopGroup;

    @NotNull
    private Integer/*Shop*/ shop;

    // почему нет rate ставки на группу?

    @NotNull
    private String name;

    @NotNull
    private Integer sum;

    public PartnerGroup(Consumer<PartnerGroup> builder){
        super();
        builder.accept(this);
    }

    public PartnerGroup() {
        super();
    }

}

