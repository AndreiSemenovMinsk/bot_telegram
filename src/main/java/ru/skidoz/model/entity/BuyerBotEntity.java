package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class BuyerBotEntity extends AbstractEntity  implements Serializable {

    @NotNull
    @ManyToOne
    private BotEntity bot;

    @NotNull
    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy="buyerBot")
    private List<BuyerBotMessageEntity> buyerBotMessageList = new ArrayList<>();

    public BuyerBotEntity(Consumer<BuyerBotEntity> builder){
        builder.accept(this);
    }

    public BuyerBotEntity() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuyerBotEntity)) return false;
        if (!super.equals(o)) return false;
        BuyerBotEntity action = (BuyerBotEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

