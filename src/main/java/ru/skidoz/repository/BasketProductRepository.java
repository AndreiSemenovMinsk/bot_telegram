package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.BasketEntity;
import ru.skidoz.model.entity.BasketProductEntity;
import ru.skidoz.model.entity.PrdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProductEntity, Integer> {

    List<BasketProductEntity> findAllByBasket(BasketEntity basket);

    List<BasketProductEntity> findAllByBasketId(Integer basketId);

    List<BasketProductEntity> findAllByProduct(PrdEntity product);

    List<BasketProductEntity> findAllByBasket_User_IdAndBasket_Shop_Id(Integer/*Users*/ buyerId, Integer/*Shop*/ shopId);

    BasketProductEntity save(BasketProductEntity basketProduct);
}
