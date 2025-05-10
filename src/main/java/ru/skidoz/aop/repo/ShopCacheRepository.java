package ru.skidoz.aop.repo;

import java.util.List;
import java.util.Set;

import ru.skidoz.model.pojo.side.Shop;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ShopCacheRepository extends JpaRepositoryTest<Shop, Integer> {

    Set<Shop> findAllByActiveIsTrue();

//    Shop findById(Integer shopId);

    public Shop findByChatId(Long userChatId);

    public Shop findBySellerChatId(Long userChatId);

    public Shop findByNameAndAdminUser_Id(String name, Integer userId);

    public Shop findByName(String name);

    List<Shop> findAllBySellerIdAndActiveIsTrue(Integer userId);

    void storeFromRepo(Shop shop);

    void replaceAfterStoreFromRepo();
}
