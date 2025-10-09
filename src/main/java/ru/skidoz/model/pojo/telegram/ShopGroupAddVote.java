package ru.skidoz.model.pojo.telegram;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.skidoz.model.pojo.AbstractDTO;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@Setter
@Getter
public class ShopGroupAddVote extends AbstractDTO implements Serializable {

    @NotNull
    private boolean approved = false;

    @NotNull
    private Integer/*Shop Initiator*/ addingInitiatorShop;

    @NotNull
    private Integer/*Shop Requester*/ addingShop;

    @NotNull
    private Integer/*Shop Voter*/ voterShop;

    @NotNull
    private Integer/*ShopGroupDTO*/ shopGroup;

    private int secretCode = ThreadLocalRandom.current().nextInt();


    public ShopGroupAddVote(Consumer<ShopGroupAddVote> builder){
        super();
        builder.accept(this);
    }

    public ShopGroupAddVote() {
        super();
    }

    @Override
    public String toString() {
        return "CashbackWriteOffDTO{" +
                "id=" + getId() +
                ", addingShop=" + addingShop +
                ", voterShop=" + voterShop +
                ", shopGroup=" + shopGroup +
                '}';
    }

}

