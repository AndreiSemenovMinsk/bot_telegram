package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "schedule_default")
public class ScheduleDefaultEntity extends AbstractEntity  implements Serializable {

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
    @ManyToOne
    //@JoinColumn(name = "productId", nullable = false, insertable = true, updatable = true)
    private ProductEntity product;

    public ScheduleDefaultEntity(Consumer<ScheduleDefaultEntity> builder){
        builder.accept(this);
    }

    public ScheduleDefaultEntity() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleDefaultEntity)) return false;
        if (!super.equals(o)) return false;
        ScheduleDefaultEntity action = (ScheduleDefaultEntity) o;
        return super.getId().equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

