package ru.skidoz.model.entity;

import ru.skidoz.model.entity.telegram.LevelEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
public class BotEntity extends AbstractEntity  implements Serializable {

    private String name = "Default Taxi Bot";

    //TODO make one to one?
    @NotNull/*@ManyToOne*/
    @OneToOne
    private ShopEntity shop;

    @OneToMany(mappedBy="bot")
    private List<BuyerBotEntity> buyerBotList = new ArrayList<>();

    @OneToMany(mappedBy="currentChangingBot", fetch = FetchType.LAZY)
    private List<UserEntity> currentChangingBotUsersList = new ArrayList<>();
/*
    @OneToMany(mappedBy="bot")
    private List<BuyerBotMessage> buyerBotMessageList;*/

    /*@OneToOne
    @MapsId
    @JoinColumn(name = "level_initial_id")*/
    private Integer initialLevel;

    @OneToMany(mappedBy="bot", fetch = FetchType.LAZY)
    private List<LevelEntity> levelList;

    public BotEntity(Consumer<BotEntity> builder){
        builder.accept(this);
    }

    public BotEntity() {

    }

    @Override
    public String toString() {
        return "Bot{" +
                "id=" + id +
                //", buyerBotList=" + buyerBotList +
                ", initialLevel=" + initialLevel +
                //", levelList=" + levelList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BotEntity)) return false;
        if (!super.equals(o)) return false;
        BotEntity action = (BotEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

