package ru.skidoz.repository;


import java.util.List;

import ru.skidoz.model.entity.category.CategoryEntity;
import ru.skidoz.model.entity.category.CategoryGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    CategoryEntity findByAliasAndCategoryGroup(String alias, CategoryGroupEntity categoryGroup);

    List<CategoryEntity> findByCategoryGroup(CategoryGroupEntity categoryGroup);

    List<CategoryEntity> findByCategoryGroup_Id(Integer categoryGroupId);

//    List<Cat> findAllByNameContaining(String name);
//
//    List<Cat> findAllByNameContainingAndNameContaining(String name1, String name2);
    CategoryEntity save(CategoryEntity category);
}
