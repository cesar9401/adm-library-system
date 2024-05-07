package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.dto.AuthReqDto;
import com.ayd2.adm.library.system.dto.JwtResDto;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<JwtResDto> createToken(@RequestBody AuthReqDto reqDto) throws LibException {
        var token = authenticationService.createToken(reqDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
