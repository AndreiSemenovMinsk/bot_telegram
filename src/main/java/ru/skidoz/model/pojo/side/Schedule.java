package ru.skidoz.model.pojo.side;

import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Schedule extends AbstractDTO {

    private Integer day;
    private Integer month;
    private Integer year;
    private Integer productId;
    private Integer timeStart;
    private Integer timeEnd;
    Integer dayOld;
    Integer monthOld;
    Integer yearOld;
    Integer dayNew;
    Integer monthNew;
    Integer yearNew;

    public Schedule(Consumer<Schedule> builder){
        super();
        builder.accept(this);
    }

    public Schedule() {
        super();
    }
}
