package ru.skidoz.aop.repo;

import java.util.List;
import java.util.Set;

import ru.skidoz.model.pojo.telegram.User;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface UserCacheRepository extends JpaRepositoryTest<User, Integer> {

    User findByChatId(Long chatId);

//    public Users findById(Integer userId);

    User findBySessionId(String sessionId);

    void saveByCacheOnly(User users);

    void storeFromRepo(User users);

    void replaceAfterStoreFromRepo();

    User findFirstByShop_IdAndBuyer_Id(Integer shopId, Integer buyerId);

    User findFirstByShopNullAndBuyer_Id(Integer buyerId);

    List<User> findAllByShopId(Integer shopId);

    List<User> findAllByName(String name);
}
