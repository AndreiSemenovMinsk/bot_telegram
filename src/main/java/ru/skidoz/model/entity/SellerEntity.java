package ru.skidoz.model.entity;

/**
 * @author andrey.semenov
 */
/*@Entity
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "sellers",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_USER_COL_LOGIN", columnNames = {"user", "shop"})},
        indexes = {
                @Index(name = "IDX_SELLER_COL_USER_SHOP", columnList = "user, shop"),
                @Index(name = "IDX_SELLER_COL_SHOP", columnList = "shop")})*/
public class SellerEntity extends AbstractEntity {

//    private Long expirationTimeMilliseconds;
//
//    @ManyToOne
//    private User user;
//
//    @ManyToOne
//    private Shop shop;
//
//    public Seller(Integer id) {
//        super.id = id;
//    }
//
//    public Seller(Consumer<Seller> builder){
//        builder.accept(this);
//    }
//
//    public Seller() {
//
//    }
//
//    @Override
//    public String toString() {
//        return "Seller {" +
//                "id=" + id +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//
//        System.out.println("(o instanceof Users)+++++++"+(o instanceof Sellers));
//        System.out.println("super.equals(o)+++++++++++"+super.equals(o));
//
//        if (this == o) return true;
//        if (!(o instanceof Seller)) return false;
//        if (!super.equals(o)) return false;
//        Seller seller = (Seller) o;
//
//        System.out.println("super.id+++" + super.id + "+++++seller.getId()+" + seller.getId());
//        System.out.println(super.id.equals(seller.getId()));
//
//        return super.id.equals(seller.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), super.getId());
//    }
}
