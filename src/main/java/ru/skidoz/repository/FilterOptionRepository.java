package ru.skidoz.repository;


import java.util.List;

import ru.skidoz.model.entity.FilterOptionEntity;
import ru.skidoz.model.entity.FilterPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterOptionRepository extends JpaRepository<FilterOptionEntity, Integer> {

    FilterOptionEntity findByFilterPointAndName(FilterPointEntity filterPoint, String name);

    List<FilterOptionEntity> findAllByFilterPoint(FilterPointEntity filterPoint);

    FilterOptionEntity save(FilterOptionEntity filterOption);
}
