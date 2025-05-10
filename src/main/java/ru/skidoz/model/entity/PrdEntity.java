package ru.skidoz.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import ru.skidoz.model.entity.category.CategoryEntity;
import ru.skidoz.model.entity.category.CategoryGroupEntity;
import ru.skidoz.model.entity.category.CategorySuperGroupEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
//@Indexed
@Entity
/*@Table(name = "product",
        uniqueConstraints = { @UniqueConstraint(name = "UC_PRODUCT_COL_NAME", columnNames = {"shop_id"})},
//        , "name"
        indexes = {
                @Index(name = "IDX_PRODUCT_ID", columnList = "id"),
//                @Index(name = "IDX_PRODUCT_NAME_SHOP", columnList = "active,shop_id,name"),
                @Index(name = "IDX_PRODUCT_SHOP", columnList = "active,shop_id"),
//                @Index(name = "IDX_PRODUCT_NAME", columnList = "active,name"),
                @Index(name = "IDX_PRODUCT_POPULARITY", columnList = "popularity")})*/
public class PrdEntity extends AbstractGroupEntity  implements Serializable {

    @NotNull
    private Long chatId;

    private Integer popularity;

    private String article;
    //@NotNull
//    @Field
    private String shortText;

    //@NotNull
    private String bigText;

    //@NotNull
    private BigDecimal price;

    private Integer priceHash;

    @Lob
    @JsonIgnore
    @Column(name = "image", columnDefinition="BYTEA")
    private byte[] image;

    //@NotNull
    private BigDecimal discount;

    //@NotNull
    private boolean productService;

    private Long duration;

    //@NotNull
    @ManyToOne
    //@JoinColumn(name = "shopId", nullable = false, insertable = true, updatable = true)
    private ShopEntity shop;

    private boolean active = false;

    /*
    @ManyToMany(mappedBy = "productSet", fetch = FetchType.LAZY)
    private Set<Cat> categorySet = new HashSet<>();

    @ManyToMany(mappedBy = "productSet", fetch = FetchType.LAZY)
    private Set<CatG> categoryGroupSet = new HashSet<>();

    @ManyToMany(mappedBy = "productSet", fetch = FetchType.LAZY)
    private Set<CatSG> categorySuperGroupSet = new HashSet<>();
    */
    @NotNull
    @ManyToOne
    private CategoryEntity category;

    @NotNull
    @ManyToOne
    private CategoryGroupEntity categoryGroup;

    @NotNull
    @ManyToOne
    private CategorySuperGroupEntity categorySuperGroup;

/*
    @ManyToMany(mappedBy = "productSet")
    private Set<NameWord> nameWordSet = new HashSet<>();*/

    @OneToMany(mappedBy="product")
    private List<NameWordProductEntity> nameWordProductList = new ArrayList<>();

/*
    @OneToMany(mappedBy="product")
    private List<Purchase> purchaseList = new ArrayList<>();*/

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    private List<ScheduleDefaultEntity> scheduleDefaultList = new ArrayList<>();

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    private List<ScheduleBuyerEntity> scheduleBuyerList = new ArrayList<>();

    /*@ManyToMany(mappedBy = "productSet")
    private Set<Basket> basketList = new HashSet<>();*/

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    private List<BasketProductEntity> basketProductList = new ArrayList<>();

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    private List<BookmarkEntity> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    private List<CategoryFilterProductEntity> categoryFilterProductList = new ArrayList<>();

    @OneToMany(mappedBy="currentCreatingProduct", fetch = FetchType.LAZY)
    private List<ShopEntity> currentCreatingProductShopList = new ArrayList<>();

    public PrdEntity(Consumer<PrdEntity> builder){
        builder.accept(this);
    }

    public PrdEntity(Integer id){
        super.setId(id);
    }

    public PrdEntity() {
        super();
    }

    public void setPrice(BigDecimal price){
        if (price != null) {
            this.priceHash = price.intValue();
            this.price = price;
        }
    }

/*
    public void addCategory(Cat category) {
        this.categorySet.add(category);
    }

    public void addCategoryGroup(CatG categoryGroup) {
        this.categoryGroupSet.add(categoryGroup);
    }

    public void addCategorySuperGroup(CatSG categorySuperGroup) {
        this.categorySuperGroupSet.add(categorySuperGroup);
    }
 */
/*
    public void addPurchase(Purchase purchase) {
        this.purchaseList.add(purchase);
    }*/

    public void addScheduleDefault(ScheduleDefaultEntity scheduleDefault) {
        this.scheduleDefaultList.add(scheduleDefault);
    }

    public void addScheduleBuyer(ScheduleBuyerEntity scheduleBuyer) {
        this.scheduleBuyerList.add(scheduleBuyer);
    }

    public void addBasketProduct(BasketProductEntity basketProduct) {
        this.basketProductList.add(basketProduct);
    }

    public void addBookmark(BookmarkEntity bookmark) {
        this.bookmarkList.add(bookmark);
    }

    public void addCategoryFilterProduct(CategoryFilterProductEntity categoryFilterProduct) {
        this.categoryFilterProductList.add(categoryFilterProduct);
    }


    @Override
    public String toString() {
        return "Prd{" +
                "id=" + super.getId() +
//                ", name='" + super.getName() + '\'' +
                ", shortText='" + shortText + '\'' +
                ", bigText='" + bigText + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", productService=" + productService +
                ", duration=" + duration +
                '}';
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prd)) return false;
        if (!super.equals(o)) return false;
        Prd product = (Prd) o;
        return productService == product.productService
                && active == product.active
                && Objects.equals(shortText, product.shortText)
                && Objects.equals(bigText, product.bigText)
                && Objects.equals(price, product.price)
                && Objects.equals(discount, product.discount)
                && Objects.equals(duration, product.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shortText, bigText, price, discount, productService, duration, active);
    }*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrdEntity)) return false;
        if (!super.equals(o)) return false;
        PrdEntity action = (PrdEntity) o;
        return super.getId().equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

