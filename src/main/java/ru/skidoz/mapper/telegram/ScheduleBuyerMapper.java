package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.side.ProductMapper;
import ru.skidoz.model.entity.ScheduleBuyerEntity;
import ru.skidoz.model.pojo.telegram.ScheduleBuyer;

@Component
@Mapper(componentModel = "spring", uses = {/*JpaRepository.class, */ProductMapper.class, UsersMapper.class})
public abstract class ScheduleBuyerMapper extends EntityMapper<ScheduleBuyer, ScheduleBuyerEntity> {

    @Override
    @Mapping(source = "users.id", target = "users")
    @Mapping(source = "product.id", target = "product")
    public abstract ScheduleBuyer toDto(ScheduleBuyerEntity entity);


    @Override
    @Mapping(source = "users", target = "users.id")//, qualifiedByName = "idUser")
    @Mapping(source = "product", target = "product.id")//, qualifiedByName = "idPrd")
    public abstract ScheduleBuyerEntity toEntity(ScheduleBuyer scheduleBuyer);
}
