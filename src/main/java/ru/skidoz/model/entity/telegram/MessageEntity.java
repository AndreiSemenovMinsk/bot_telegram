package ru.skidoz.model.entity.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.skidoz.model.entity.AbstractEntity;
import ru.skidoz.model.entity.category.LanguageEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author andrey.semenov
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "message",
        indexes = {
                @Index(name = "IDX_MESSAGE_COL_ID", columnList = "id")})
public class MessageEntity extends AbstractEntity {

    @ManyToOne
    private LevelEntity level;

    private Integer levelID;

    @Size(max=160)
    private String nameEN;

    @Size(max=160)
    private String nameRU;

    @Size(max=160)
    private String nameDE;

//    private String text;
//
//    @Size(max=50)
//    private String alias;

/*    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "i18n_Message", foreignKey = @ForeignKey(name = "fk_i18n_Message"), joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "locale")
    @Column(name = "text")
    private Map<LanguageEnum, String> nameLanguages = new HashMap<>();*/

    @Lob
    private byte[] image;

    private String imageDescription;
    private Double longitude;
    private Double latitude;

//    @OneToMany(mappedBy="currentChangingMessage", fetch = FetchType.LAZY)
//    private List<UserEntity> usersWithChangingMessageList = new ArrayList<>();

//    public MessageEntity(LevelEntity newLevel,
//                         Integer levelID,
//                         String nameEN,
//                         String nameRU,
//                         String nameDE,
//                         byte[] image,
//                         String imageDescription,
//                         Double longitude,
//                         Double latitude) {
//        this.level = newLevel;
//        this.levelID = levelID;
//        this.nameEN = nameEN;
//        this.nameRU = nameRU;
//        this.nameDE = nameDE;
//        this.image = image;
//        this.imageDescription = imageDescription;
//        this.longitude = longitude;
//        this.latitude = latitude;
//    }

    public MessageEntity(LevelEntity newLevel,
                         Integer levelID,
                         Map<LanguageEnum, String> nameLanguages,
                         byte[] image,
                         String imageDescription,
                         Double longitude,
                         Double latitude) {
        this.level = newLevel;
        this.levelID = levelID;
        if (nameLanguages != null) {
            nameLanguages.forEach((k, v) -> {
                addName(v, k);
            });
        }
        this.image = image;
        this.imageDescription = imageDescription;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public MessageEntity(LevelEntity newLevel,
                         Integer levelID,
                         Map<LanguageEnum, String> nameLanguages,
                         byte[] image,
                         String imageDescription) {
        this.level = newLevel;
        this.levelID = levelID;
        if (nameLanguages != null) {
            nameLanguages.forEach((k, v) -> {
                addName(v, k);
            });
        }
        this.image = image;
        this.imageDescription = imageDescription;
    }

    public MessageEntity(LevelEntity level,
                         Map<LanguageEnum, String> nameLanguages) {
        this.level = level;
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
    }

    public MessageEntity(LevelEntity level,
                         Integer levelID,
                         Map<LanguageEnum, String> nameLanguages) {
        this.level = level;
        this.levelID = levelID;
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
    }

    public MessageEntity() {
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
        return "Message{" +
                "id=" + id +
                ", nameRU='" + nameRU + "'" +
                ", nameDE='" + nameDE +
                ", nameEN='" + nameEN +
                ", imageDescription='" + imageDescription + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEntity)) return false;
        if (!super.equals(o)) return false;
        MessageEntity message = (MessageEntity) o;
        return super.id.equals(message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }

    public String getText(LanguageEnum language) {
        if (LanguageEnum.EN.equals(language)) {
            return this.nameEN;
        } else if (LanguageEnum.RU.equals(language)) {
            return this.nameRU;
        } else if (LanguageEnum.DE.equals(language)) {
            return this.nameDE;
        }
        return null;
    }

    public void setText(LanguageEnum language, String name) {
        if (LanguageEnum.EN.equals(language)) {
            this.nameEN = name;
        } else if (LanguageEnum.RU.equals(language)) {
            this.nameRU = name;
        } else if (LanguageEnum.DE.equals(language)) {
            this.nameDE = name;
        }
    }
}
