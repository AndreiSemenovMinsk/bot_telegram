package ru.skidoz.repository;


import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Integer> {

    Optional<ShopEntity> findById(Integer id);

    //@Query(value = "select * from shop shp inner join chat cht on (cht.) where shp.session_id=:session_id", nativeQuery = true)
    @Query(value = "SELECT shp FROM ShopEntity shp JOIN shp.adminUser usr WHERE usr.chatId = :chatId AND shp.id=usr.currentAdminShop")
    ShopEntity getByChatId(@Param("chatId") Integer chatId);


    @Query(value = "SELECT distinct shp FROM ShopEntity shp JOIN shp.sellerSet sellers WHERE sellers.chatId = :chatId AND shp.id=sellers.currentAdminShop ")
    ShopEntity getBySellerChatId(@Param("chatId") Long chatId);


    ShopEntity findBySecretId(String secretId);

    //используется для myShopsAll-> Adder.addShop
    //List<Shop> findAllBySellerIdAndActiveIsTrue(Integer buyerId);
    @Query(value = "SELECT distinct shp FROM ShopEntity shp JOIN shp.sellerSet sellers WHERE sellers.id = :userId AND shp.active = true")
    List<ShopEntity> findAllBySellerIdAndActiveIsTrue(@Param("userId") Integer userId);

//    List<Shop> findAllByAdminUserAndActiveIsTrue(User user);

    List<ShopEntity> findAllByActiveIsTrue();

    @Modifying
    @Query(value = "UPDATE shop SET id_shop=temp_id_shop WHERE secretId = :secretId",
            nativeQuery = true)
    void validateShop(@Param("secretId") String secretId);

    List<ShopEntity> findAllByNameLikeAndActiveIsTrue(String nameLike);

    ShopEntity findByNameAndAdminUser_Id(String name, Integer userId);

    ShopEntity save(ShopEntity shop);
}
