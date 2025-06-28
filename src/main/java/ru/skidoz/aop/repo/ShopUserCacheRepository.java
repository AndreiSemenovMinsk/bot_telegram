package ru.skidoz.aop.repo;

import ru.skidoz.model.pojo.side.ShopUser;

import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ShopUserCacheRepository extends JpaRepositoryTest<ShopUser, Integer> {

    ShopUser findByUserAndShop(Integer userId, Integer shopId);
}
