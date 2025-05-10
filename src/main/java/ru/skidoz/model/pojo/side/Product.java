package ru.skidoz.model.pojo.side;


import java.math.BigDecimal;
import java.util.function.Consumer;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product extends AbstractDTO {


    private String article;
    private Integer popularity;

    @Size(max=50)
    private String nameEN;

    @Size(max=50)
    private String nameRU;

    @Size(max=50)
    private String nameDE;

    @NotNull
    @Size(max=150)
    private String alias;
    private boolean active = false;
    private String shortText;
    private String bigText;
    private BigDecimal price;

    private Integer priceHash;
    @JsonIgnore
    private byte[] image;
    private BigDecimal discount;
    private boolean productService;
    private Long duration;
    private Shop shop;
    private String type;
    @JsonIgnore
    private Long chatId;

    @NotNull
    private Integer/*AbstractGroupDTO*/ category;
    @NotNull
    private Integer/*AbstractGroupDTO*/ categoryGroup;
    @NotNull
    private Integer/*AbstractGroupDTO*/ categorySuperGroup;

    public Product(Consumer<Product> builder) {
        super();
        builder.accept(this);
    }

    public Product() {
        super();
    }

    public void addName(String name, LanguageEnum language) {
        if (LanguageEnum.EN.equals(language)) {
            this.nameEN = name;
        } else if (LanguageEnum.RU.equals(language)) {
            this.nameRU = name;
        } else if (LanguageEnum.DE.equals(language)) {
            this.nameDE = name;
        }
    }

    public String getName(LanguageEnum language) {
        if (LanguageEnum.EN.equals(language)) {
            return this.nameEN;
        } else if (LanguageEnum.RU.equals(language)) {
            return this.nameRU;
        } else if (LanguageEnum.DE.equals(language)) {
            return this.nameDE;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + getId() +
                ", nameRU='" + nameRU + '\'' +
                ", time=" + getTime() +
                ", shortText='" + shortText + '\'' +
                ", bigText='" + bigText + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", productService=" + productService +
                ", duration=" + duration +
                ", shopDTO=" + shop +
                ", type='" + type + '\'' +
                ", chatId=" + chatId +
                ", category=" + category +
                ", categoryGroup=" + categoryGroup +
                ", categorySuperGroup=" + categorySuperGroup +
                '}';
    }

}
