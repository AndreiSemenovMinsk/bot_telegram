package ru.skidoz.mapper.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.category.CategorySuperGroupEntity;
import ru.skidoz.model.pojo.category.CategorySuperGroup;

@Component
@Mapper(componentModel = "spring", uses = {})
public abstract class  CategorySuperGroupMapper extends EntityMapper<CategorySuperGroup, CategorySuperGroupEntity> {

    @Override
    @Mapping(target = "categoryGroupSet", ignore = true)
    @Mapping(target = "productSet", ignore = true)
    public abstract CategorySuperGroup toDto(CategorySuperGroupEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "categoryGroupSet", ignore = true)
    @Mapping(target = "productSet", ignore = true)
    public abstract CategorySuperGroupEntity toEntity(CategorySuperGroup dto);

}
