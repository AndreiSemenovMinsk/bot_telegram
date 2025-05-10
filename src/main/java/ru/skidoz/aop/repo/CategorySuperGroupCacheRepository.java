package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.category.CategorySuperGroup;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface CategorySuperGroupCacheRepository extends JpaRepositoryTest<CategorySuperGroup, Integer> {

    CategorySuperGroup findByAlias(String alias);

//    CategorySuperGroup findById(Integer id);

    List<CategorySuperGroup> findAll();
}
