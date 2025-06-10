package ru.skidoz.mapper.telegram;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.telegram.ButtonEntity;
import ru.skidoz.model.pojo.telegram.Button;

@Component
@Mapper(componentModel = "spring", uses = {ButtonRowMapper.class})
public abstract class ButtonMapper extends EntityMapper<Button, ButtonEntity> {

//    @Mapping(target = "usersWithCurrentChangingButtonList", ignore = true)
    @Mapping(source = "buttonRow.id", target = "buttonRow")
    @Mapping(source = "buttonRow.level.id", target = "level")
    public abstract Button toDto(ButtonEntity entity);

//    @Mapping(target = "usersWithCurrentChangingButtonList", ignore = true)
    @Mapping(source = "buttonRow.id", target = "buttonRow")
    @Mapping(source = "buttonRow.level.id", target = "level")
    public abstract List<Button> toDto(List<ButtonEntity> entity);

//    @Mapping(target = "usersWithCurrentChangingButtonList", ignore = true)
    @Mapping(source = "buttonRow", target = "buttonRow.id")//, qualifiedByName = "idButtonRow")
    public abstract ButtonEntity toEntity(Button entity);

}
