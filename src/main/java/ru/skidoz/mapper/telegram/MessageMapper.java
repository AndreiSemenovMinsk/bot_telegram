package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.telegram.MessageEntity;
import ru.skidoz.model.pojo.telegram.Message;

@Component
@Mapper(componentModel = "spring", uses = {/*JpaRepository.class, */LevelMapper.class})
public abstract class MessageMapper extends EntityMapper<Message, MessageEntity> {

    @Override
    @Mapping(target = "usersWithChangingMessageList", ignore = true)
    @Mapping(source = "level.id", target = "level")
    public abstract Message toDto(MessageEntity entity);

    @Override
    @Mapping(target = "usersWithChangingMessageList", ignore = true)
    @Mapping(source = "level", target = "level.id")//, qualifiedByName = "idLevel")
    public abstract MessageEntity toEntity(Message message);

}
