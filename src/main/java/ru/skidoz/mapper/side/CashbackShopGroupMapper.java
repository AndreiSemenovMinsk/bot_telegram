package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.entity.PurchaseShopGroupEntity;
import ru.skidoz.model.pojo.telegram.PurchaseShopGroup;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopGroupMapper.class,
        ShopMapper.class,
        PurchaseMapper.class,
        UsersMapper.class})
public abstract class CashbackShopGroupMapper extends EntityMapper<PurchaseShopGroup, PurchaseShopGroupEntity> {

    @Override
    @Mapping(source = "shop.id", target = "shop")
    @Mapping(source = "shopGroup.id", target = "shopGroup")
    @Mapping(source = "purchase.id", target = "purchase")
    @Mapping(source = "user.id", target = "user")
    public abstract PurchaseShopGroup toDto(PurchaseShopGroupEntity entity);

    @Override
    @Mapping(source = "shop", target = "shop.id")//, qualifiedByName = "idShop")
    @Mapping(source = "shopGroup", target = "shopGroup.id")//, qualifiedByName = "idShopGroup")
    @Mapping(source = "purchase", target = "purchase.id")//, qualifiedByName = "idPurchase")
    @Mapping(source = "user", target = "user.id")//, qualifiedByName = "idUser")
    public abstract PurchaseShopGroupEntity toEntity(PurchaseShopGroup dto);

}
