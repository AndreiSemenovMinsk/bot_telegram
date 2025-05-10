package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.entity.BasketEntity;
import ru.skidoz.model.pojo.side.Basket;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopMapper.class,
        UsersMapper.class,
        BasketProductMapper.class})
public abstract class BasketMapper extends EntityMapper<Basket, BasketEntity> {

    @Override
    @Mapping(source = "user.id", target = "user")
    @Mapping(source = "shop.id", target = "shopId")
//    @Mapping(target = "basketProductList", ignore = true)
    public abstract Basket toDto(BasketEntity entity);

    @Override
//    @Mapping(source = "users", target = "users", qualifiedByName = "idUser")
//    @Mapping(source = "shop", target = "shop", qualifiedByName = "idShop")
//    @Mapping(target = "basketProductList", ignore = true)
    public abstract BasketEntity toEntity(Basket basket);

}
