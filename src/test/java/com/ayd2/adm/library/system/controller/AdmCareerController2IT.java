package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmCareer;
import com.ayd2.adm.library.system.repository.AdmCareerRepository;
import com.ayd2.adm.library.system.service.AdmCareerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {AdmCareerController.class, AdmCareerService.class, AdmCareerRepository.class})
@AutoConfigureMockMvc
public class AdmCareerController2IT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

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
}
