package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.PrdEntity;
import ru.skidoz.model.entity.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<PrdEntity, Integer> {

//    List<Prd> findAllByCategorySuperGroupAndNameInAndActiveIsTrue(CatSG categorySuperGroup, List<String> names);
//
//    List<Prd> findAllByCategoryGroupAndNameInAndActiveIsTrue(CatG categoryGroup, List<String> names);
//
//    List<Prd> findAllByCategoryAndNameInAndActiveIsTrue(Cat category, List<String> names);

    List<PrdEntity> findAllByShop_Id(Integer shopId);

//    List<Prd> findAllByNameInAndActiveIsTrue(List<String> names);
//
//    List<Prd> findAllByCategorySuperGroupAndActiveIsTrue(CatSG categorySuperGroup);
//
//    List<Prd> findAllByCategoryGroupAndActiveIsTrue(CatG categoryGroup);

    List<PrdEntity> findAllByCategoryAndActiveIsTrue(CategoryEntity category);

    List<PrdEntity> findAllByCategory_IdAndActiveIsTrue(Integer categoryId);

//    List<Prd> findAllByNameContaining(String name);

//    Prd findAllByShopAndName(Shop shop, String name);

    PrdEntity findAllByShop_IdAndArticle(Integer shopId, String nameArticle);

    PrdEntity save(PrdEntity prd);
}
