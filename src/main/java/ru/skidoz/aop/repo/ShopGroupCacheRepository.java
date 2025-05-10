package ru.skidoz.aop.repo;

import ru.skidoz.model.pojo.telegram.ShopGroup;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ShopGroupCacheRepository extends JpaRepositoryTest<ShopGroup, Integer> {

}
