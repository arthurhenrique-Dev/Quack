package com.quack.quack_app.Domain.Exceptions;

public class ProcessingErrorException extends RuntimeException {
    public ProcessingErrorException() {super("Internal error");}

    public ProcessingErrorException(String message) {
        super(message);
    }
}
