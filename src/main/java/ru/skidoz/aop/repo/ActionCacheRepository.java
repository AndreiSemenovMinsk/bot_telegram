package ru.skidoz.aop.repo;

import java.io.Serializable;
import java.util.List;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Action;
import org.springframework.stereotype.Service;

@Service
public interface ActionCacheRepository extends JpaRepositoryTest<Action, Integer> {

    Action findFirstByShopAndTypeAndActiveIsTrue(Integer/*Shop*/ shopId, ActionTypeEnum type);

    List<Action> findAllByShopAndTypeAndActiveIsTrue(Integer/*Shop*/ shopId, ActionTypeEnum type);

    List<Action> findAllByShopAndActiveIsTrue(Integer/*Shop*/ shopId);

//    List<Action> findAllByShopAndTypeAndActiveIsTrue(Integer shopId, ActionTypeEnum type, boolean active);

}
