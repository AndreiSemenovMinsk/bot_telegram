package ru.skidoz.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.skidoz.model.entity.telegram.UserEntity;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "link_starter_keeper",
        indexes = {
                @Index(name = "IDX_LINK_STARTER_KEEPER_SECRET_CODE", columnList = "secretCode")})
public class LinkStarterKeeperEntity extends AbstractEntity  implements Serializable {

    private Integer parameter1;

    private Integer parameter2;

    private Integer parameter3;

    private Integer secretCode;

    public LinkStarterKeeperEntity(Consumer<LinkStarterKeeperEntity> builder){
        builder.accept(this);
    }

    public LinkStarterKeeperEntity() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkStarterKeeperEntity)) return false;
        if (!super.equals(o)) return false;
        LinkStarterKeeperEntity action = (LinkStarterKeeperEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

