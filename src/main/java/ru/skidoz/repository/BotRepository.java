package ru.skidoz.repository;

import ru.skidoz.model.entity.BotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends JpaRepository<BotEntity, Integer> {

    BotEntity findByShopId(Integer shopId);

    BotEntity save(BotEntity bot);
}
