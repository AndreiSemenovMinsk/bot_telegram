package ru.skidoz.service.batch;

import java.io.IOException;

import ru.skidoz.service.ScheduleService;
import com.google.zxing.WriterException;
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
    public void execute() {

        try {
            scheduleService.save();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
