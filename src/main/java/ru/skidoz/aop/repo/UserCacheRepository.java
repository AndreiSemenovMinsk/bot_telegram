package ru.skidoz.aop.repo;

import java.util.Set;

import ru.skidoz.model.pojo.telegram.User;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface UserCacheRepository extends JpaRepositoryTest<User, Integer> {

    public User findByChatId(Long chatId);

//    public Users findById(Integer userId);

    public User findBySessionId(String sessionId);

    public void saveByCacheOnly(User users);

    public void storeFromRepo(User users);

    void replaceAfterStoreFromRepo();

    User findFirstByShop_IdAndBuyer_Id(Integer shopId, Integer buyerId);

    User findFirstByShopNullAndBuyer_Id(Integer buyerId);

    Set<User> findAllByShopId(Integer shopId);
}
