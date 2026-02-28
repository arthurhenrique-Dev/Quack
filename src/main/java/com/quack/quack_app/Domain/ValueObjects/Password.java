package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Password(String password) {
    private static final Logger log = LoggerFactory.getLogger(Password.class);

    public Password {
        if (password == null || !password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")) {
            InvalidDataException ex = new InvalidDataException("Password requirements not met");
            log.error("Validation failed for Password: ", ex);
            throw ex;
        }
    }
}