package ru.skidoz.mapper.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.category.CategoryGroupEntity;
import ru.skidoz.model.pojo.category.CategoryGroup;

@Component
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, CategorySuperGroupMapper.class})
public abstract class CategoryGroupMapper extends EntityMapper<CategoryGroup, CategoryGroupEntity> {

    @Mapping(target = "categorySet", ignore = true)
    @Mapping(target = "productSet", ignore = true)
    @Mapping(source = "categorySuperGroup.id", target = "categorySuperGroup")
    public abstract CategoryGroup toDto(CategoryGroupEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "categorySet", ignore = true)
    @Mapping(target = "productSet", ignore = true)
    @Mapping(source = "categorySuperGroup", target = "categorySuperGroup.id")//, qualifiedByName = "idCatSG")
    public abstract CategoryGroupEntity toEntity(CategoryGroup entity);

}
