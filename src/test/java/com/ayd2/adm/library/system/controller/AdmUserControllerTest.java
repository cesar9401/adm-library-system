package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmUser;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdmUserControllerTest {

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
    public void createWithRepeatedEmail() throws Exception {
        final var email = "demo@cunoc.edu.gt";

        final var user1 = new AdmUser();
        user1.setEmail(email);
        user1.setPassword("8eE6NSbhckfzXo");
        user1.setFullName("Vicente Fernandez");

        final var user2 = new AdmUser();
        user2.setEmail(email);
        user2.setPassword("Wp8wYvNdK9WEx9");
        user2.setFullName("Luis Miguel");

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user1))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber());

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user2))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("email_already_exists"));
    }

    @Test
    @WithMockUser
    public void shouldReturnNotFoundWhenUpdateAUserThatDoesntExist() throws Exception {
        final var userId = -1;
        final var user = new AdmUser();
        user.setEmail("demo77@cunoc.edu.gt");
        user.setFullName("Vicente Fernandez");

        mockMvc.perform(
                        put("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("user_not_found"));
    }

    @Test
    @WithMockUser
    public void updateUserTest() throws Exception {
        final var email = "demo.email@cunoc.edu.gt";
        final var name = "Nombre demo";

        final var newEmail = "luismi@cunoc.edu.gt";
        final var newName = "Luis Miguel Gallego";

        final var user = new AdmUser();
        user.setEmail(email);
        user.setFullName(name);
        user.setPassword("7BLxqNgnQvjb2b");

        var result = mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber())
                .andReturn();

        var json = result.getResponse().getContentAsString();
        var createdUser = mapper.readValue(json, AdmUser.class);

        assertAll(
                () -> assertNotNull(createdUser),
                () -> assertNotNull(createdUser.getUserId()),
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(name, user.getFullName())
        );

        var userId = createdUser.getUserId();
        // find by id
        var resultById = mockMvc.perform(
                        get("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var userById = mapper.readValue(resultById.getResponse().getContentAsString(), AdmUser.class);
        assertAll(
                () -> assertNotNull(userById),
                () -> assertEquals(email, userById.getEmail()),
                () -> assertEquals(userId, userById.getUserId())
        );

        // update email and name
        userById.setEmail(newEmail);
        userById.setFullName(newName);

        // test update
        mockMvc.perform(
                        put("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userById))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(newName))
                .andExpect(jsonPath("$.email").value(newEmail))
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    @WithMockUser
    public void findAllTest() throws Exception {
        final var page = 1;
        final var size = 20;

        mockMvc.perform(
                        get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"));
    }
}
