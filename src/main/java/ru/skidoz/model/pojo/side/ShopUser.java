package ru.skidoz.model.pojo.side;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skidoz.model.pojo.AbstractDTO;


import java.util.function.Consumer;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ShopUser extends AbstractDTO {

    private Integer shop;
    private Integer user;
    private Integer currentLevelId;

    public ShopUser(Consumer<ShopUser> builder) {
        super();
        builder.accept(this);
    }

    public ShopUser() {
        super();
    }

    public ShopUser(Integer id) {
        super();
        super.setId(id);
    }

}
