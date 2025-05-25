package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ScheduleBuyer extends AbstractDTO implements Serializable {

    @NotNull
    private Integer timeStart;

    @NotNull
    private Integer timeEnd;

    @NotNull
    private Integer day;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

    @NotNull
    private Integer/*User*/ user;

    @NotNull
    private Integer/*ProductDTO*/ product;

    public ScheduleBuyer(Consumer<ScheduleBuyer> builder){
        super();
        builder.accept(this);
    }

    public ScheduleBuyer() {
        super();
    }

}

