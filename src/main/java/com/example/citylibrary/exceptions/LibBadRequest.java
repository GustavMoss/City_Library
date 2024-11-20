package com.example.citylibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LibBadRequest extends RuntimeException {
    public LibBadRequest(String message) {
        super(message);
    }
}
