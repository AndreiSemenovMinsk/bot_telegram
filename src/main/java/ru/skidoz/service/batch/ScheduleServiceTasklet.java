package ru.skidoz.service.batch;

import java.io.IOException;

import ru.skidoz.service.ScheduleService;
import com.google.zxing.WriterException;
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
public class ScheduleServiceTasklet implements Tasklet {

    @Autowired
    ScheduleService scheduleService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws IOException, WriterException {

        scheduleService.save();

        return RepeatStatus.FINISHED;
    }
}
