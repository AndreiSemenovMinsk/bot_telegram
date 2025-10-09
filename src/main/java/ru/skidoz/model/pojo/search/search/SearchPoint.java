package ru.skidoz.model.pojo.search.search;

import java.util.ArrayList;
import java.util.List;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SearchPoint extends AbstractDTO {

    // для селекта берется optionId, а для диапазона - minValue-maxValue
//    private Integer minValue;
//    private Integer maxValue;
    private Integer minValue;
    private Integer maxValue;
    private List<Integer> optionIdList = new ArrayList<>();

    public SearchPoint() {
        super();
    }
}
