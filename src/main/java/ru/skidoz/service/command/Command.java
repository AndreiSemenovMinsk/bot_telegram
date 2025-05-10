package ru.skidoz.service.command;


import java.util.List;

import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.User;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface Command {

	List<LevelChat> runCommand(Update update, Level level, User users) throws Exception;

}
