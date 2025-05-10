package ru.skidoz.service.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;


@EnableBatchProcessing
@Configuration
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


//    @Autowired
//    private JobLauncher jobLauncher;

//    @Autowired
//    private Job job;
//
//    @Scheduled(cron = "${cronTimer}")
//    public void perform() throws Exception
//    {
//        JobParameters params = new JobParametersBuilder()
//                .addString("currencyJob", String.valueByCode(System.currentTimeMillis()))
//                .toJobParameters();
//        jobLauncher.run(job, params);
//    }
/*
    @Bean
    public Step step1() {
        System.out.println("step1");
        return steps
                .get("step1")
                .<CurrencyPOJO, CurrencyPOJO>chunk(10)
                .reader(new NBRBCurrencyRatesReader(new NBRBDataGettingService()))
                .writer(new CurrencyRatesWriter(currencyRepository, "nbrb"))
                .build();
    }

    @Bean
    public Step step2() {
        System.out.println("step2");
        return steps
                .get("step2")
                .<CurrencyPOJO, CurrencyPOJO>chunk(10)
                .reader(new MTBCurrencyRatesReader(mtbDataGettingService, xmlGettingService))
                .writer(new CurrencyRatesWriter(currencyRepository, "mtb"))
                .build();
    }
*/

    /*@Bean
    public ExcelMenuTasklet excelMenuTasklet1() {
        return new ExcelMenuTasklet();
    }*/


    @Bean
    public Step excelMenuStep(ExcelMenuTasklet excelMenuTasklet) {
        return stepBuilderFactory.get("excelMenuStep")
                .tasklet(excelMenuTasklet).build();
    }

    @Bean
    public Step categoryOptionsStep(CategoryOptionsTasklet categoryOptionsTasklet) {
        return stepBuilderFactory.get("categoryOptionsStep")
                .tasklet(categoryOptionsTasklet).build();
    }

    @Bean
    public Step resetTimePointStep(ResetTimePointTasklet resetTimePointTasklet) {
        return stepBuilderFactory.get("resetTimePointStep")
                .tasklet(resetTimePointTasklet).build();
    }

    @Bean
    public Step scheduleServiceStep(ScheduleServiceTasklet scheduleServiceTasklet) {
        return stepBuilderFactory.get("scheduleServiceStep")
                .tasklet(scheduleServiceTasklet).build();
    }

    @Bean
    public Step databaseDumpStep(DatabaseDumpTasklet databaseDumpTasklet) {
        return stepBuilderFactory.get("databaseDumpStep")
                .tasklet(databaseDumpTasklet).build();
    }

    @Bean
    public Step productExcelsStep(ProductExcelsTasklet productExcelsTasklet) {
        return stepBuilderFactory.get("productExcelsStep")
                .tasklet(productExcelsTasklet).build();
    }

    @Bean
    public Step cacheSearchStep(CacheSearchTasklet cacheSearchTasklet) {
        return stepBuilderFactory.get("cacheSearchStep")
                .tasklet(cacheSearchTasklet).build();
    }

    @Bean
    public Step initialLevelCashbackStep(InitialLevelCashbackTasklet initialLevelCashbackTasklet) {
        return stepBuilderFactory.get("initialLevelCashbackStep")
                .tasklet(initialLevelCashbackTasklet).build();
    }

    @Bean
    public Step priceNotifierStep(PriceNotifierTasklet priceNotifierTasklet) {
        return stepBuilderFactory.get("priceNotifierStep")
                .tasklet(priceNotifierTasklet).build();
    }

    @Bean
    public Step monitorPriceStep(MonitorPriceTasklet monitorPriceTasklet) {
        return stepBuilderFactory.get("monitorPriceStep")
                .tasklet(monitorPriceTasklet).build();
    }

    @Bean
    Job currencyJob(@Qualifier("excelMenuStep") Step excelMenuStep,
                    @Qualifier("categoryOptionsStep") Step categoryOptionsStep,
                    @Qualifier("resetTimePointStep") Step resetTimePointStep,
                    @Qualifier("scheduleServiceStep") Step scheduleServiceStep,
                    @Qualifier("productExcelsStep") Step productExcelsStep,
                    @Qualifier("cacheSearchStep") Step cacheSearchStep,
                    @Qualifier("initialLevelCashbackStep") Step initialLevelCashbackStep,
                    @Qualifier("databaseDumpStep") Step databaseDumpStep,
                    @Qualifier("priceNotifierStep") Step priceNotifierStep,
                    @Qualifier("monitorPriceStep") Step monitorPriceStep) {
        return jobBuilderFactory.get("currencyJob")
                .incrementer(new RunIdIncrementer())
                .start(excelMenuStep)
                .next(categoryOptionsStep)
                .next(resetTimePointStep)
                .next(scheduleServiceStep)
                .next(databaseDumpStep)
                .next(productExcelsStep)
                .next(cacheSearchStep)
                .next(initialLevelCashbackStep)
                .next(priceNotifierStep)
                .next(monitorPriceStep)
                .build();
    }
}