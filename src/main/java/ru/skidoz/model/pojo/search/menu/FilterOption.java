package ru.skidoz.model.pojo.search.menu;

import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FilterOption extends AbstractDTO {

    private String name;
    private Integer/*FilterPointDTO*/ filterPoint;

    public FilterOption(Consumer<FilterOption> builder) {
        super();
        builder.accept(this);
    }

    public FilterOption() {
        super();
    }
}
