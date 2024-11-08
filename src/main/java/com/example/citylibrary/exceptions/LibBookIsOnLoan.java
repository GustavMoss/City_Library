package com.example.citylibrary.exceptions;

public class LibBookIsOnLoan extends RuntimeException {
    public LibBookIsOnLoan(String message) {
        super(message);
    }
}
