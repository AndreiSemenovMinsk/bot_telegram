package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.search.menu.FilterPoint;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface FilterPointCacheRepository extends JpaRepositoryTest<FilterPoint, Integer> {

    FilterPoint findByCategoryAndNameRU(Integer categoryId, String name);

    FilterPoint findByCategoryAndUnitNameRU(Integer categoryId, String unitName);

    List<FilterPoint> findAllByCategory_Id(Integer  categoryId);

}
