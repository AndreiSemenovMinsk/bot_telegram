package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "schedule_buyer")
public class ScheduleBuyerEntity extends AbstractEntity  implements Serializable {

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
    //@JoinColumn(name = "buyerId", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "productId", nullable = false, insertable = false, updatable = false)
    private ProductEntity product;

    public ScheduleBuyerEntity(Consumer<ScheduleBuyerEntity> builder){
        builder.accept(this);
    }

    public ScheduleBuyerEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleBuyerEntity)) return false;
        if (!super.equals(o)) return false;
        ScheduleBuyerEntity action = (ScheduleBuyerEntity) o;
        return super.getId().equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

