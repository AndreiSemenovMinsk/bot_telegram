package ru.skidoz.mapper.telegram;

import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.side.ShopMapper;
import ru.skidoz.model.entity.ShopUserEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import ru.skidoz.model.pojo.side.ShopUser;
import ru.skidoz.model.pojo.telegram.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopMapper.class,
        UsersMapper.class})
public abstract class ShopUserMapper extends EntityMapper<ShopUser, ShopUserEntity> {

    @Override
    public abstract ShopUser toDto(ShopUserEntity entity);

    @Override
    public abstract ShopUserEntity toEntity(ShopUser entity);

}
