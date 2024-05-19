package com.ayd2.adm.library.system.demo;

import com.ayd2.adm.library.system.controller.HelloController;
import com.ayd2.adm.library.system.security.jwt.JwtAuthenticationFilter;
import com.ayd2.adm.library.system.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloService service;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @WithMockUser
    void helloShouldReturnMessageFromService() throws Exception {
        when(service.sayHello()).thenReturn("Hello, there!");

        this.mockMvc.perform(get("/hello/there"))
                .andDo(print())
                .andExpect(status().isOk());
                // .andExpect(content().string(is("Hello, there!")));
    }
}
