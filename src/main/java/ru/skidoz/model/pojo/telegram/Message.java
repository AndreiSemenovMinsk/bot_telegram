package ru.skidoz.model.pojo.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author andrey.semenov
 */

@Setter
@Getter
public class Message extends AbstractDTO {

    private Integer/*LevelDTO*/ level;

    private Integer levelID;
    @Size(max=160)
    private String nameEN;
    @Size(max=160)
    private String nameRU;
    @Size(max=160)
    private String nameDE;

    private byte[] image;

    private String imageDescription;
    private Double longitude;
    private Double latitude;

    private List<User> usersWithChangingMessageList = new ArrayList<>();

    public Message(Level newLevel,
                   Integer levelID,
                   String nameEN,
                   String nameRU,
                   String nameDE,
                   byte[] image,
                   String imageDescription,
                   Double longitude,
                   Double latitude) {
        super();
        this.level = newLevel.getId();
        this.levelID = levelID;
        this.nameEN = nameEN;
        this.nameRU = nameRU;
        this.nameDE = nameDE;
        this.image = image;
        this.imageDescription = imageDescription;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Message(Level newLevel,
                   Integer levelID,
                   Map<LanguageEnum, String> nameLanguages,
                   byte[] image,
                   String imageDescription,
                   Double longitude,
                   Double latitude) {
        super();
        this.level = newLevel.getId();
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

    public Message(Level newLevel,
                   Integer levelID,
                   Map<LanguageEnum, String> nameLanguages,
                   byte[] image,
                   String imageDescription) {
        super();
        if (newLevel != null) {
            this.level = newLevel.getId();
        }
        this.levelID = levelID;
        if (nameLanguages != null) {
            nameLanguages.forEach((k, v) -> {
                addName(v, k);
            });
        }
        this.image = image;
        this.imageDescription = imageDescription;
    }

    public Message(Level level,
                   Map<LanguageEnum, String> nameLanguages) {
        super();
        if (level != null) {
            this.level = level.getId();
        }
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
    }

    public Message(Level level,
                   Integer levelID,
                   Double lat,
                   Double lng) {
        super();
        if (level != null) {
            this.level = level.getId();
        }
        this.levelID = levelID;
        this.longitude = lng;
        this.latitude = lat;
    }

    public Message(Level level,
                   Integer levelID,
                   Map<LanguageEnum, String> nameLanguages) {
        super();
        if (level != null) {
            this.level = level.getId();
        }
        this.levelID = levelID;
        nameLanguages.forEach((k, v) -> {
            addName(v, k);
        });
    }

    public Message(){
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
                "id=" + getId() +
                ", level=" + level +
                ", levelID=" + levelID +
                ", nameRU='" + nameRU + "'" +
                ", nameDE='" + nameDE +
                ", nameEN='" + nameEN +
                ", imageDescription='" + imageDescription + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
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
