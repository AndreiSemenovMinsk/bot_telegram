package ru.skidoz.service.batch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import ru.skidoz.service.TelegramBotWebhook;
import ru.skidoz.util.Exceler;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class ProductExcelsTasklet implements Tasklet {

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    private Exceler exceler;
    @Autowired
    private TelegramBotWebhook telegramBot;

    @Override
    public void execute() {

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ProductExcelsTasklet+++++++++++++++++++++++++++++++++++");

        Map<User, byte[]> excels = telegramBot.excels;
        for (Map.Entry<User, byte[]> excel : excels.entrySet()) {
            System.out.println("excel byte[]***" + excel.getValue().length);
            try {

                exceler.processExcel(new ByteArrayInputStream(excel.getValue()), excel.getKey());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ProductExcelsTasklet finish+++++++++++++++++++++++++++++++++++");
    }
}
