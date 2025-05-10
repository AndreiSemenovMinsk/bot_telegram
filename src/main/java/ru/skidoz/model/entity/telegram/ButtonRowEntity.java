package ru.skidoz.model.entity.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.skidoz.model.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author andrey.semenov
 */
@Entity
@Getter
@Setter
@Table(name = "button_row",
        indexes = {
                //@Index(name = "IDX_BUTTONROW_COL_ID", columnList = "id"),
                @Index(name = "IDX_BUTTONROW_COL_LEVEL", columnList = "level_id")})
public class ButtonRowEntity extends AbstractEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private LevelEntity level;

    @OneToMany(mappedBy = "buttonRow"/*, cascade = CascadeType.MERGE*/, fetch = FetchType.EAGER)
    //@LazyCollection(LazyCollectionOption.FALSE)
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<ButtonEntity> buttonList = new ArrayList<>();

    public ButtonRowEntity(LevelEntity level) {
        this.level = level;
    }

    public ButtonRowEntity() {
        super();
    }

    /*public ButtonRow(Level level, List<Button> buttonList) {
        this.level = level;
        this.buttonList = buttonList;
    }*/

    public void add(ButtonEntity button) {
        buttonList.add(button);
    }

    @Override
    public String toString() {
        return "ButtonRow{" +
                "id=" + id +
                ", buttonList=" + buttonList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ButtonRowEntity)) return false;
        if (!super.equals(o)) return false;
        ButtonRowEntity buttonRow = (ButtonRowEntity) o;
        return super.id.equals(buttonRow.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}
