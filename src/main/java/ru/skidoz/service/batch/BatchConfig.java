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
    private ResetTimePointTasklet resetTimePointTasklet;
    @Autowired
    private ScheduleServiceTasklet scheduleServiceTasklet;
    @Autowired
    private DatabaseDumpTasklet databaseDumpTasklet;
    @Autowired
    private ProductExcelsTasklet productExcelsTasklet;
    @Autowired
    private CacheSearchTasklet cacheSearchTasklet;
    @Autowired
    private InitialLevelCashbackTasklet initialLevelCashbackTasklet;
    @Autowired
    private PriceNotifierTasklet priceNotifierTasklet;
    @Autowired
    private MonitorPriceTasklet monitorPriceTasklet;

    @Scheduled(cron = "${cronBatch}")
    public void currencyJob() {

        System.out.println("currencyJob____");

        CompletableFuture.runAsync(() -> excelMenuTasklet.execute())
                .thenRun(() -> categoryOptionsTasklet.execute())
                .thenRun(() -> resetTimePointTasklet.execute())
                .thenRun(() -> scheduleServiceTasklet.execute())
                .thenRun(() -> databaseDumpTasklet.execute())
                .thenRun(() -> productExcelsTasklet.execute())
                .thenRun(() -> cacheSearchTasklet.execute())
                .thenRun(() -> initialLevelCashbackTasklet.execute())
                .thenRun(() -> priceNotifierTasklet.execute())
                .thenRun(() -> monitorPriceTasklet.execute())
                .join();
    }
}