package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record TwoFA(boolean enabled, String secret) {

    private static final Logger log = LoggerFactory.getLogger(TwoFA.class);

    public TwoFA {
        if (enabled && (secret == null || secret.isBlank())) {
            var e = new ValidationFailedException("Secret cannot be empty if 2FA is enabled");
            log.warn(e.getMessage(), e);
            throw e;
        }
    }

    public static TwoFA disabled() {
        return new TwoFA(false, null);
    }
}
