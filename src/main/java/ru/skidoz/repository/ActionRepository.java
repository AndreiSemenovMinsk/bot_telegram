package ru.skidoz.repository;

import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.ActionEntity;
import ru.skidoz.model.entity.ActionTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, Integer> {

    Optional<ActionEntity> findById(Integer id);

    ActionEntity findFirstByShop_IdAndTypeAndActive(Integer shopId, ActionTypeEnum type, boolean active);

    List<ActionEntity> findAllByShop_IdAndTypeAndActive(Integer shopId, ActionTypeEnum type, boolean active);

    List<ActionEntity> findAllByShop_IdAndActive(Integer shopId, boolean active);
}
