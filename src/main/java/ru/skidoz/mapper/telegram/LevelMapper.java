package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.telegram.LevelEntity;
import ru.skidoz.model.pojo.telegram.Level;

@Component
@Mapper(componentModel = "spring", uses = {  UsersMapper.class, ButtonRowMapper.class, MessageMapper.class, BotMapper.class })
public abstract class LevelMapper extends EntityMapper<Level, LevelEntity> {

    @Override
    @Mapping(target = "usersWithCurrentLevelBeforeInterruptionList", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "buttonRows", ignore = true)
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "bot.id", target = "bot")
    public abstract Level toDto(LevelEntity entity);

    @Override
//    @Mapping(source = "users", target = "users", qualifiedByName = "idUser")
    @Mapping(source = "bot", target = "bot.id")//, qualifiedByName = "idBot")
    @Mapping(target = "usersWithCurrentLevelBeforeInterruptionList", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "buttonRows", ignore = true)
    public abstract LevelEntity toEntity(Level level);

}
