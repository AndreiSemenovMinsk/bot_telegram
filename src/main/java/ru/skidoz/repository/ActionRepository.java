package ru.skidoz.repository;

import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.ActionEntity;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, Integer> {

    Optional<ActionEntity> findById(Integer id);

    ActionEntity findFirstByShopAndTypeAndActiveIsTrue(ShopEntity shop, ActionTypeEnum type);

    List<ActionEntity> findAllByShopAndTypeAndActiveIsTrue(ShopEntity shop, ActionTypeEnum type);

    List<ActionEntity> findAllByShopAndActiveIsTrue(ShopEntity shop);

    ActionEntity save(ActionEntity action);
}
