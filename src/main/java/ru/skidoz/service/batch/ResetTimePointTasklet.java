package ru.skidoz.service.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import static ru.skidoz.service.ScheduleService.timePoint;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class ResetTimePointTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        timePoint = 0;

        return RepeatStatus.FINISHED;
    }
}
