package ru.skidoz.model.pojo.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;

/**
 * @author andrey.semenov
 */

@Getter
public class LevelDTOWrapper extends AbstractDTO {

    private Level level;

    private List<Message> messages = new ArrayList<>();

    private List<ButtonRow> buttonRows = new ArrayList<>();

    public LevelDTOWrapper(Consumer<LevelDTOWrapper> builder){
        super();
        builder.accept(this);
    }

    public LevelDTOWrapper() {
        super();
    }

    public void addMessage(Message message){
        message.setLevel(super.getId());
        messages.add(message);
    }

    public void addMessage(int position, Message message){
        message.setLevel(super.getId());
        messages.add(position, message);
    }

    public void addRow(ButtonRow buttonRow){

//        System.out.println("LevelDTOWrapper addRow***********" + buttonRow.getId());
//        System.out.println("buttonRow.getLevel()******" + buttonRow.getLevel());

        buttonRow.setLevel(super.getId());
        buttonRows.add(buttonRow);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setMessages(List<Message> messages) {
        this.messages.addAll(messages);
    }

    public List<ButtonRow> getButtonRows() {
        return buttonRows;
    }

    public void setButtonRows(List<ButtonRow> buttonRows) {
        this.buttonRows.addAll(buttonRows);
    }

    @Override
    public String toString() {
        return "LevelDTOWrapper{" +
                "level=" + level +
                ", messages=" + messages +
                ", buttonRows=" + buttonRows +
                '}';
    }
}
