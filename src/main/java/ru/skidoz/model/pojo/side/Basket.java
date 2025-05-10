package ru.skidoz.model.pojo.side;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractSideDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Basket extends AbstractSideDTO {

    private String note;
    private Integer shopId;
    private Integer userId;
    @NotNull
    private Boolean temp = false;
    private List<BasketProduct> basketProductList = new ArrayList<>();

    public Basket(Consumer<Basket> builder){
        super();
        builder.accept(this);
    }

    public Basket() {
        super();
    }

//    public Users getUsers() {
//        return usersRepository.findById(users);
//    }


//    public Shop getShop() {
//        return shopRepository.findById(shop);
//    }

    public Integer getShopId() {
        return shopId;
    }
}
