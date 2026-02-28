package com.quack.quack_app.Domain.Exceptions;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }
}
