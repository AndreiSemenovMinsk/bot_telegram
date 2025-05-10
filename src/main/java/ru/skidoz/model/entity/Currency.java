package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.function.Consumer;

import jakarta.persistence.*;

@Entity
public class Currency  extends AbstractEntity  implements Serializable {


    private Double rate;

//    @Column(unique = true)
    private String code;

    public Currency(String code){
        this.code = code;
    }

    public Currency(){
    }

    public Currency (Consumer<Currency> builder){
        builder.accept(this);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}

