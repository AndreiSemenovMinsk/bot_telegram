package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.side.Bookmark;
import org.springframework.stereotype.Service;

@Service
public interface BookmarkCacheRepository extends JpaRepositoryTest<Bookmark, Integer> {

    List<Bookmark> findAllByUserId(Integer id);

    Bookmark findByIdAndUserId(Integer id, Integer  buyerId);

    List<Bookmark> findAllByUserIdAndShopId(Integer userId, Integer  shopId);

    List<Bookmark> findAllNotify();
//    Bookmark findById(Integer id);findAllByUserIdAndShopId
}
