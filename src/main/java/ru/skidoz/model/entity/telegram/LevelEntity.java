package ru.skidoz.model.entity.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.skidoz.model.entity.AbstractEntity;
import ru.skidoz.model.entity.BotEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
@Entity
@Getter
@Setter
@Table(name = "level",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_LEVEL_COL_USER_CALL_NAME", columnNames = {"callName", "user_id"})},
        indexes = {
                //@Index(name = "IDX_LEVEL_COL_ID", columnList = "id"),
                @Index(name = "IDX_LEVEL_COL_CALLNAME", columnList = "callName"),
                @Index(name = "IDX_LEVEL_COL_USER", columnList = "user_id"),
                @Index(name = "IDX_LEVEL_COL_PARENTLEVEL", columnList = "parent_level_id")})
public class LevelEntity extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Size(max=80)
    private String callName;

    private boolean sourceIsMessage;

    private boolean terminateBotLevel = false;

    private boolean botLevel = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private BotEntity bot;

    //bot.getShop().getUsers().getChatId()
    private Long chatId;

    /*@OneToOne(mappedBy = "initialLevel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)*/
    private Integer initialLevelBot;

    /*@OneToMany(fetch = FetchType.LAZY, mappedBy="currentLevel")
    private List<Users> userWithCurrentLevelList = new ArrayList<>();*/

//    @OneToMany(mappedBy="currentLevelBeforeInterruption")
//    private List<UserEntity> usersWithCurrentLevelBeforeInterruptionList = new ArrayList<>();

    /*@OneToMany(mappedBy = "parentLevelId", fetch = FetchType.LAZY)//, fetch = FetchType.EAGER)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Singular
    private List<Level> childLevels = new ArrayList<>();*/

    //@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "parent_level_id")
    private Integer parentLevelId;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)//, fetch = FetchType.EAGER)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<MessageEntity> messages = new ArrayList<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)//, fetch = FetchType.EAGER)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<ButtonRowEntity> buttonRows = new ArrayList<>();
/*
    @OneToMany(mappedBy = "level")//, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Button> buttonList = new ArrayList<>();*/

    public LevelEntity(UserEntity user, String callName, LevelEntity parentLevel, Boolean sourceIsMessage) {
        this.user = user;
        this.callName = callName;
        this.parentLevelId = parentLevel.getId();
        this.sourceIsMessage = sourceIsMessage;
        this.terminateBotLevel = false;
        this.botLevel = false;
        /*if (parentLevelId != null) {
            parentLevelId.addChildLevel(this);
        }*/
    }

    public LevelEntity() {
        super();
    }

    public void updateLevel(UserEntity users, String callName, LevelEntity parentLevel, Boolean sourceIsMessage) {
        this.user = users;
        this.callName = callName;
        if (parentLevel != null) {
            this.parentLevelId = parentLevel.getId();
        }
        this.sourceIsMessage = sourceIsMessage;
        /*if (parentLevel != null) {
            parentLevel.addChildLevel(this);
        }*/
    }

    public void updateLevel(UserEntity users, String callName, LevelEntity parentLevel, Boolean sourceIsMessage, Boolean isTerminate, Boolean isBotLevel) {
        this.user = users;
        this.callName = callName;
        if (parentLevel != null) {
            this.parentLevelId = parentLevel.getId();
        }
        this.sourceIsMessage = sourceIsMessage;
        this.terminateBotLevel = isTerminate;
        this.botLevel = isBotLevel;
        /*if (parentLevel != null) {
            parentLevel.addChildLevel(this);
        }*/
    }

    public void addMessage(MessageEntity message){
        messages.add(message);
    }

    public void setMessage(Integer id, MessageEntity message){
        messages.set(id, message);
    }

    public void prependMessage(MessageEntity message){
        messages.add(0, message);
    }

    /*public void addChildLevel(Level level){
        childLevels.add(level);
    }*/
    //@DeclareWarning("do not use without saveNew - use DTO")
    public void addRow(ButtonRowEntity buttonRow){
        buttonRows.add(buttonRow);
    }


    public LevelEntity cloneShallow() throws CloneNotSupportedException {

        LevelEntity level = (LevelEntity) super.clone();

        level.user = this.user;
        level.callName = this.callName;
        level.sourceIsMessage = this.sourceIsMessage;
        level.parentLevelId = this.parentLevelId;

        return level;
    }

    @Override
    public LevelEntity clone() throws CloneNotSupportedException {

        LevelEntity level = (LevelEntity) super.clone();

        level.user = user;
        level.callName = this.callName;
        level.sourceIsMessage = this.sourceIsMessage;
        level.parentLevelId = this.parentLevelId;
        return level;
    }

    public String getIdString(){
        return getIdString(super.id);
    }

    public static String getIdString(Integer id){
        return StringUtils.leftPad("" + id, 19, "0");
    }

    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", callName='" + callName + '\'' +
                ", users.id=" + (user != null ? user.getId() : "null") +
                ", sourceIsMessage=" + sourceIsMessage +
                ", initialLevelBot=" + initialLevelBot +
                ", parentLevelId=" + parentLevelId +
                ", isBotLevel=" + botLevel +
                ", isTerminate=" + terminateBotLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LevelEntity)) return false;
        if (!super.equals(o)) return false;
        LevelEntity level = (LevelEntity) o;
        return super.id.equals(level.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}
