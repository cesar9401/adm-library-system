package com.ayd2.adm.library.system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnAHelloWorld() {
        final var url = "http://localhost:" + port + "/hello";
        final var expected_msg = "Hello, world!";
        assertThat(this.testRestTemplate.getForObject(url, String.class)).isEqualTo(expected_msg);
    }

    @Test
    void shouldReturnAHelloThere() {
        final var url = "http://localhost:" + port + "/hello/there";
        final var expected_msg = "Hello, there!";
        assertThat(this.testRestTemplate.getForObject(url, String.class)).isEqualTo(expected_msg);
    }

    @Test
    void shouldReturnUnauthorized() {
        final var url = "http://localhost:" + port + "/hello/with-jwt";
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
