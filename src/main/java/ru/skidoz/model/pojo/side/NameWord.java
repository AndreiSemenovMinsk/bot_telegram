package ru.skidoz.model.pojo.side;


import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NameWord extends AbstractDTO {

    private String text;

    public NameWord(Consumer<NameWord> builder) {
        super();
        builder.accept(this);
    }

    public NameWord() {
        super();
    }

}
