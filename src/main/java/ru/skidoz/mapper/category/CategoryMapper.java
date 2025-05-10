package ru.skidoz.mapper.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.category.CategoryEntity;
import ru.skidoz.model.pojo.category.Category;

@Component
@Mapper(componentModel = "spring", uses = {
        CategoryGroupMapper.class,
        CategorySuperGroupMapper.class
})
public abstract class CategoryMapper extends EntityMapper<Category, CategoryEntity> {

    @Mapping(target = "childCategorySet", ignore = true)
    @Mapping(target = "productSet", ignore = true)
    @Mapping(target = "filterPointList", ignore = true)
    @Mapping(source = "categoryGroup.id", target = "categoryGroup")
    @Mapping(source = "parentCategory.id", target = "parentCategory")
    @Mapping(source = "categorySuperGroup.id", target = "categorySuperGroup")
    public abstract Category toDto(CategoryEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "childCategorySet", ignore = true)
    @Mapping(target = "productSet", ignore = true)
    @Mapping(target = "filterPointList", ignore = true)
    @Mapping(source = "parentCategory", target = "parentCategory.id")//, qualifiedByName = "idCat")
    @Mapping(source = "categoryGroup", target = "categoryGroup.id")//, qualifiedByName = "idCatG")
    @Mapping(source = "categorySuperGroup", target = "categorySuperGroup.id")//, qualifiedByName = "idCatSG")
    public abstract CategoryEntity toEntity(Category entity);

}
