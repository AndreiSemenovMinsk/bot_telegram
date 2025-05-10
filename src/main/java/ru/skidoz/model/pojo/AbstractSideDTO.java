package ru.skidoz.model.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AbstractSideDTO extends AbstractDTO {

    Integer/*User*/ user;

    public AbstractSideDTO(Integer id) {
        super.setId(id);
    }

    public AbstractSideDTO() {
//        this.setId(-(long) (Math.random() * 1_000_000_000L));
    }
}

