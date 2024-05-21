package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmRole;
import com.ayd2.adm.library.system.util.enums.RoleEnum;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdmRoleControllerTest {

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
    public void findAllRolesTest() throws Exception {
        final var page = 1;
        final var size = 20;

        mockMvc.perform(
                        get("/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                ).andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"));
    }

    @Test
    @WithMockUser
    public void findRoleByIdTest() throws Exception {
        var result = mockMvc.perform(
                        get("/roles/{roleId}", RoleEnum.LIBRARIAN.roleId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var json = result.getResponse().getContentAsString();
        var role = mapper.readValue(json, AdmRole.class);

        assertAll(
                () -> assertNotNull(role),
                () -> assertNotNull(role.getRoleId()),
                () -> assertEquals(RoleEnum.LIBRARIAN.roleId, role.getRoleId())
        );
    }

    @Test
    @WithMockUser
    public void shouldReturnNotFoundWhenRoleDoesNotExist() throws Exception {
        final var roleId = -1L;
        mockMvc.perform(
                        get("/roles/{roleId}", roleId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
