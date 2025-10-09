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
public class LinkStarterKeeper extends AbstractDTO implements Serializable {

    private Integer parameter1;

    private Integer parameter2;

    private Integer parameter3;

    private int secretCode = ThreadLocalRandom.current().nextInt();


    public LinkStarterKeeper(Consumer<LinkStarterKeeper> builder){
        super();
        builder.accept(this);
    }

    public LinkStarterKeeper() {
        super();
    }

    @Override
    public String toString() {
        return "LinkStarterKeeper {" +
                "id=" + getId() +
                ", parameter1=" + parameter1 +
                ", parameter2=" + parameter2 +
                ", parameter3=" + parameter3 +
                '}';
    }

}

