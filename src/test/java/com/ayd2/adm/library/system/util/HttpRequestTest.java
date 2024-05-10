package com.ayd2.adm.library.system.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

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
}
