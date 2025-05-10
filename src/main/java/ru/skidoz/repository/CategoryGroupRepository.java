package ru.skidoz.repository;


import java.util.List;

import ru.skidoz.model.entity.category.CategoryGroupEntity;
import ru.skidoz.model.entity.category.CategorySuperGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroupEntity, Integer> {

    CategoryGroupEntity findByAliasAndCategorySuperGroup(String alias, CategorySuperGroupEntity categorySuperGroup);

    List<CategoryGroupEntity> findByCategorySuperGroup_Id(Integer id);

//    List<CatG> findAllByNameContaining(String name);
//
//    List<CatG> findAllByNameContainingAndNameContaining(String name1, String name2);

    CategoryGroupEntity save(CategoryGroupEntity categoryGroup);
}
