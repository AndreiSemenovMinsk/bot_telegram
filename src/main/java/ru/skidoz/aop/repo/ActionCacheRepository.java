package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Action;
import org.springframework.stereotype.Service;

@Service
public interface ActionCacheRepository extends JpaRepositoryTest<Action, Integer> {

    Action findFirstByShopAndTypeAndActive(Integer/*Shop*/ shopId, ActionTypeEnum type, boolean active);

    List<Action> findAllByShopAndTypeAndActive(Integer/*Shop*/ shopId, ActionTypeEnum type, boolean active);

    List<Action> findAllByShopAndActive(Integer/*Shop*/ shopId, boolean active);

//    List<Action> findAllByShopAndTypeAndActive(Integer shopId, ActionTypeEnum type, boolean active);

}
