package ru.skidoz.mapper.side;


import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.ActionEntity;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.util.Structures;

@Component
@Mapper(componentModel = "spring", uses = { ShopMapper.class, ProductMapper.class })
public abstract class ActionMapper extends EntityMapper<Action, ActionEntity> {

    @Override
    @Mapping(source = "action", target = "levelRatePreviousPurchaseList",
            qualifiedByName = "getLevelRatePreviousPurchase")
    @Mapping(source = "productSource.id", target = "productSource")
    @Mapping(source = "productTarget.id", target = "productTarget")
    @Mapping(source = "shop.id", target = "shop")
    @Mapping(source = "levelSumString", target = "levelSumList",
            qualifiedByName = "setLevelSumList")
    public abstract Action toDto(ActionEntity action);


    @Override
//    @Mapping(source = "shop", target = "shop", qualifiedByName = "idShop")
    @Mapping(source = "productSource", target = "productSource.id")//, qualifiedByName = "idPrd")
    @Mapping(source = "productTarget", target = "productTarget.id")//, qualifiedByName = "idPrd")
    @Mapping(source = "levelRatePreviousPurchaseList", target = "levelRatePreviousPurchaseList",
            qualifiedByName = "getLevelRatePreviousPurchase")
    @Mapping(source = "levelSumList", target = "levelSumString",
            qualifiedByName = "setLevelSumString")
    public abstract ActionEntity toEntity(Action action);


    public ActionEntity toEntity(Integer actionId) {
        ActionEntity action = new ActionEntity();
        action.setId(actionId);
        return action;
    }

    @Named("setLevelSumString")
    public String setLevelSumString(List<Integer> levelSumList) {
        List<String> strings = new ArrayList<>(levelSumList.size());
        for (Integer levelSum : levelSumList) {
            strings.add(levelSum.toString());
        }
        return String.join(":", strings);
    }

    @Named("setLevelSumList")
    public List<Integer> getLevelSumList(String levelSumString) {
        List<Integer> result = new ArrayList<>();
        if (levelSumString != null && !levelSumString.equals("")) {
            String[] arr = levelSumString.split(":");
            for (String str : arr) {
                result.add(Integer.valueOf(str));
            }
        }
        return result;
    }

    @Named("getLevelRatePreviousPurchase")
    public List<Integer> getLevelRatePreviousPurchase(ActionEntity action) {

        if (action.getLevelRatePreviousPurchaseList() == null) {
            return new ArrayList<>();
        }
        action.accessLevelRatePreviousPurchaseList().forEach(e -> System.out.println("QQQ+++getLevelRatePreviousPurchase*****" + e));

        return new ArrayList<>(action.accessLevelRatePreviousPurchaseList());
    }

    @Named("getLevelRatePreviousPurchase")
    public String getLevelRatePreviousPurchase(List<Integer> levelRatePreviousPurchaseList) {

        if (levelRatePreviousPurchaseList.size() > 0) {
            String result = "";

            for (Integer i : levelRatePreviousPurchaseList) {
                result += ":" + i.toString();
            }

            return result.substring(1);
        }
        return null;
    }

}
