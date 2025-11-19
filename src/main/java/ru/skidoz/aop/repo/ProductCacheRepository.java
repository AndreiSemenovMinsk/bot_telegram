package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.side.Product;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ProductCacheRepository extends JpaRepositoryTest<Product, Integer> {

    List<Product> findAllByShop_Id(Integer shopId);

    Product findByShop_IdAndArticle(Integer  shopId, String nameArticle);

    Product findByShop_IdAndAlias(Integer  shopId, String alias);

    List<Product> findAllByCategory_IdAndActive(Integer  categoryId, Boolean active);
}
