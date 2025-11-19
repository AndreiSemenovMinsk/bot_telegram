package ru.skidoz.model.pojo.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.telegram.telegrambots.meta.api.objects.Document;

/**
 * @author andrey.semenov
 */
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
public class FileResponse {

    private LevelChat levelChat;
    private Document document;
    private int userId;
    private String key;
}
