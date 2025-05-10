package ru.skidoz.model.pojo.search.search;

import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.function.Consumer;

@Setter
@Getter
@ToString
public class Search extends AbstractDTO {

    private String search;
    private MenuSearch menuSearch;
    private Double maxLat;
    private Double maxLng;
    private Double minLat;
    private Double minLng;
    private Integer priceMin;
    private Integer priceMax;
    private String sortDir;
    private String sortBase;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer categorySuperGroupId;
    private Integer categoryGroupId;
    private Integer categoryId;

    public Search(Consumer<Search> builder){
        super();
        builder.accept(this);
    }

    public Search() {
        super();
    }
}
