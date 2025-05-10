package ru.skidoz.model.pojo.search.menu;

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
public class Menu extends AbstractDTO {

    private Integer categoryId;
    private List<FilterPoint> filterPointList = new ArrayList<>();

    public Menu(Consumer<Menu> builder) {
        super();
        builder.accept(this);
    }

    public Menu() {
        super();
    }

    public void addFilterPoint (FilterPoint filterPoint){
        filterPointList.add(filterPoint);
    }
}
