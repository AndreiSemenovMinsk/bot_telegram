package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.entity.CashbackEntity;
import ru.skidoz.model.pojo.side.Cashback;

@Component
@Mapper(componentModel = "spring", uses = {ShopMapper.class, UsersMapper.class, PurchaseMapper.class, ActionMapper.class})
public abstract class CashbackMapper extends EntityMapper<Cashback, CashbackEntity> {

    @Override
    @Mapping(source = "user.id", target = "user")
    @Mapping(source = "shop.id", target = "shop")
    @Mapping(source = "action.id", target = "action")
    @Mapping(source = "purchase.id", target = "purchase")
    public abstract Cashback toDto(CashbackEntity entity);

    @Override
    @Mapping(source = "user", target = "user.id")//, qualifiedByName = "idUser")
    @Mapping(source = "shop", target = "shop.id")//, qualifiedByName = "idShop")
    @Mapping(source = "action", target = "action.id")//, qualifiedByName = "idAction")
    @Mapping(source = "purchase", target = "purchase.id")//, qualifiedByName = "idPurchase")
    public abstract CashbackEntity toEntity(Cashback cashback);
}
