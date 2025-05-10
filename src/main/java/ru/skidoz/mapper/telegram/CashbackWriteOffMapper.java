package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.side.ActionMapper;
import ru.skidoz.mapper.side.ShopMapper;
import ru.skidoz.model.entity.CashbackWriteOffEntity;
import ru.skidoz.model.pojo.telegram.CashbackWriteOff;

@Component
@Mapper(componentModel = "spring", uses = {ShopMapper.class, UsersMapper.class, ActionMapper.class})
public abstract class CashbackWriteOffMapper extends EntityMapper<CashbackWriteOff, CashbackWriteOffEntity> {

    @Override
//    @Mapping(target = "resultPurchaseList", ignore = true)
    @Mapping(source = "shop.id", target = "shop")
    @Mapping(source = "user.id", target = "user")
//    @Mapping(source = "action.id", target = "action")
    public abstract CashbackWriteOff toDto(CashbackWriteOffEntity entity);

    @Override
//    @Mapping(target = "resultPurchaseList", ignore = true)
    @Mapping(source = "shop", target = "shop.id")
    @Mapping(source = "user", target = "user.id")
//    @Mapping(source = "action", target = "action")
    public abstract CashbackWriteOffEntity toEntity(CashbackWriteOff cashbackWriteOff);

}
