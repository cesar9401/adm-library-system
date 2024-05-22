package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.dto.AuthReqDto;
import com.ayd2.adm.library.system.dto.JwtResDto;
import com.ayd2.adm.library.system.model.AdmUser;
import com.ayd2.adm.library.system.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    JwtService jwtService;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @WithMockUser
    public void createUserAndGetTokenTest() throws Exception {
        final var email = "ctzoc123@cunoc.edu.gt";
        final var password = "8eE6NSbhckfzXo";
        final var url = "http://localhost:" + port + "/auth";

        final var user = new AdmUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName("Demo name");

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.email").value(email));

        var reqAuth = new AuthReqDto(email, password);
        HttpEntity<AuthReqDto> request = new HttpEntity<>(reqAuth);
        ResponseEntity<JwtResDto> response = testRestTemplate.postForEntity(url, request, JwtResDto.class);
        var jwt = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(jwt),
                () -> assertNotNull(jwt.token())
        );

        var isValid = jwtService.isValid(jwt.token());
        var userName = jwtService.getUsername(jwt.token());

        assertAll(
                () -> assertTrue(isValid),
                () -> assertEquals(email, userName)
        );
    }

    @Test
    @WithMockUser
    public void createUserAndGetHelloGreetingTest() throws Exception {
        final var email = "ctzoc321@cunoc.edu.gt";
        final var password = "8eE6NSbhckfzXo";
        final var url = "http://localhost:" + port;
        final var expectedMessage = "Hello, user!";

        final var user = new AdmUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName("Demo name");

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.email").value(email));

        var reqAuth = new AuthReqDto(email, password);
        HttpEntity<AuthReqDto> request = new HttpEntity<>(reqAuth);
        ResponseEntity<JwtResDto> response = testRestTemplate.postForEntity(url + "/auth", request, JwtResDto.class);
        var jwt = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(jwt),
                () -> assertNotNull(jwt.token())
        );

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer ".concat(jwt.token()));

        ResponseEntity<String> responseWithJwt = this.testRestTemplate.exchange(
                url + "/hello/with-jwt",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class, headers
        );

        assertNotNull(responseWithJwt);
        assertEquals(HttpStatus.OK, responseWithJwt.getStatusCode());
        assertNotNull(responseWithJwt.getBody());
        assertEquals(expectedMessage, responseWithJwt.getBody());
    }
}
