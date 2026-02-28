package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Natural(Integer i) {
    private static final Logger log = LoggerFactory.getLogger(Natural.class);

    public Natural {
        if (i == null || i < 0) {
            InvalidDataException ex = new InvalidDataException("Invalid natural number: " + i);
            log.error("Validation failed for Natural: ", ex);
            throw ex;
        }
    }
}