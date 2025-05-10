package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.telegram.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "recommendation",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_RECOMMENDATION_COL_BUYER_FRIEND_SHOP", columnNames = {"buyer_id", "friend_id", "shop_id"})},
        indexes = {
                @Index(name = "IDX_RECOMMENDATION_COL_ID", columnList = "id"),
                @Index(name = "IDX_RECOMMENDATION_COL_FRIEND", columnList = "friend_id"),
                @Index(name = "IDX_RECOMMENDATION_COL_BUYER", columnList = "buyer_id"),
                @Index(name = "IDX_RECOMMENDATION_COL_SHOP", columnList = "shop_id"),
                @Index(name = "IDX_RECOMMENDATION_COL_BUYER_SHOP", columnList = "buyer_id,shop_id"),
                @Index(name = "IDX_RECOMMENDATION_COL_FRIEND_SHOP", columnList = "friend_id,shop_id")})
public class RecommendationEntity extends AbstractEntity  implements Serializable {

    // если дал рекомендацию магазин
    @ManyToOne
    //@JoinColumn(name = "shopId", nullable = false, insertable = true, updatable = true)
    private ShopEntity shop;

    // тот, кто получил рекомендацию
    @NotNull
    @ManyToOne
    //@JoinColumn(name = "buyerId", nullable = false, insertable = true, updatable = true)
    private UserEntity buyer;

    // тот, кто дал рекомендацию
    @ManyToOne
    //@JoinColumn(name = "friendId", nullable = false, insertable = true, updatable = true)
    private UserEntity friend;

    public RecommendationEntity(Consumer<RecommendationEntity> builder){
        builder.accept(this);
    }

    public RecommendationEntity() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecommendationEntity)) return false;
        if (!super.equals(o)) return false;
        RecommendationEntity action = (RecommendationEntity) o;
        return super.getId().equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

