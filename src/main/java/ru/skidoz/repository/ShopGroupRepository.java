package ru.skidoz.repository;


import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.ShopGroupEntity;
import ru.skidoz.model.pojo.telegram.ShopGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopGroupRepository extends JpaRepository<ShopGroupEntity, Integer> {

    ShopGroupEntity findByName(String name);

    Optional<ShopGroupEntity> findById(Integer id);

    ShopGroupEntity save(ShopGroupEntity shopGroup);

    @Query(value = "SELECT sg FROM ShopGroupEntity sg WHERE sg.id IN (:shopGroupId) ")
    List<ShopGroupEntity> shopGroupByIds(@Param("shopGroupId") List<Integer> shopGroupId);

    @Query(value = "DELETE FROM ShopGroupEntity sg WHERE sg.id IN (:shopGroupId) ")
    void deleteShopGroupByIds(List<Integer> shopGroupId);


    @Query(value = "SELECT * FROM shop_group_shops WHERE shop_id IN (?, ?) GROUP BY shop_group_id HAVING COUNT(DISTINCT shop_id) = 2",
            nativeQuery = true)
    List<ShopGroupEntity> shopGroupByShopAndPartner(@Param("shop1") Integer shop1Id, @Param("shop2") Integer shop2Id);

}
