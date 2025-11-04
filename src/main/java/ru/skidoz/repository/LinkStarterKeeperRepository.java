package ru.skidoz.repository;

import ru.skidoz.model.entity.LinkStarterKeeperEntity;
import ru.skidoz.model.entity.PurchaseShopGroupEntity;
import ru.skidoz.model.pojo.telegram.LinkStarterKeeper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkStarterKeeperRepository extends JpaRepository<LinkStarterKeeperEntity, Integer> {

    LinkStarterKeeperEntity findBySecretCode(Integer secretCode);
}
