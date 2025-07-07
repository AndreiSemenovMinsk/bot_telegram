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

    List<ShopGroup> shopGroupByIds(List<Integer> shopGroupId);
}
