package ru.skidoz.service.batch;

import com.google.zxing.WriterException;
import ru.skidoz.service.ScheduleService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class BatchConfig {

    @Autowired
    private ExcelMenuTasklet excelMenuTasklet;
    @Autowired
    private CategoryOptionsTasklet categoryOptionsTasklet;
    @Autowired
    private DatabaseDumpTasklet databaseDumpTasklet;
    @Autowired
    private ProductExcelsTasklet productExcelsTasklet;
    @Autowired
    private CacheSearchTasklet cacheSearchTasklet;
    @Autowired
    private InitialLevelCashbackTasklet initialLevelCashbackTasklet;
    @Autowired
    private BookmarkPriceNotifierTasklet bookmarkPriceNotifierTasklet;
    @Autowired
    private MonitorPriceTasklet monitorPriceTasklet;
    @Autowired
    private ScheduleService scheduleService;

    private static boolean systemInitialize = false;
    private static boolean systemRefresh = false;
    private static boolean reminder = false;

    @Scheduled(cron = "${systemInitializeCron}")
    public void systemInitializeJob() {

        systemInitialize = true;
    }

    @Scheduled(cron = "${systemRefreshCron}")
    public void systemRefreshJob() {

        systemRefresh = true;
    }

    @Scheduled(cron = "${remindTimer}")
    public void reminderJob() {
        reminder = true;
    }


    @Scheduled(cron = "${saveToRepoTimer}")
    public void heartbeatJob() throws IOException, WriterException {

        if (systemInitialize) {
            CompletableFuture.runAsync(() -> excelMenuTasklet.execute())
                    .thenRun(() -> categoryOptionsTasklet.execute())
                    .thenRun(() -> initialLevelCashbackTasklet.execute())
                    .join();
            systemInitialize = false;
        }
        if (systemRefresh) {
            CompletableFuture.runAsync(() -> databaseDumpTasklet.execute())
                    .thenRun(() -> productExcelsTasklet.execute())
                    .thenRun(() -> cacheSearchTasklet.execute())
                    .thenRun(() -> bookmarkPriceNotifierTasklet.execute())
                    .thenRun(() -> monitorPriceTasklet.execute())
                    .join();
            systemRefresh = false;
        }

        scheduleService.save();

        if (reminder) {
            scheduleService.reminder();
            reminder = false;
        }
    }
}