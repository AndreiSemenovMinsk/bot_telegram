package ru.skidoz.model.entity.category;

import java.io.Serializable;

import ru.skidoz.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//@Data
@Getter
@Setter
@Entity
public class NameLocaleEntity extends AbstractEntity implements Serializable {
/*
    @NotNull
    @ManyToOne
    private AbstractGroupEntity abstractGroup;

    @OneToMany(mappedBy = "nameLocal")
    private List<LanguageEnum>  language;

    @NotNull
    private String alias;

    public NameLocale(Consumer<NameLocale> builder){
        builder.accept(this);
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + super.getId() +
                '}';
    }*/

    public NameLocaleEntity() {
        super();
    }
}

