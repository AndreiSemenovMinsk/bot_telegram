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
    @Mapping(source = "debtor.id", target = "debtor")
    @Mapping(source = "creditor.id", target = "creditor")
    public abstract PartnerGroup toDto(PartnerGroupEntity partnerGroup);

    @Override
    @Mapping(source = "debtor", target = "debtor.id")//, qualifiedByName = "idShopGroup")
//    @Mapping(source = "creditor", target = "creditor", qualifiedByName = "idShop")
    public abstract PartnerGroupEntity toEntity(PartnerGroup partnerGroup);
}
