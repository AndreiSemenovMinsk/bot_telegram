package ru.skidoz.model.pojo.search.response;

import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Setter
@Getter
@ToString
public class CategorySuperGroupResponse extends AbstractDTO {

    private List<CategorySuperGroup> categorySuperGroupDTOS = new ArrayList<>();
    private Integer elementNumber;

    public CategorySuperGroupResponse(Consumer<CategorySuperGroupResponse> builder) {
        super();
        builder.accept(this);
    }

    public CategorySuperGroupResponse() {
        super();
    }
}
