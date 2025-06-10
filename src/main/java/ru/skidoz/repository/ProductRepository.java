package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.ProductEntity;
import ru.skidoz.model.entity.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

//    List<Prd> findAllByCategorySuperGroupAndNameInAndActiveIsTrue(CatSG categorySuperGroup, List<String> names);
//
//    List<Prd> findAllByCategoryGroupAndNameInAndActiveIsTrue(CatG categoryGroup, List<String> names);
//
//    List<Prd> findAllByCategoryAndNameInAndActiveIsTrue(Cat category, List<String> names);

    List<ProductEntity> findAllByShop_Id(Integer shopId);

//    List<Prd> findAllByNameInAndActiveIsTrue(List<String> names);
//
//    List<Prd> findAllByCategorySuperGroupAndActiveIsTrue(CatSG categorySuperGroup);
//
//    List<Prd> findAllByCategoryGroupAndActiveIsTrue(CatG categoryGroup);

    List<ProductEntity> findAllByCategoryAndActive(CategoryEntity category, Boolean active);

    List<ProductEntity> findAllByCategory_IdAndActive(Integer categoryId, Boolean active);

//    List<Prd> findAllByNameContaining(String name);

//    Prd findAllByShopAndName(Shop shop, String name);

    ProductEntity findAllByShop_IdAndArticle(Integer shopId, String nameArticle);

    ProductEntity save(ProductEntity prd);
}
