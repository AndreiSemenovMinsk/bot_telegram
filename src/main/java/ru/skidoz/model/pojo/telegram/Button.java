package ru.skidoz.model.pojo.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author andrey.semenov
 */
@Setter
@Getter
public class Button extends AbstractDTO {

    private Integer/*ButtonRowDTO*/ buttonRow;
    private Integer/*ButtonRow.Level*/ level;
    private String nameEN;
    private String nameRU;
    private String nameDE;
    private String callback;
    private Boolean display = true;
    private Integer/*User*/ user;

    private String webApp;

    private List<Integer/*User*/> usersWithCurrentChangingButtonList = new ArrayList<>();

    public Button(Consumer<Button> builder){
        super();
        builder.accept(this);
    }

    public Button() {
        super();
    }

    public Button(ButtonRow buttonRow, Map<LanguageEnum, String> nameLanguages, String callback) {
        super();
        this.buttonRow = buttonRow.getId();
        this.level = buttonRow.getLevel();
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
        this.callback = callback;
    }

    public Button(ButtonRow buttonRow, Map<LanguageEnum, String> nameLanguages, String callback, String webApp) {
        this.webApp = webApp;
        this.buttonRow = buttonRow.getId();
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
        this.callback = callback;
    }

    public Button clone(ButtonRow buttonRow) throws CloneNotSupportedException {
        Button button = new Button();//(Button) super.clone();
        button.setId(-(int) (Math.random() * 1_000_000_000));
        button.callback = this.callback;
        button.level = this.level;
        button.nameEN = this.nameEN;
        button.nameRU = this.nameRU;
        button.nameDE = this.nameDE;
        button.buttonRow = buttonRow.getId();
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
                "id=" + getId() +
                ", callback='" + callback + '\'' +
                ", buttonRow='" + buttonRow + '\'' +
                ", nameRU='" + this.nameRU + '\'' +
                ", display='" + display + '\'' +
                '}';
    }

}
