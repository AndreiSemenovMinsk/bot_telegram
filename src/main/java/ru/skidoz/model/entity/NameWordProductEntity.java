package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.function.Consumer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "name_word_product")
public class NameWordProductEntity extends AbstractEntity  implements Serializable {

    @ManyToOne
    private NameWordEntity nameWord;

    @ManyToOne
    private PrdEntity product;

    public NameWordProductEntity(Consumer<NameWordProductEntity> builder){
        builder.accept(this);
    }

    public NameWordProductEntity() {

    }

    @Override
    public String toString() {
        return "NameWordProduct{" +
                "id=" + id +
                ", nameWord=" + nameWord.getText() +
//                ", product=" + product.getName() +
                '}';
    }
}