package ru.skidoz.model.entity.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.skidoz.model.entity.AbstractEntity;
import ru.skidoz.model.entity.category.LanguageEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author andrey.semenov
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "button",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_BUTTON_COL_USER_CALLBACK", columnNames = {"button_row_id", "callback"})},
        indexes = {
                //@Index(name = "IDX_BUTTON_COL_ID", columnList = "id"),
                @Index(name = "IDX_BUTTON_COL_BUTTONROW", columnList = "button_row_id")})
public class ButtonEntity extends AbstractEntity {

    @ManyToOne
    private ButtonRowEntity buttonRow;

    @Size(max = 50)
    private String nameEN;

    @Size(max = 50)
    private String nameRU;

    @Size(max = 50)
    private String nameDE;

    /*@ManyToOne
    private Level level;*/
//    @Size(max=50)
//    private String alias;

/*    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "i18n_Button", foreignKey = @ForeignKey(name = "fk_i18n_Button"), joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "locale")
    @Column(name = "text")
    private Map<LanguageEnum, String> nameLanguages = new HashMap<>();*/

    @Size(max = 60)
    private String callback;

    private Boolean display = true;

    private String webApp;

//    @OneToMany(mappedBy = "currentChangingButton", fetch = FetchType.LAZY)
//    private List<UserEntity> usersWithCurrentChangingButtonList = new ArrayList<>();

    public ButtonEntity(ButtonRowEntity buttonRow, Map<LanguageEnum, String> nameLanguages, String callback) {
        this.buttonRow = buttonRow;
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
        this.callback = callback;
    }

    public ButtonEntity(ButtonRowEntity buttonRow, Map<LanguageEnum, String> nameLanguages, String callback, String webApp) {
        this.webApp = webApp;
        this.buttonRow = buttonRow;
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
        this.callback = callback;
    }

    public ButtonEntity() {
        super();
    }

    public ButtonEntity clone(ButtonRowEntity buttonRow) throws CloneNotSupportedException {
        ButtonEntity button = new ButtonEntity();//(Button) super.clone();
        button.callback = this.callback;
        button.nameEN = this.nameEN;
        button.nameRU = this.nameRU;
        button.nameDE = this.nameDE;
        button.buttonRow = buttonRow;
        return button;
    }

    public void addName(String name, LanguageEnum language) {
        if (LanguageEnum.EN.equals(language)) {
            this.nameEN = name;
        } else if (LanguageEnum.RU.equals(language)) {
            this.nameRU = name;
        } else if (LanguageEnum.DE.equals(language)) {
            this.nameDE = name;
        }
    }

    public String getName(LanguageEnum language) {
        if (LanguageEnum.EN.equals(language)) {
            return this.nameEN;
        } else if (LanguageEnum.RU.equals(language)) {
            return this.nameRU;
        } else if (LanguageEnum.DE.equals(language)) {
            return this.nameDE;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Button{" +
                "id=" + id +
                ", callback='" + callback + '\'' +
                ", this.nameRU='" + this.nameRU + '\'' +
                ", display='" + display + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ButtonEntity)) return false;
        if (!super.equals(o)) return false;
        ButtonEntity button = (ButtonEntity) o;
        return super.id.equals(button.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}
