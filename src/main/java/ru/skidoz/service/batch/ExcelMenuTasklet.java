package ru.skidoz.service.batch;

import ru.skidoz.service.InitialExcelMenu;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class ExcelMenuTasklet implements Tasklet {

    @Autowired
    InitialExcelMenu initialExcelMenu;


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        initialExcelMenu.execute();

        return RepeatStatus.FINISHED;
    }
}
