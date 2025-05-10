package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.ShopGroupEntity;
import ru.skidoz.model.pojo.telegram.ShopGroup;

@Component
@Mapper(componentModel = "spring", uses = { ShopMapper.class})
public abstract class ShopGroupMapper extends EntityMapper<ShopGroup, ShopGroupEntity> {

    @Override
    public abstract ShopGroup toDto(ShopGroupEntity entity);

    @Override
    public abstract ShopGroupEntity toEntity(ShopGroup shop);

}
