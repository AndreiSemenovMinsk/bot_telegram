package ru.skidoz.service.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.service.InitialLevel;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class InitialLevelCashbackTasklet implements Tasklet {

    @Autowired
    InitialLevel initialLevel;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        if (InitialLevel.SHOP == null) {

            initialLevel.initLevels();
        }

        return RepeatStatus.FINISHED;
    }
}
