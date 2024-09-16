package com.example.demo.exceptions;

public class HandleGeneralException extends RuntimeException {
    public HandleGeneralException(String message) {
        super(message);
    }

    public HandleGeneralException(String message, Throwable cause) {
        super(message, cause);
    }
}

