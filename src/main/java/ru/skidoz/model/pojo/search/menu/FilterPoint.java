package ru.skidoz.model.pojo.search.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilterPoint extends AbstractDTO {

    @Size(max=50)
    private String nameEN;

    @Size(max=50)
    private String nameRU;

    @Size(max=50)
    private String nameDE;
    @Size(max=50)
    private String unitNameEN;

    @Size(max=50)
    private String unitNameRU;

    @Size(max=50)
    private String unitNameDE;
    private Integer categoryId;
    private Integer inputType;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private List<FilterOption> filterOptionList = new ArrayList<>();

    public FilterPoint(Consumer<FilterPoint> builder) {
        super();
        builder.accept(this);
    }

    public FilterPoint() {
        super();
    }

    public void addFilterOption(FilterOption filterOption){
        filterOptionList.add(filterOption);
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

    public void addUnitName(String name, LanguageEnum language) {
        if (LanguageEnum.EN.equals(language)) {
            this.unitNameEN = name;
        } else if (LanguageEnum.RU.equals(language)) {
            this.unitNameRU = name;
        } else if (LanguageEnum.DE.equals(language)) {
            this.unitNameDE = name;
        }
    }

    @Override
    public String toString() {
        return "FilterPointDTO{" +
                ", id='" + super.getId() + '\'' +
                "nameEN='" + nameEN + '\'' +
                ", nameRU='" + nameRU + '\'' +
                ", nameDE='" + nameDE + '\'' +
                ", unitNameEN='" + unitNameEN + '\'' +
                ", unitNameRU='" + unitNameRU + '\'' +
                ", unitNameDE='" + unitNameDE + '\'' +
                ", categoryId=" + categoryId +
                ", inputType=" + inputType +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }
}
