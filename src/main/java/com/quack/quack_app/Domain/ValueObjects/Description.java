package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Description(String description) {
    private static final Logger log = LoggerFactory.getLogger(Description.class);

    public Description {
        if (description == null || !description.matches("^.{0,250}$")) {
            InvalidDataException ex = new InvalidDataException("Description is too long");
            log.error("Validation failed for Description: ", ex);
            throw ex;
        }
    }
}