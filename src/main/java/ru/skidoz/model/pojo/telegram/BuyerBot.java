package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BuyerBot extends AbstractDTO implements Serializable {

    @NotNull
    private Integer/*BotDTO*/ bot;

    @NotNull
    private Integer/*User*/ user;

    private List<BuyerBotMessage> buyerBotMessageList = new ArrayList<>();

    public BuyerBot(Consumer<BuyerBot> builder) {
        super();
        builder.accept(this);
    }

    public BuyerBot() {
        super();
    }

}

