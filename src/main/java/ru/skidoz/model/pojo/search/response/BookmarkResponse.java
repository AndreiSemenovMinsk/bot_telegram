package ru.skidoz.model.pojo.search.response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.side.Bookmark;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BookmarkResponse extends AbstractDTO {

    private List<Bookmark> bookmarkResponseDTOS = new ArrayList<>();
    private Integer elementNumber;

    public BookmarkResponse(Consumer<BookmarkResponse> builder) {
        super();
        builder.accept(this);
    }

    public BookmarkResponse() {
        super();
    }
}
