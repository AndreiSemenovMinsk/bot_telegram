package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.RecommendationEntity;
import ru.skidoz.model.entity.ShopEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationEntity, Integer> {

    RecommendationEntity findFirstByBuyerAndShop(UserEntity buyer, ShopEntity shop);

    RecommendationEntity findFirstByBuyerAndShopIsNull(UserEntity buyer);

    List<RecommendationEntity> findAllByBuyer_Id(Integer/*Users*/ buyer);

    List<RecommendationEntity> findAllByFriend_Id(Integer/*Users*/ friend);

    RecommendationEntity save(RecommendationEntity recommendation);
}
