package ru.skidoz.repository;


import ru.skidoz.model.entity.ShopEntity;
import ru.skidoz.model.entity.ShopGroupAddVoteEntity;
import ru.skidoz.model.pojo.telegram.ShopGroupAddVote;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopGroupAddVoteRepository extends JpaRepository<ShopGroupAddVoteEntity, Integer> {

    List<ShopGroupAddVoteEntity> findAllByShopGroup_IdAndAddingShop_Id(
            Integer /*VoterShop*/ voterShop,
            Integer/*ShopGroup*/ addingShop);

    ShopGroupAddVoteEntity findBySecretCodeAndAddingShop_Id(Integer secretCode, Integer addingShop);
}
