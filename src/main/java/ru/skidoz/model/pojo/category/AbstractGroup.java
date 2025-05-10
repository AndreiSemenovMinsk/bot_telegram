package ru.skidoz.model.pojo.category;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AbstractGroup extends AbstractDTO {


    @Size(max=50)
    private String nameEN;

    @Size(max=50)
    private String nameRU;

    @Size(max=50)
    private String nameDE;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    private LanguageEnum lang;

    @NotNull
    @Size(max=50)
    private String alias;

    public AbstractGroup() {
        super();
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

    public String getName() {
        if (LanguageEnum.EN.equals(lang)) {
            return this.nameEN;
        } else if (LanguageEnum.RU.equals(lang)) {
            return this.nameRU;
        } else if (LanguageEnum.DE.equals(lang)) {
            return this.nameDE;
        }
        return null;
    }
}

