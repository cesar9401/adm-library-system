package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmCareer;
import com.ayd2.adm.library.system.model.AdmStudent;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
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
class AdmStudentControllerTest {

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
    public void createWithRepeatedCarnet() throws Exception {
        // create career first
        var career = new AdmCareer();
        career.setCode("ICX1");
        career.setName("Ingenieria en Ciencias y Sistemas");

        var result = mockMvc.perform(
                        post("/careers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(career))
                ).andExpect(status().isCreated())
                .andReturn();

        var newCareer = mapper.readValue(result.getResponse().getContentAsString(), AdmCareer.class);

        final var carnet = "201430927";

        final var user1 = new AdmUser();
        user1.setEmail("cesartzoc201430927@cunoc.edu.gt");
        user1.setPassword("8eE6NSbhckfzXo");
        user1.setFullName("César Tzoc Alvarado");

        final var student1 = new AdmStudent();
        student1.setCarnet(carnet);
        student1.setBirthday(LocalDate.of(2000, 1, 1));
        student1.setUser(user1);
        student1.setCareer(newCareer);

        // student 2
        final var user2 = new AdmUser();
        user2.setEmail("ctzoc@cunoc.edu.gt");
        user2.setPassword("8eE6NSbhckfzXo");
        user2.setFullName("César Tzoc Alvarado 2");

        final var student2 = new AdmStudent();
        student2.setCarnet(carnet);
        student2.setBirthday(LocalDate.of(2000, 1, 1));
        student2.setUser(user2);
        student2.setCareer(newCareer);

        System.out.println(mapper.writeValueAsString(student1));
        mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(student1))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").isNumber());

        mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(student2))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("carnet_already_exists"));
    }

    @Test
    @WithMockUser
    public void updateUserTest() throws Exception {
        // create career first
        var career = new AdmCareer();
        career.setCode("ICX2");
        career.setName("Ingenieria en Ciencias y Sistemas");

        var result = mockMvc.perform(
                        post("/careers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(career))
                ).andExpect(status().isCreated())
                .andReturn();

        var newCareer = mapper.readValue(result.getResponse().getContentAsString(), AdmCareer.class);

        var carnet = "201430927";
        var newCarnet = "202030927";

        var email = "ctzoc1@cunoc.edu.gt";
        var newEmail = "ctzoc.alvarado@cunoc.edu.gt";

        final var user1 = new AdmUser();
        user1.setEmail(email);
        user1.setPassword("8eE6NSbhckfzXo");
        user1.setFullName("César Tzoc Alvarado");

        final var student1 = new AdmStudent();
        student1.setCarnet(carnet);
        student1.setBirthday(LocalDate.of(2000, 1, 1));
        student1.setUser(user1);
        student1.setCareer(newCareer);

        var postResult = mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(student1))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").isNumber())
                .andReturn();

        var json = postResult.getResponse().getContentAsString();
        var createdStudent = mapper.readValue(json, AdmStudent.class);
        assertAll(
                () -> assertNotNull(createdStudent),
                () -> assertNotNull(createdStudent.getStudentId()),
                () -> assertNotNull(createdStudent.getUser()),
                () -> assertNotNull(createdStudent.getUser().getUserId())
        );

        var studentId = createdStudent.getStudentId();
        // get by id
        var resultById = mockMvc.perform(
                        get("/students/{studentId}", studentId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var studentById = mapper.readValue(resultById.getResponse().getContentAsString(), AdmStudent.class);
        assertAll(
                () -> assertNotNull(studentById),
                () -> assertNotNull(studentById.getStudentId()),
                () -> assertNotNull(studentById.getUser()),
                () -> assertNotNull(studentById.getUser().getUserId()),
                () -> assertEquals(studentId, studentById.getStudentId())
        );

        // update carnet and email
        studentById.setCarnet(newCarnet);
        studentById.getUser().setEmail(newEmail);

        // test update
        mockMvc.perform(
                        put("/students/{studentId}", studentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(studentById))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carnet").value(newCarnet))
                .andExpect(jsonPath("$.user.email").value(newEmail))
                .andExpect(jsonPath("$.studentId").value(studentId));
    }

    @Test
    @WithMockUser
    public void shouldReturnNotFoundWhenUpdateAUserThatDoesntExist() throws Exception {
        final var userId = -1L;
        final var studentId = -1L;
        final var user = new AdmUser();
        user.setUserId(userId);
        user.setEmail("demo77@cunoc.edu.gt");
        user.setFullName("Vicente Fernandez");

        // create career first
        var career = new AdmCareer();
        career.setCode("IC");
        career.setName("Ingenieria en Ciencias y Sistemas");

        var result = mockMvc.perform(
                        post("/careers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(career))
                ).andExpect(status().isCreated())
                .andReturn();

        var newCareer = mapper.readValue(result.getResponse().getContentAsString(), AdmCareer.class);

        final var student = new AdmStudent();
        student.setStudentId(studentId);
        student.setCareer(newCareer);
        student.setCarnet("201430927");
        student.setUser(user);
        student.setBirthday(LocalDate.of(2000, 1, 1));

        mockMvc.perform(
                        put("/students/{studentId}", studentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(student))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("student_not_found"));
    }

    @Test
    @WithMockUser
    public void findAllTest() throws Exception {
        final var page = 1;
        final var size = 20;

        mockMvc.perform(
                        get("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"));
    }
}
