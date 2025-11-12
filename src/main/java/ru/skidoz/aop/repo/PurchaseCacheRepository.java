package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.main.Purchase;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface PurchaseCacheRepository extends JpaRepositoryTest<Purchase, Integer> {

//    List<Purchase> findAllByShopAndBuyer(Integer /*Shop*/ shop, Integer/*Users*/ buyer);

//    List<Purchase> findAllByIdIsIn(List<Integer> ids);

    List<Purchase> findAllByBuyer_Id(Integer /*Users*/ buyer);

//    Purchase findById(Integer id);

    void delete(Purchase purchase);

    void deleteById(Integer id);
}
