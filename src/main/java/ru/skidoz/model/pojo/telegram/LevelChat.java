package ru.skidoz.model.pojo.telegram;

import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LevelChat extends AbstractDTO {

    private LevelDTOWrapper level;
    private Long chatId;
    private User user;

    public LevelChat(Consumer<LevelChat> builder){
        super();
        builder.accept(this);
    }

    public LevelChat() {
        super();
    }
}
