package ru.skidoz.aop.repo;


import org.springframework.stereotype.Repository;
import ru.skidoz.model.pojo.search.menu.CategoryFilterProduct;
import ru.skidoz.model.pojo.side.Product;

@Repository
public interface CategoryFilterProductCacheRepository extends JpaRepositoryTest<CategoryFilterProduct, Integer> {

    void deleteAllByProduct(Product product);

    CategoryFilterProduct findByFilterPoint_IdAndProduct_Id(Integer filterPointId, Integer productId);

    //CategoryFilterProduct save(CategoryFilterProduct categoryFilterProduct);
}
