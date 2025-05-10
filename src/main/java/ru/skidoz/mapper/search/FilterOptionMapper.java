package ru.skidoz.mapper.search;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.FilterOptionEntity;
import ru.skidoz.model.pojo.search.menu.FilterOption;

@Component

@Mapper(componentModel = "spring", uses = {FilterPointMapper.class})
public abstract class FilterOptionMapper extends EntityMapper<FilterOption, FilterOptionEntity> {

    @Override
    @Mapping(source = "filterPoint.id", target = "filterPoint")
    public abstract FilterOption toDto(FilterOptionEntity filterPoint);

    @Mapping(source = "filterPoint", target = "filterPoint.id")//, qualifiedByName = "idFilterPoint")
    public abstract FilterOptionEntity toEntity(FilterOption filterPoint);
}
