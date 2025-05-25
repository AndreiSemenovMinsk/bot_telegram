package ru.skidoz.repository;


import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.CashbackWriteOffEntity;
import ru.skidoz.model.entity.ShopEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashbackWriteOffRepository extends JpaRepository<CashbackWriteOffEntity, Integer> {

    List<CashbackWriteOffEntity> findAllByUserIdAndApprovedIsFalse(Integer id);

    CashbackWriteOffEntity findAllByUserAndShopAndApprovedIsFalse(UserEntity buyer, ShopEntity shop);

    Optional<CashbackWriteOffEntity> findById(Integer id);

    CashbackWriteOffEntity findByShopAndUserAndApprovedFalse(ShopEntity shop, UserEntity buyer);

    List<CashbackWriteOffEntity> findAllByUser_Id(Integer/*Users*/ buyer);

    CashbackWriteOffEntity save(CashbackWriteOffEntity cashbackWriteOff);
}
