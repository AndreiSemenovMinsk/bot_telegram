package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.side.Shop;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ShopCacheRepository extends JpaRepositoryTest<Shop, Integer> {

//    List<Shop> findAllByActiveIsTrue();

    Shop findBySecretHash(String secretHash);

//    Shop findById(Integer shopId);

    Shop findByChatId(Long userChatId);

//    Shop findBySellerChatId(Long userChatId);

    Shop findByNameAndAdminUser(String name, Integer userId);

    List<Shop> findAllByAdminUser(Integer userId);

    Shop findByName(String name);

//    List<Shop> findAllBySellerIdAndActive(Integer userId, boolean active);

    void storeFromRepo(Shop shop);

    void replaceAfterStoreFromRepo();

//    default List<Shop> findAllByNameLikeAndActiveIsTrue(String name) {
//        List<Integer> searchIndxList = TextParser.search(name, ShopSearchHandler.parts, ShopSearchHandler.wordPhraseList);
//
//        return searchIndxList.stream().map(index -> findByName(ShopSearchHandler.pairs.get(index))).toList();
//    }
}
