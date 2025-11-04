package ru.skidoz.aop.repo;

import ru.skidoz.model.pojo.telegram.ShopGroupAddVote;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ShopGroupAddVoteCacheRepository extends JpaRepositoryTest<ShopGroupAddVote, Integer> {

    ShopGroupAddVote findByVoterShop_IdAndAddingShop_Id(
            Integer /*VoterShop*/ voterShop,
            Integer/*AddingShop*/ addingShop);

    List<ShopGroupAddVote> findAllByShopGroupAndAddingShop(
            Integer /*VoterShop*/ voterShop,
            Integer/*ShopGroup*/ addingShop);

    ShopGroupAddVote findBySecretCodeAndAddingShop_Id(Integer secretCode, Integer addingShop);
}