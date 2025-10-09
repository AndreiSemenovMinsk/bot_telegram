package ru.skidoz.aop.repo;

import ru.skidoz.model.entity.ShopGroupEntity;
import ru.skidoz.model.pojo.telegram.ShopGroup;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ShopGroupCacheRepository extends JpaRepositoryTest<ShopGroup, Integer> {

    ShopGroup findByName(String name);

    List<ShopGroup> shopGroupByIds(List<Integer> shopGroupId);

    void deleteShopGroupByIds(List<Integer> shopGroupId);

    List<ShopGroup> shopGroupByShopAndPartner(Integer shop1Id, Integer shop2Id);
}
