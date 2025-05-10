package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.side.ShopMapper;
import ru.skidoz.model.entity.BotEntity;
import ru.skidoz.model.pojo.telegram.Bot;

@Component
@Mapper(componentModel = "spring", uses = { LevelMapper.class, ShopMapper.class})
public abstract class BotMapper extends EntityMapper<Bot, BotEntity> {

    @Override
    @Mapping(target = "buyerBotList", ignore = true)
    @Mapping(target = "currentChangingBotUsersList", ignore = true)
    @Mapping(target = "levelList", ignore = true)
    @Mapping(source = "shop.id", target = "shop")
    public abstract Bot toDto(BotEntity entity);

    @Override
    @Mapping(target = "buyerBotList", ignore = true)
    @Mapping(target = "currentChangingBotUsersList", ignore = true)
    @Mapping(target = "levelList", ignore = true)
//    @Mapping(source = "shop", target = "shop", qualifiedByName = "idShop")
    @Mapping(source = "initialLevel", target = "initialLevel")
    public abstract BotEntity toEntity(Bot bot);

}
