package ru.skidoz.model.pojo.side;

import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cashback extends AbstractDTO {

    private Integer radius;
    private Integer/*ShopDTO*/ shop;
    // накапливаем суммы по одной акции
    private Integer/*ActionDTO*/ action;
    private Integer/*PurchaseDTO*/ purchase;
    private Integer/*User*/ user;

    public Cashback(Consumer<Cashback> builder){
        super();
        builder.accept(this);
    }

    public Cashback() {
        super();
    }

    @Override
    public String toString() {
        return "CashbackDTO{" +
                "id=" + super.getId() +
                ", shop=" + shop +
                ", action=" + action +
                ", purchase=" + purchase +
                ", users=" + user +
                ", radius=" + radius +
                '}';
    }
}
