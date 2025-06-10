package ru.skidoz.repository;


import java.util.List;

import ru.skidoz.model.entity.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    CategoryEntity findByAliasAndCategoryGroup_Id(String alias, Integer categoryGroupId);

    List<CategoryEntity> findByCategoryGroup_Id(Integer categoryGroupId);

    CategoryEntity save(CategoryEntity category);
}
