package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmCareer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdmCareerControllerTest {

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
    public void createCareer() throws Exception {
        var career = new AdmCareer();
        career.setCode("IC");
        career.setName("Ingenieria en Ciencias y Sistemas");

        mockMvc.perform(
                post("/careers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(career))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void createWithRepeatedCode() throws Exception {
        var career = new AdmCareer();
        career.setCode("ICS");
        career.setName("Ingenieria en Ciencias y Sistemas");

        var repeatedCodeCareer = new AdmCareer();
        repeatedCodeCareer.setCode("ICS");
        repeatedCodeCareer.setName("Ingenieria en Civil");

        mockMvc.perform(
                post("/careers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(career))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                        post("/careers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(repeatedCodeCareer))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string("code_already_exists"));
    }

    @Test
    @WithMockUser
    public void updateCareerTest() throws Exception {
        final var code = "ABC";
        final var name = "Career ABC";
        final var newName = "Career ABC Updated";

        var career = new AdmCareer();
        career.setCode(code);
        career.setName(name);

        var result = mockMvc.perform(
                        post("/careers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(career))
                ).andExpect(status().isCreated())
                .andReturn();

        var content = result.getResponse().getContentAsString();
        var createdCareer = mapper.readValue(content, AdmCareer.class);

        assertAll(
                () -> assertNotNull(createdCareer),
                () -> assertNotNull(createdCareer.getCareerId())
        );

        var careerId = createdCareer.getCareerId();
        var resultById = mockMvc.perform(
                        get("/careers/{careerId}", careerId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        var careerById = mapper.readValue(resultById.getResponse().getContentAsString(), AdmCareer.class);

        assertAll(
                () -> assertNotNull(careerById),
                () -> assertEquals(code, careerById.getCode()),
                () -> assertEquals(name, careerById.getName())
        );

        careerById.setName(newName);
        mockMvc.perform(
                        put("/careers/{careerId}", careerById.getCareerId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(careerById))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.code").value(code));
    }

    @Test
    @WithMockUser
    public void shouldReturnNotFoundWhenUpdateNonExistingCareer() throws Exception {
        final var careerId = -1L;
        final var career = new AdmCareer();
        career.setCode("XYZ");
        career.setName("Carrera XYZ");
        career.setCareerId(careerId);

        mockMvc.perform(
                        put("/careers/{careerId}", careerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(career))
                ).andExpect(status().isNotFound())
                .andExpect(content().string("career_not_found"));
    }

    @Test
    @WithMockUser
    public void findAllTest() throws Exception {
        var page = 1;
        var size = 20;

        mockMvc.perform(
                        get("/careers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                ).andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"));
    }
}
