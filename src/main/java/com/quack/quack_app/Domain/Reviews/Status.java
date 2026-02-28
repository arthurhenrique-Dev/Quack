package com.quack.quack_app.Domain.Reviews;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Status {

    ON,
    OFF;

    private static final Logger log = LoggerFactory.getLogger(Status.class);

    public Status removeReview() {
        try {
            if (this == ON) {
                return OFF;
            }
            throw new IllegalArgumentException("Review is already in OFF status");
        } catch (Exception e) {
            log.error("Domain Error: Attempted to remove a review that is already OFF", e);
            throw new InvalidDataException("Review already removed");
        }
    }
}