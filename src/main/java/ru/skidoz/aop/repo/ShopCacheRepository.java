package ru.skidoz.aop.repo;

import java.util.List;
import java.util.Set;

import ru.skidoz.model.pojo.side.Shop;
import org.springframework.stereotype.Service;
import ru.skidoz.service.search.ShopSearchHandler;
import ru.skidoz.util.TextParser;

/**
 * @author andrey.semenov
 */
@Service
public interface ShopCacheRepository extends JpaRepositoryTest<Shop, Integer> {

    List<Shop> findAllByActiveIsTrue();

    Shop findBySecretId(String secretId);

//    Shop findById(Integer shopId);

    Shop findByChatId(Long userChatId);

    Shop findBySellerChatId(Long userChatId);

    Shop findByNameAndAdminUser_Id(String name, Integer userId);

    Shop findByName(String name);

    List<Shop> findAllBySellerIdAndActiveIsTrue(Integer userId);

    void storeFromRepo(Shop shop);

    void replaceAfterStoreFromRepo();

    default List<Shop> findAllByNameLikeAndActiveIsTrue(String name) {
        List<Integer> searchIndxList = TextParser.search(name, ShopSearchHandler.parts, ShopSearchHandler.wordPhraseList);

        return searchIndxList.stream().map(index -> findByName(ShopSearchHandler.pairs.get(index))).toList();
    }
}
