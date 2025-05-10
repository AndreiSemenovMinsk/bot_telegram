package ru.skidoz.repository.telegram;

import java.util.Optional;
import java.util.Set;

import ru.skidoz.model.entity.telegram.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByChatId(Long chatId);

    UserEntity findByEmail(String email);

    Optional<UserEntity> findById(Integer userId);

    UserEntity findBySessionId(String sessionId);//char[]

    @Query(value = "select byr.id from users byr where byr.session_id=:session_id", nativeQuery = true)
    Integer findBuyerId(@Param("session_id") char[] sessionId);

//    @Query(value = "SELECT rmdn.friend FROM Recommendation rmdn WHERE rmdn.shop = :shop AND rmdn.buyer = :buyer")
//    Users findFirstByShopAndBuyer(@Param("shop") Shop shop, @Param("buyer") Users buyer);

    @Query(value = "select users.* from recommendation rmdn INNER JOIN users where rmdn.shop_id=:shop_id and rmdn.buyer_id=:buyer_id", nativeQuery = true)
    UserEntity findFirstByShopAndBuyer(@Param("shop_id") Integer shopId, @Param("buyer_id") Integer buyerId);

//    @Query(value = "SELECT rmdn.friend FROM Recommendation rmdn WHERE rmdn.shop is null AND rmdn.buyer = :buyer")
//    Users findFirstByShopNullAndBuyer(@Param("buyer") Users buyer);

    @Query(value = "select users.* from recommendation rmdn INNER JOIN users where rmdn.shop_id is null and rmdn.buyer_id=:buyer_id", nativeQuery = true)
    UserEntity findFirstByShopNullAndBuyer(@Param("buyer_id") Integer buyerId);

    @Query(value = "SELECT * FROM users INNER JOIN users_shop_set on (users.id=users_shop_set.admin_users_id) WHERE shop_id = :buyer", nativeQuery = true)
    Set<UserEntity> findAllByShopId(@Param("buyer") Integer shopId);
}
