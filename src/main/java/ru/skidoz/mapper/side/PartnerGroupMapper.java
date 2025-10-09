package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.PartnerGroupEntity;
import ru.skidoz.model.pojo.telegram.PartnerGroup;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopGroupMapper.class,
        ShopMapper.class})
public abstract class PartnerGroupMapper extends EntityMapper<PartnerGroup, PartnerGroupEntity> {

    @Override
    @Mapping(source = "shopGroup.id", target = "shopGroup")//, qualifiedByName = "idBot")
    @Mapping(source = "shop.id", target = "shop")
    public abstract PartnerGroup toDto(PartnerGroupEntity partnerGroup);

    @Override
//    @Mapping(source = "shopGroup", target = "shopGroup.id")//, qualifiedByName = "idShopGroup")
//    @Mapping(source = "creditor", target = "creditor", qualifiedByName = "idShop")
    @Mapping(source = "shopGroup", target = "shopGroup.id")//, qualifiedByName = "idBot")
    @Mapping(source = "shop", target = "shop.id")//, qualifiedByName = "idBot")
    public abstract PartnerGroupEntity toEntity(PartnerGroup partnerGroup);
}
