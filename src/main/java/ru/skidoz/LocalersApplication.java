package ru.skidoz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication//(exclude = { BatchAutoConfiguration.class })
public class LocalersApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalersApplication.class, args);
    }

}
