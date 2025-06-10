package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.BasketProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProductEntity, Integer> {

    List<BasketProductEntity> findAllByBasket_Id(Integer basketId);

    List<BasketProductEntity> findAllByProduct_Id(Integer productId);

    List<BasketProductEntity> findAllByBasket_User_IdAndBasket_Shop_Id(Integer buyerId, Integer shopId);
}
