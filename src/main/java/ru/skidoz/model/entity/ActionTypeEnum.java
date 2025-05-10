package ru.skidoz.model.entity;


public enum ActionTypeEnum {
    BASIC("Скидка по послед.покупкам"),
    BASIC_DEFAULT("Скидка по послед.покупкам"),
    BASIC_MANUAL("Начисленный кешбек"),
    COUPON("Купон на I-ю покупку"),
    COUPON_DEFAULT("Купон на I-ю покупку"),
    LINK_TO_PRODUCT("Скидка на связанный товар");

    private String value;

    ActionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

