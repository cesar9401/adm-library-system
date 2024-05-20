package com.ayd2.adm.library.system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void helloShouldReturnMessageFromService() throws Exception {
        this.mockMvc.perform(
                        get("/hello/there")
                                .accept(MediaType.TEXT_PLAIN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, there!"));
    }

    @Test
    @WithMockUser
    void helloShouldReturnHelloWorldFromController() throws Exception {
        this.mockMvc.perform(
                        get("/hello")
                                .accept(MediaType.TEXT_PLAIN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, world!"));
    }
}
