package ru.skidoz.service.batch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.ScheduleService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.service.TelegramBot;
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
    private TelegramBot telegramBot;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws IOException {

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ProductExcelsTasklet+++++++++++++++++++++++++++++++++++");

        Map<User, byte[]> excels = telegramBot.excels;
        for (Map.Entry<User, byte[]> excel : excels.entrySet()) {
            System.out.println("excel byte[]***" + excel.getValue().length);
            exceler.processExcel(new ByteArrayInputStream(excel.getValue()), excel.getKey());
        }

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ProductExcelsTasklet finish+++++++++++++++++++++++++++++++++++");

        return RepeatStatus.FINISHED;
    }
}
