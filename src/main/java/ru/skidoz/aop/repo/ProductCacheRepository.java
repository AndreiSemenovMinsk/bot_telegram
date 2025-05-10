package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.side.Product;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ProductCacheRepository extends JpaRepositoryTest<Product, Integer> {

//    Optional<Product> findById(Integer id);

    List<Product> findAllByShop_Id(Integer shopId);

    Product findAllByShop_IdAndArticle(Integer  shopId, String nameArticle);

    Product findAllByShop_IdAndAlias(Integer  shopId, String alias);

    public List<Product> findAllByCategory_IdAndActiveIsTrue(Integer  categoryId);
//
//    void replaceAfterStoreFromRepo();
//
//    void saveNew(Prd product);
//
//    void storeFromRepo(Prd product);
}
