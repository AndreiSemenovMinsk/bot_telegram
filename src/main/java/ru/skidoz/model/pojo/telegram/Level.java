package ru.skidoz.model.pojo.telegram;

import java.util.ArrayList;
import java.util.List;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


/**
 * @author andrey.semenov
 */
@Setter
@Getter
public class Level extends AbstractDTO implements Cloneable {

    private Integer/*User*/ userId;
    @Size(max=80)
    private String callName;
    private boolean sourceIsMessage;
    private boolean terminateBotLevel = false;
    private boolean botLevel = false;
    private Integer/*BotDTO*/ bot;
    private Long chatId;
    private Integer initialLevelBot;
    private Integer parentLevelId;


//    private List<User> usersWithCurrentLevelBeforeInterruptionList = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<ButtonRow> buttonRows = new ArrayList<>();
//    private List<Button> buttons = new ArrayList<>();

    public Level() {
        super();
    }

    public Level(User userId, String callName, Level parentLevel, Boolean sourceIsMessage) {

        super();
        this.userId = userId.getId();
        this.callName = callName;
        this.parentLevelId = parentLevel.getId();
        this.sourceIsMessage = sourceIsMessage;
        this.terminateBotLevel = false;
        this.botLevel = false;
        /*if (parentLevel != null) {
            parentLevel.addChildLevel(this);
        }*/
    }

    public void updateLevel(User user, String callName, Level parentLevel, Boolean sourceIsMessage) {
        this.userId = user.getId();
        this.callName = callName;
        if (parentLevel != null) {
            this.parentLevelId = parentLevel.getId();
        }
        this.sourceIsMessage = sourceIsMessage;
        /*if (parentLevel != null) {
            parentLevel.addChildLevel(this);
        }*/
    }

    public void updateLevel(User user, String callName, Level parentLevel, Boolean sourceIsMessage, Boolean isTerminate, Boolean isBotLevel) {
        this.userId = user.getId();
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

    public void addMessage(Message message){
        messages.add(message);
    }

    public void setMessage(Integer id, Message message){
        messages.set(id, message);
    }

    public void prependMessage(Message message){
        messages.add(0, message);
    }

    /*public void addChildLevel(Level(elevel){
        childLevels.add(level);
    }*/
    //@DeclareWarning("do not use without saveNew - use DTO")
    public void addRow(ButtonRow buttonRow){
        buttonRows.add(buttonRow);
    }


    public Level cloneShallow() throws CloneNotSupportedException {

        Level level = (Level) super.clone();

        level.userId = this.userId;
        level.callName = this.callName;
        level.sourceIsMessage = this.sourceIsMessage;
        level.parentLevelId = this.parentLevelId;

        return level;
    }

    @Override
    public Level clone() throws CloneNotSupportedException {

        Level level = (Level) super.clone();

        level.userId = userId;
        level.callName = this.callName;
        level.sourceIsMessage = this.sourceIsMessage;
        level.parentLevelId = this.parentLevelId;
        return level;
    }

    public Level clone(User user) throws CloneNotSupportedException {

        Level level = (Level) super.clone();

        level.userId = user.getId();
        level.chatId = user.getChatId();
        level.callName = this.callName;
        level.sourceIsMessage = this.sourceIsMessage;
        level.parentLevelId = this.parentLevelId;
        return level;
    }

    public String getIdString(){
        return getIdString(getId());
    }

    public static String getIdString(Integer id) {
        return StringUtils.leftPad("" + id, 19, "0");
    }

    @Override
    public String toString() {
        return "Level{" +
                "id=" + getId() +
                ", callName='" + callName + '\'' +
                ", user_id=" + (userId != null ? userId : "null") +
                ", sourceIsMessage=" + sourceIsMessage +
                ", initialLevelBot=" + initialLevelBot +
                ", parentLevelId=" + parentLevelId +
                ", chatId=" + chatId +
                ", isBotLevel=" + botLevel +
                ", isTerminate=" + terminateBotLevel +
                '}';
    }

}
