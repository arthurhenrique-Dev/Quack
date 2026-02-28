package com.quack.quack_app.Domain.Exceptions;

public class ValidationFailedException extends RuntimeException {
    public  ValidationFailedException() {super("Validation Failed");}

    public ValidationFailedException(String message) {
        super(message);
    }
}
