package ru.skidoz.mapper.search;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.category.CategoryMapper;
import ru.skidoz.model.entity.FilterPointEntity;
import ru.skidoz.model.pojo.search.menu.FilterPoint;

@Component
@Mapper(componentModel = "spring", uses = {FilterOptionMapper.class, CategoryMapper.class})
public abstract class FilterPointMapper extends EntityMapper<FilterPoint, FilterPointEntity> {

    @Override
    @Mapping(source = "category.id", target = "categoryId")
    public abstract FilterPoint toDto(FilterPointEntity filterPoint);

    @Override
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "filterOptionList", ignore = true)
    @Mapping(target = "categoryFilterProductList", ignore = true)
    public abstract FilterPointEntity toEntity(FilterPoint filterPoint);

}
