package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.function.Consumer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "filter_option")
public class FilterOptionEntity extends AbstractEntity  implements Serializable {

    @NotNull
    private String name;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "filterPointId"/*, nullable = false*/)
    private FilterPointEntity filterPoint;

    public FilterOptionEntity(Consumer<FilterOptionEntity> builder){
        builder.accept(this);
    }

    public FilterOptionEntity() {
        super();
    }

    @Override
    public String toString() {
        return "FilterOption{" +
                "name='" + name + '\'' +
                ", filterPoint=" + filterPoint +
                ", filterPoint='" +
                (filterPoint.getId() > 0 ?  "+" : filterPoint.getNameRU() + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + filterPoint.getNameEN()) + "\'" +
                '}';
    }
}

