package ru.skidoz.repository;


import java.util.Optional;

import ru.skidoz.model.entity.ShopGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopGroupRepository extends JpaRepository<ShopGroupEntity, Integer> {

    Optional<ShopGroupEntity> findById(Integer id);

    ShopGroupEntity save(ShopGroupEntity shopGroup);
}
