package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.BuyerBotMessageEntity;
import ru.skidoz.model.pojo.telegram.BuyerBotMessage;

@Component
@Mapper(componentModel = "spring", uses = {})
public abstract class BuyerBotMessageMapper extends EntityMapper<BuyerBotMessage, BuyerBotMessageEntity> {

    @Override
    @Mapping(source = "buyerBot.id", target = "buyerBot")
    public abstract BuyerBotMessage toDto(BuyerBotMessageEntity entity);

    @Override
    @Mapping(source = "buyerBot", target = "buyerBot.id")
    public abstract BuyerBotMessageEntity toEntity(BuyerBotMessage buyerBotMessage);
}
