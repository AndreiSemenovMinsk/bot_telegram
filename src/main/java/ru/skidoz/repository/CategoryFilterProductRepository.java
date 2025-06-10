package ru.skidoz.repository;


import ru.skidoz.model.entity.CategoryFilterProductEntity;
import ru.skidoz.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFilterProductRepository extends JpaRepository<CategoryFilterProductEntity, Integer> {

    void deleteAllByProduct(ProductEntity product);

    CategoryFilterProductEntity findByFilterPoint_IdAndProduct_Id(Integer filterPointId, Integer productId);

    CategoryFilterProductEntity save(CategoryFilterProductEntity categoryFilterProduct);
}
