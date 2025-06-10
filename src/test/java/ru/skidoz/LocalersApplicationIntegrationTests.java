package ru.skidoz;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LocalersApplicationIntegrationTests {

    @Test
    void contextLoads() {
    }

}
