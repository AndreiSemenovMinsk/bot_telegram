package ru.skidoz.model.pojo.telegram;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author andrey.semenov
 */
@Setter
@Getter
public class ButtonRow extends AbstractDTO {

    private Integer/*LevelDTO*/ level;
    private List<Button> buttonList = new ArrayList<>();

    public ButtonRow(Consumer<ButtonRow> builder){
        super();
        builder.accept(this);
    }

    public ButtonRow() {
        super();
    }

    public ButtonRow(Level level) {
        super();
        if (level != null) {
            this.level = level.getId();
        }
    }

    public void add(Button button) {
        buttonList.add(button);
    }

    @Override
    public String toString() {
        return "ButtonRow{" +
                "id=" + getId() +
                ", level=" + level +
                ", buttonList=" + buttonList +
                '}';
    }

}
