package ru.skidoz.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
public class BookmarkEntity extends AbstractEntity  implements Serializable {

    @NotNull
    private Integer radius;

    @NotNull
    private boolean viberNotify;

    @NotNull
    private boolean priceUpdated = true;

    private BigDecimal bidPrice;

    @NotNull
    @ManyToOne
    private ShopEntity shop;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "buyerId", nullable = false, insertable = false, updatable = false)
    private UserEntity user;

    private Instant notification;

    private Instant lastNotification;

    private boolean notificationSent = false;

    private boolean priceStrike = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    public BookmarkEntity(Consumer<BookmarkEntity> builder){
        builder.accept(this);
    }

    public BookmarkEntity() {
        super();
    }


    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", radius=" + radius +
                ", viberNotify=" + viberNotify +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookmarkEntity)) return false;
        if (!super.equals(o)) return false;
        BookmarkEntity action = (BookmarkEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

