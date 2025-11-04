package ru.skidoz.repository;


import ru.skidoz.model.entity.ShopEntity;
import ru.skidoz.model.entity.ShopUserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopUserRepository extends JpaRepository<ShopUserEntity, Integer> {

    ShopUserEntity findByUser_IdAndShop_Id(Integer userId, Integer shopId);

}
