package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.entity.PurchaseEntity;
import ru.skidoz.model.pojo.main.Purchase;

@Component
@Mapper(componentModel = "spring", uses = {
        UsersMapper.class,
        ShopMapper.class})
public abstract class PurchaseMapper extends EntityMapper<Purchase, PurchaseEntity> {

    @Override
    @Mapping(source = "buyer.id", target = "buyer")
    @Mapping(source = "shop.id", target = "shop")
    public abstract Purchase toDto(PurchaseEntity purchase);

    @Override
//    @Mapping(source = "users", target = "buyer", qualifiedByName = "idUser")
//    @Mapping(source = "shop", target = "shop", qualifiedByName = "idShop")
    public abstract PurchaseEntity toEntity(Purchase purchase);

}
