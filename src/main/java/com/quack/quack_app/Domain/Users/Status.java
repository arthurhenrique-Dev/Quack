package com.quack.quack_app.Domain.Users;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Status {

    ACTIVE,
    INACTIVE,
    PENDING;

    private static final Logger log = LoggerFactory.getLogger(Status.class);

    public Status ban() {
        try {
            if (this == ACTIVE) {
                return INACTIVE;
            }
            throw new IllegalArgumentException("User status must be ACTIVE to be banned. Current: " + this);
        } catch (Exception e) {
            log.error("Domain Error: Invalid ban operation from status [{}]", this, e);
            throw new InvalidDataException("This user cannot be banned because they are not currently active.");
        }
    }

    public Status activate() {
        try {
            if (this != ACTIVE) {
                return ACTIVE;
            }
            throw new IllegalArgumentException("User is already ACTIVE.");
        } catch (Exception e) {
            log.error("Domain Error: Invalid activation operation from status [{}]", this, e);
            throw new InvalidDataException("This user cannot be activated because they are already active.");
        }
    }
}