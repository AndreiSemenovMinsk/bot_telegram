package ru.skidoz.repository;


import ru.skidoz.model.entity.category.CategorySuperGroupEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySuperGroupRepository extends JpaRepository<CategorySuperGroupEntity, Integer> {

    CategorySuperGroupEntity findByAlias(String alias);

    List<CategorySuperGroupEntity> findAll();
//
//    List<CatSG> findAllByNameContaining(String name);
//
//    List<CatSG> findAllByNameContainingAndNameContaining(String name1, String name2);

    CategorySuperGroupEntity save(CategorySuperGroupEntity categorySuperGroup);
}
