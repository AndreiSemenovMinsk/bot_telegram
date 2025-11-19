package ru.skidoz.service.batch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
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
    private Exceler exceler;

    public Map<Integer, byte[]> excels = new HashMap<>();

    @Override
    public void execute() {

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ProductExcelsTasklet+++++++++++++++++++++++++++++++++++");

        for (Map.Entry<Integer, byte[]> excel : excels.entrySet()) {

            System.out.println("excel byte[]***" + excel.getValue().length);
            try {

                final int userId = excel.getKey();

                exceler.processExcel(new ByteArrayInputStream(excel.getValue()), userId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ProductExcelsTasklet finish+++++++++++++++++++++++++++++++++++");
    }
}
