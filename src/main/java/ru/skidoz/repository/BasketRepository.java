package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.BasketEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<BasketEntity, Integer> {

    List<BasketEntity> findAllByUserIdAndTemp(Integer id, Boolean temp);

    @Query(value = "SELECT bskt FROM BasketEntity bskt JOIN bskt.shop shp WHERE shp.adminUser = :shopBuyer AND bskt.user = :buyer")
    List<BasketEntity> findAllByUserAndShopBuyer(@Param("buyer") UserEntity buyer, @Param("shopBuyer") UserEntity shopBuyer);

    List<BasketEntity> findAllByUser_IdAndShop_Id(Integer buyerId, Integer shopId);

    List<BasketEntity> findAllByUser_Id(Integer buyerId);

    void deleteAllByUser_IdAndShop_Id(Integer buyerId, Integer shopId);

    void deleteById(Integer id);
}
