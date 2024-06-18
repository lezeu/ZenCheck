package com.example.phoneapp.utils;

public class ZenCheckException extends RuntimeException {

    public ZenCheckException(String message) {
        super(message);
    }

    public ZenCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
