package ru.skidoz.model.pojo.main;

import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserShort extends AbstractDTO {

    private char[] sessionId;

    public UserShort(Consumer<UserShort> builder){
        super();
        builder.accept(this);
    }

    public UserShort() {
        super();
    }
}
