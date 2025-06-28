package ru.skidoz.model.pojo.telegram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author andrey.semenov
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class LevelResponse {

    private List<LevelChat> levelChats;
    private List<LevelChat> levelChatsAfterSave;
    private String key;
}
