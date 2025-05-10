package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.category.Category;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface CategoryCacheRepository extends JpaRepositoryTest<Category, Integer> {

    public Category findByAliasAndCategoryGroup(String alias, Integer categoryGroupId);

    public List<Category> findByCategoryGroup_Id(Integer categoryGroupId);

//    public Category findById(Integer id);
}
