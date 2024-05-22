package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
@Slf4j
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping
    public String sayHello() {
        return "Hello, world!";
    }

    @GetMapping("there")
    public String helloThere() {
        return helloService.sayHello();
    }

    @GetMapping("with-jwt")
    public String helloAuthenticated() {
        return "Hello, user!";
    }
}
