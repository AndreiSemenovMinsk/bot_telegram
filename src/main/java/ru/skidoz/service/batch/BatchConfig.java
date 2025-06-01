package ru.skidoz.service.batch;

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

    @Scheduled(cron = "${systemInitializeCron}")
    public void systemInitializeJob() {

        CompletableFuture.runAsync(() -> excelMenuTasklet.execute())
                .thenRun(() -> categoryOptionsTasklet.execute())
                .thenRun(() -> initialLevelCashbackTasklet.execute())
                .join();
    }

    @Scheduled(cron = "${systemRefreshCron}")
    public void systemRefreshJob() {

        CompletableFuture.runAsync(() -> databaseDumpTasklet.execute())
                .thenRun(() -> productExcelsTasklet.execute())
                .thenRun(() -> cacheSearchTasklet.execute())
                .thenRun(() -> bookmarkPriceNotifierTasklet.execute())
                .thenRun(() -> monitorPriceTasklet.execute())
                .join();
    }
}