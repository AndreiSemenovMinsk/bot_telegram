package ru.skidoz.model.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import ru.skidoz.model.entity.category.LanguageEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

/*@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)*/
//@Indexed
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name="UC_categorySuperGroup_name", columnNames={"nameru"}),
        @UniqueConstraint(name = "UC_PRODUCT_COL_NAME", columnNames = {"shop_id"})},
        indexes = {
                @Index(name = "IDX_PRODUCT_ID", columnList = "id"),
//                @Index(name = "IDX_PRODUCT_NAME_SHOP", columnList = "active,shop_id,name"),
                @Index(name = "IDX_PRODUCT_SHOP", columnList = "active,shop_id"),
//                @Index(name = "IDX_PRODUCT_NAME", columnList = "active,name"),
                @Index(name = "IDX_PRODUCT_POPULARITY", columnList = "popularity")})
public class AbstractGroupEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @CreatedDate
    private Instant time = Instant.now();

    @Size(max=50)
    private String nameEN;

    @Size(max=50)
    private String nameRU;

    @Size(max=50)
    private String nameDE;

    @NotNull
    @Size(max=50)
    private String alias;

    /*
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "i18n_AbstractGroupEntity", foreignKey = @ForeignKey(name = "fk_i18n_AbstractGroupEntity"), joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "locale")
    @Column(name = "text")
    private Map<LanguageEnum, String> nameLanguages = new HashMap<>();*/

//    @ManyToOne
//    private Action sourceAtAction;
//
//    @ManyToOne
//    private Action targetAtAction;

    @OneToMany(mappedBy = "productSource")
    private List<ActionEntity> productSourceActionList = new ArrayList<>();

    @OneToMany(mappedBy = "productTarget")
    private List<ActionEntity> productTargetActionList = new ArrayList<>();


    public AbstractGroupEntity(){
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
}

