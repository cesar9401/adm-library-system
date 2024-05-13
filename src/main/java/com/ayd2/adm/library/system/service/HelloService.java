package com.ayd2.adm.library.system.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String sayHello() {
        return "Hello, there!";
    }
}
