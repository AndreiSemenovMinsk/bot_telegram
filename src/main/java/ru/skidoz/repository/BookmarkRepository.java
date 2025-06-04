package ru.skidoz.repository;

import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.BookmarkEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Integer> {

    List<BookmarkEntity> findAllByUser_Id(Integer id);

    @Query(value = "SELECT bkmrk FROM BookmarkEntity bkmrk JOIN bkmrk.shop shp WHERE shp.adminUser = :shopBuyer AND bkmrk.user = :buyer")
    List<BookmarkEntity> findAllByUserAndShopBuyer(@Param("buyer") UserEntity buyer, @Param("shopBuyer") UserEntity shopBuyer);

    @Query(value = "SELECT bkmrk FROM BookmarkEntity bkmrk JOIN bkmrk.product product WHERE product.price < bkmrk.bidPrice AND bkmrk.priceUpdated = FALSE")
    List<BookmarkEntity> findAllByPriceStrikeAndPriceUpdated(Boolean strike, Boolean updated);


    List<BookmarkEntity> findAllByUser_IdAndShop_Id(Integer userId, Integer shopId);

    BookmarkEntity findByIdAndUserId(Integer id, Integer buyerId);

    Optional<BookmarkEntity> findById(Integer id);

    List<BookmarkEntity> findAllByUser(UserEntity buyer);

    BookmarkEntity save(BookmarkEntity bookmark);
}
