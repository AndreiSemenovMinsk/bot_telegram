package ru.skidoz.model.pojo.main;

import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Buyer extends AbstractDTO {

    public Buyer(Consumer<Buyer> builder){
        super();
        builder.accept(this);
    }

    public Buyer() {
        super();
    }
}
