package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.telegram.ButtonRowEntity;
import ru.skidoz.model.pojo.telegram.ButtonRow;


@Component
@Mapper(componentModel = "spring", uses = {ButtonMapper.class, LevelMapper.class})
public abstract class ButtonRowMapper extends EntityMapper<ButtonRow, ButtonRowEntity> {


    @Override
//    @Mapping(source = "buttonList", target = "buttonList", qualifiedByName = "buttonList")
    @Mapping(source = "level.id", target = "level")
    public abstract ButtonRow toDto(ButtonRowEntity entity);

    @Override
    @Mapping(target = "buttonList", ignore = true)
    @Mapping(source = "level", target = "level.id")//, qualifiedByName = "idLevel")
    public abstract ButtonRowEntity toEntity(ButtonRow buttonRow);

//
//    @Named("buttonList")
//    public List<Button> buttonList(Integer id) {
//        Integer resId;
//        if (id == null) {
//            return null;
//        } else if (id < 0) {
//            resId = buttonRowRepository.getStoredId(id);
//            if (resId == null){
//                return null;
//            }
//        } else {
//            resId = id;
//        }
//        return buttonRowRepository.findById(resId).orElse(null);
//    }
}
