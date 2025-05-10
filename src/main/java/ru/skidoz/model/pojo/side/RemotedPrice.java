package ru.skidoz.model.pojo.side;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

//import org.springframework.data.redis.core.RedisHash;

//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
//@Entity
//@RedisHash("RemotedPrice")
public class RemotedPrice implements Serializable {

    private String id;

    private String url;

    private Integer bidPrice;

    @NotNull
    private Integer shop;

    private Long chatId;

    @NotNull
    private String productId;

    public RemotedPrice(Consumer<RemotedPrice> builder){
        builder.accept(this);
    }

    public RemotedPrice() {
        super();
    }


    @Override
    public String toString() {
        return "RemotedPrice{ id=" + id +
                ", bidPrice=" + bidPrice +
                ", chatId=" + chatId +
                ", shop=" + shop +
                ", productId=" + productId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemotedPrice)) return false;
        if (!super.equals(o)) return false;
        RemotedPrice action = (RemotedPrice) o;
        return chatId.equals(action.getChatId()) && productId.equals(action.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chatId, productId);
    }
}

