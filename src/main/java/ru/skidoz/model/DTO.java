package ru.skidoz.model;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DTO {

    //@Id
    private int id;

    private int mark;// = ThreadLocalRandom.current().nextInt();
    private List<Object> fieldValues;
    private List<String> methodKeys;

    public DTO () {
        id = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, -1);
        mark = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMark() {
        return mark;
    }

    public List<Object> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<Object> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
