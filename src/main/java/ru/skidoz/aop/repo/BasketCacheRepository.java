package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.side.Basket;
import org.springframework.stereotype.Service;

@Service
public interface BasketCacheRepository extends JpaRepositoryTest<Basket, Integer> {

    List<Basket> findAllByUserId(Integer id);

    List<Basket> findAllByUserIdAndTemp(Integer id, Boolean temp);

    List<Basket> findAllByUserIdAndShopIdAndTemp(Integer buyerId, Integer  shopId, boolean temp);

    //List<Basket> findAllByUserIdAndShopIdConst(Integer buyerId, Integer  shopId);

    void delete(Basket button);

//    void deleteAllByUser_IdAndShop_Id(Integer buyerId, Integer  shopId);
    //findAllByUser_IdAndShop_IdTemp
}
