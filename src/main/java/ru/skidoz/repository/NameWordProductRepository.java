package ru.skidoz.repository;

import java.util.Set;

import ru.skidoz.model.entity.NameWordEntity;
import ru.skidoz.model.entity.NameWordProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameWordProductRepository extends JpaRepository<NameWordProductEntity, Integer> {


    Set<NameWordProductEntity> findByNameWord(NameWordEntity nameWord);

    NameWordProductEntity save(NameWordProductEntity nameWordProduct);
}
