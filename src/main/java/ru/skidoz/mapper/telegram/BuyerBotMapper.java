package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.BuyerBotEntity;
import ru.skidoz.model.pojo.telegram.BuyerBot;

@Component
@Mapper(componentModel = "spring", uses = {UsersMapper.class, BotMapper.class})
public abstract class BuyerBotMapper extends EntityMapper<BuyerBot, BuyerBotEntity> {

    @Override
    @Mapping(target = "buyerBotMessageList", ignore = true)
    @Mapping(source = "bot.id", target = "bot")
    @Mapping(source = "user.id", target = "user")
    public abstract BuyerBot toDto(BuyerBotEntity entity);

    @Override
    @Mapping(source = "bot", target = "bot.id")//, qualifiedByName = "idBot")
    @Mapping(source = "user", target = "user.id")//, qualifiedByName = "idUser")
    @Mapping(target = "buyerBotMessageList", ignore = true)
    public abstract BuyerBotEntity toEntity(BuyerBot buyerBot);
}
