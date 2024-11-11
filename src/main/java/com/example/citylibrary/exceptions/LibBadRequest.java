package com.example.citylibrary.exceptions;

public class LibBadRequest extends RuntimeException {
    public LibBadRequest(String message) {
        super(message);
    }
}
