package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.search.menu.FilterPoint;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface FilterPointCacheRepository extends JpaRepositoryTest<FilterPoint, Integer> {

    FilterPoint findByCategoryAndNameRu(Integer categoryId, String name);

    FilterPoint findByCategoryAndUnitNameRu(Integer categoryId, String unitName);

    List<FilterPoint> findAllByCategory_Id(Integer  categoryId);

}
