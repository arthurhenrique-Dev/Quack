package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Username(String username) {
    private static final Logger log = LoggerFactory.getLogger(Username.class);

    public Username {
        if (username == null || !username.matches("^.{0,130}$")) {
            InvalidDataException ex = new InvalidDataException("Username is too long");
            log.error("Validation failed for Username: ", ex);
            throw ex;
        }
    }
}