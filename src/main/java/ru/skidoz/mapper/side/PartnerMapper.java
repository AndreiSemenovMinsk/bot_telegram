package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.PartnerEntity;
import ru.skidoz.model.pojo.telegram.Partner;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopGroupMapper.class,
        ShopMapper.class})
public abstract class PartnerMapper extends EntityMapper<Partner, PartnerEntity> {

    @Override
    @Mapping(source = "debtor.id", target = "debtor")
    @Mapping(source = "creditor.id", target = "creditor")
    public abstract Partner toDto(PartnerEntity partner);

    @Override
//    @Mapping(source = "debtor", target = "debtor", qualifiedByName = "idShop")
//    @Mapping(source = "creditor", target = "creditor", qualifiedByName = "idShop")
    public abstract PartnerEntity toEntity(Partner partner);
}
