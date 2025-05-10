package ru.skidoz.model.entity.category;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum LanguageEnum {
    EN("EN"),
    RU("RU"),
    DE("DE");

    private String value;

    LanguageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LanguageEnum valueByCode(String key) {

        if ("RU".equals(key)) return RU;
        if ("BY".equals(key)) return RU;
        if ("UA".equals(key)) return RU;
        if ("MD".equals(key)) return RU;
        if ("DE".equals(key)) return DE;
        if ("AT".equals(key)) return DE;
        if ("LI".equals(key)) return DE;
        if ("EN".equals(key)) return EN;
        if ("BEL".equals(key)) return EN;
        if ("CHN".equals(key)) return EN;
        if ("US".equals(key)) return EN;
        if ("FR".equals(key)) return EN;
        if ("SN".equals(key)) return EN;
        if ("IT".equals(key)) return EN;
        return null;
    }
}