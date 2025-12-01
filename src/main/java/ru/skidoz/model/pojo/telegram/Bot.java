package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Bot extends AbstractDTO implements Serializable {

    private String name = "Default Taxi Bot";

    //TODO make one to one?
    @NotNull
    private Integer shop;

    private Integer botType;

    private List<BuyerBot> buyerBotList = new ArrayList<>();

//    private List<User> currentChangingBotUsersList = new ArrayList<>();

    private Integer initialLevel;

    private boolean isEdited = false;

    //private List<Level> levelList;

    public Bot(Consumer<Bot> builder){
        super();
        builder.accept(this);
    }

    public Bot() {
        super();
    }

    @Override
    public String toString() {
        return "Bot{" +
                "id=" + getId() +
                ", isEdited=" + isEdited +
                ", initialLevel=" + initialLevel +
                //", levelList=" + levelList +
                '}';
    }

}

