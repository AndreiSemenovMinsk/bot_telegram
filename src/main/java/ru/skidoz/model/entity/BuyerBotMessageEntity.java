package ru.skidoz.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@Table(name = "buyer_bot_message")
public class BuyerBotMessageEntity extends AbstractEntity  implements Serializable {

    @NotNull
    @ManyToOne
    private BuyerBotEntity buyerBot;

    @NotNull
    /*@OneToOne(fetch = FetchType.LAZY)
    @MapsId*/
    private Integer/*Level*/ valuableLevel;

    /*@OneToOne(fetch = FetchType.LAZY)
    @MapsId*/
    private Integer/*Message*/ receivedMessage;

    public BuyerBotMessageEntity(Consumer<BuyerBotMessageEntity> builder){
        builder.accept(this);
    }

    public BuyerBotMessageEntity() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuyerBotMessageEntity)) return false;
        if (!super.equals(o)) return false;
        BuyerBotMessageEntity action = (BuyerBotMessageEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

