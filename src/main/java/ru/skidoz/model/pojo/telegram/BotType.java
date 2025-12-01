package ru.skidoz.model.pojo.telegram;

import lombok.Getter;
import lombok.Setter;
import ru.skidoz.model.pojo.AbstractDTO;
import java.io.Serializable;
import java.util.function.Consumer;

@Setter
@Getter
public class BotType extends AbstractDTO implements Serializable {

    private String name = "Default Taxi Bot";

    private String initialLevelStringId;

    public BotType(Consumer<BotType> builder){
        super();
        builder.accept(this);
    }

    public BotType() {
        super();
    }

    @Override
    public String toString() {
        return "BotType{" +
                "id=" + getId() +
                ", initialLevelStringId=" + initialLevelStringId +
                '}';
    }

}

