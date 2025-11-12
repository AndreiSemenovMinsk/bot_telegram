package ru.skidoz.mapper.side;

import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.entity.BasketEntity;
import ru.skidoz.model.entity.ShopGroupAddVoteEntity;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.telegram.ShopGroupAddVote;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopMapper.class,
        ShopGroupMapper.class})
public abstract class ShopGroupAddVoteMapper extends EntityMapper<ShopGroupAddVote, ShopGroupAddVoteEntity> {

    @Override
    @Mapping(source = "addingInitiatorShop.id", target = "addingInitiatorShop")
    @Mapping(source = "addingShop.id", target = "addingShop")
    @Mapping(source = "voterShop.id", target = "voterShop")
    @Mapping(source = "shopGroup.id", target = "shopGroup")
    public abstract ShopGroupAddVote toDto(ShopGroupAddVoteEntity entity);

    @Override
    @Mapping(source = "addingInitiatorShop", target = "addingInitiatorShop.id")
    @Mapping(source = "addingShop", target = "addingShop.id")
    @Mapping(source = "voterShop", target = "voterShop.id")
    @Mapping(source = "shopGroup", target = "shopGroup.id")
    public abstract ShopGroupAddVoteEntity toEntity(ShopGroupAddVote dto);

}
