package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmCareer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdmCareerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void createCareer() {
        final var url = "http://localhost:" + port + "/books";
        var career = new AdmCareer();
        career.setCode("IC");
        career.setName("Ingenieria en Ciencias y Sistemas");

        HttpEntity<AdmCareer> request = new HttpEntity<>(career);
        ResponseEntity<AdmCareer> response = testRestTemplate.postForEntity(url, request, AdmCareer.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
