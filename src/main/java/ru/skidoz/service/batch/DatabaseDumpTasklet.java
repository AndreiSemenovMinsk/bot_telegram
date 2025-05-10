package ru.skidoz.service.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


/**
 * Created by Users on 30.05.2020.
 */
@Component
public class DatabaseDumpTasklet implements Tasklet {

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws IOException, ExecutionException, InterruptedException, TimeoutException {


        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Process process;
        if (isWindows) {
            process = Runtime.getRuntime()
                    .exec(String.format("cmd.exe mysqldump -u'%s' -p'%s' db_name > db_backup.sql", "~/Documents", "pass"));
        } else {
            process = Runtime.getRuntime()
                    .exec(String.format("/bin/sh mysqldump -u'%s' -p'%s' db_name > db_backup.sql", "~/Documents", "pass"));
        }
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = executorService.submit(streamGobbler);

        int exitCode = process.waitFor();

        future.get(10, TimeUnit.SECONDS);

        System.out.println(exitCode);

        System.out.println(Runtime.getRuntime().availableProcessors());


        return RepeatStatus.FINISHED;
    }
}
