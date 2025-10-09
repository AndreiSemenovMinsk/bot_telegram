package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.side.Shop;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ShopGroup extends AbstractDTO implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private Integer limit;

    List<Integer> shopSet = new ArrayList<>();

    public ShopGroup(Consumer<ShopGroup> builder){
        super();
        builder.accept(this);
    }

    public ShopGroup() {
        super();
    }

}

