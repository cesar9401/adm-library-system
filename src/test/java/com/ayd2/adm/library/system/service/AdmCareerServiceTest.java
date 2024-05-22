package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmCareer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class AdmCareerServiceTest {

    @Autowired
    AdmCareerService service;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void shouldReturnInvalidTest() throws LibException {
        var careerId = 1000L;

        var career = new AdmCareer();
        career.setCode("A1");
        career.setName("Carrera demo");

        var createdCareer = service.create(career);
        assertAll(
                () -> assertNotNull(createdCareer),
                () -> assertNotNull(createdCareer.getCareerId())
        );

        var createdId = createdCareer.getCareerId();
        createdCareer.setCareerId(careerId);

        assertThrows(LibException.class, () -> service.update(createdId, createdCareer));
    }

    @Test
    void shouldReturnCodeAlreadyExists() throws LibException {
        final var code1 = "B1";
        final var code2 = "B2";

        var career = new AdmCareer();
        career.setCode(code1);
        career.setName("Carrera demo");

        var career2 = new AdmCareer();
        career2.setCode(code2);
        career2.setName("Carrera demo 2");

        var createdCareer = service.create(career);
        var createdCareer2 = service.create(career2);
        assertAll(
                () -> assertNotNull(createdCareer),
                () -> assertNotNull(createdCareer.getCareerId()),
                () -> assertNotNull(createdCareer2),
                () -> assertNotNull(createdCareer2.getCareerId())
        );

        createdCareer.setCode(code2);
        assertThrows(LibException.class, () -> service.update(createdCareer.getCareerId(), createdCareer));
    }
}
