package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.util.function.Consumer;

import ru.skidoz.model.entity.AbstractEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BuyerBotMessage extends AbstractEntity implements Serializable {

    @NotNull
    private Integer/*BuyerBot*/ buyerBot;
    @NotNull
    private Integer/*Level*/ valuableLevel;

    private Integer/*Message*/ receivedMessage;

    public BuyerBotMessage(Consumer<BuyerBotMessage> builder) {
        super();
        builder.accept(this);
    }

    public BuyerBotMessage() {
        super();
    }

}

