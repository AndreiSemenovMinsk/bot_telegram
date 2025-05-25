package ru.skidoz.service.batch;

import org.springframework.stereotype.Component;

import static ru.skidoz.service.ScheduleService.timePoint;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class ResetTimePointTasklet implements Tasklet {

    @Override
    public void execute() {

        timePoint++;
    }
}
