package com.ayd2.adm.library.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LibExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LibException.class)
    public ResponseEntity<String> handleHtlException(LibException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>("bad_credentials", HttpStatus.UNAUTHORIZED);
    }
}
