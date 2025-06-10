package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.category.CategoryGroup;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface CategoryGroupCacheRepository extends JpaRepositoryTest<CategoryGroup, Integer> {

    CategoryGroup findByAliasAndCategorySuperGroup(String alias, Integer categoryGroupId);

    List<CategoryGroup> findAllByCategorySuperGroup_Id(Integer categorySuperGroupId);

//    CategoryGroup findById(Integer id);

    //CategoryGroup save(CategoryGroup categoryGroup);
}
