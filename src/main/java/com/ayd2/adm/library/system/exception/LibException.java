package com.ayd2.adm.library.system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LibException extends Exception {

    private HttpStatus status;

    public LibException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public LibException status(HttpStatus status) {
        this.status = status;
        return this;
    }
}
