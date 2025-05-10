package ru.skidoz.model.pojo.search.search;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MenuSearch extends AbstractDTO {

    private Integer categoryId;
    private List<SearchPoint> searchPointList = new ArrayList<>();

    public MenuSearch(Consumer<MenuSearch> builder){
        super();
        builder.accept(this);
    }

    public MenuSearch() {
        super();
    }
}
