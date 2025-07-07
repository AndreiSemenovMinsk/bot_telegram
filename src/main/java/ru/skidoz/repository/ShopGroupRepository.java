package ru.skidoz.repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.ShopGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopGroupRepository extends JpaRepository<ShopGroupEntity, Integer> {

    Optional<ShopGroupEntity> findById(Integer id);

    ShopGroupEntity save(ShopGroupEntity shopGroup);

    @Query(value = "SELECT sg FROM ShopGroupEntity sg WHERE sg.id IN (:shopGroupId) ")
    List<ShopGroupEntity> shopGroupByIds(@Param("shopGroupId") List<Integer> shopGroupId);
}
