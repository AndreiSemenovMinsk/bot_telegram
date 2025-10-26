package ru.skidoz;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
/**
 * @author andrey.semenov
 */
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("mail_service_db")
                    .withUsername("mail_service")
                    .withPassword("mail_service")
                    .withReuse(true);

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

//    @BeforeAll
//    static void runLiquibase() throws Exception {
//        // Создаём обычное JDBC-подключение
//        try (Connection connection = DriverManager.getConnection(
//                postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword());
//             // Оборачиваем JDBC-подключение для Liquibase
//        ) {
//
//            Database database = DatabaseFactory.getInstance()
//                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
//
//            // Создаём Liquibase и выполняем обновление
//            try (Liquibase liquibase = new Liquibase(
//                    "changelog/db.changelog-master.yaml",
//                    new ClassLoaderResourceAccessor(),
//                    database)) {
//
//                liquibase.update(new Contexts(), new LabelExpression());
//            }
//        }
//
//        System.out.println(postgres.getJdbcUrl());
//    }
}
