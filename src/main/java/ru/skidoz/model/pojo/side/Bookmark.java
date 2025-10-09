package ru.skidoz.model.pojo.side;


import java.time.Instant;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractSideDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Bookmark extends AbstractSideDTO {

    private Integer radius;
    private boolean notificationSent = false;
    private boolean viberNotify;
    private Integer/*ProductDTO*/ product;
    private Integer/*ShopDTO*/ shop;
    private Integer/*ShopDTO*/ user;
    private Instant lastNotification;
    private boolean priceUpdated = false;
    private Integer bidPrice;
    private Instant notification;
    private boolean priceStrike = false;

    public Bookmark(Consumer<Bookmark> builder){
        super();
        builder.accept(this);
    }

    public Bookmark() {
        super();
    }

    @Override
    public String toString() {
        return "BookmarkDTO{" +
                "id=" + super.getId() +
                ", radius=" + radius +
                ", viberNotify=" + viberNotify +
                ", time=" + super.getTime() +
                ", product=" + product +
                ", shop=" + shop +
                '}';
    }
}
