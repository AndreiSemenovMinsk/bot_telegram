package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.search.menu.FilterOption;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface FilterOptionCacheRepository extends JpaRepositoryTest<FilterOption, Integer> {

    FilterOption findByFilterPointAndName(Integer  filterPointId, String name);

    List<FilterOption> findAllByFilterPoint(Integer  filterPointId);
}
