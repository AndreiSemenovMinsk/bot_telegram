package ru.skidoz.model.pojo;

import java.time.Instant;

import lombok.Data;
import ru.skidoz.model.DTO;


@Data
public class AbstractDTO extends DTO {

    //private int id;
    //@JsonIgnore
    private Instant time = Instant.now();

//    @JsonIgnore
//    List<Object> collections = new ArrayList<>();

    public AbstractDTO(Integer id) {
        super.setId(id);
    }

    public AbstractDTO() {
        super();
        //this.id = super.getId();
//        this.setId(-(long) (Math.random() * 1_000_000_000L));
    }

//    public void clear() {
//        collections.forEach(e -> {
//            Object[] o = (Object[]) e;
//
//            //((Map) o[1]).remove(o[0]);
//        });
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractDTO)) return false;
        //if (!super.equals(o)) return false;
        if (super.getId() != null && ((AbstractDTO) o).getId() != null) return super.getId().equals(((AbstractDTO) o).getId());
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        if (super.getId() == null) {
            return super.hashCode();
        } else {
            return super.getId();
        }
        //Objects.hash(super.hashCode(), id);
    }
}

