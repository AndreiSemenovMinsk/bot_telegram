package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.side.ShopMapper;
import ru.skidoz.model.entity.RecommendationEntity;
import ru.skidoz.model.pojo.telegram.Recommendation;

@Component
@Mapper(componentModel = "spring", uses = {ShopMapper.class, UsersMapper.class})
public abstract class RecommendationMapper extends EntityMapper<Recommendation, RecommendationEntity> {

    @Override
    @Mapping(source = "shop.id", target = "shop")
    @Mapping(source = "buyer.id", target = "buyer")
    @Mapping(source = "friend.id", target = "friend")
    public abstract Recommendation toDto(RecommendationEntity entity);

    @Override
//    @Mapping(source = "shop", target = "shop", qualifiedByName = "idShop")
//    @Mapping(source = "buyer", target = "buyer", qualifiedByName = "idUser")
//    @Mapping(source = "friend", target = "friend", qualifiedByName = "idUser")
    public abstract RecommendationEntity toEntity(Recommendation recommendation);
}
