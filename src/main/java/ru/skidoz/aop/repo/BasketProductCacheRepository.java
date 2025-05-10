package ru.skidoz.aop.repo;


import java.util.List;

import ru.skidoz.model.pojo.side.BasketProduct;
import org.springframework.stereotype.Service;

@Service
public interface BasketProductCacheRepository extends JpaRepositoryTest<BasketProduct, Integer> {

    List<BasketProduct> findAllByBasketId(Integer  basketId);

//    List<BasketProduct> findAllByBasket_User_IdAndBasket_Shop_Id(Integer /*Users*/ buyerId, Integer/*Shop*/ shopId);
}
