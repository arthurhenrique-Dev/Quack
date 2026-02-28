package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.UUID;

public record PasswordUpdater(String token, LocalDateTime expiration) {
    private static final Logger log = LoggerFactory.getLogger(PasswordUpdater.class);

    public static PasswordUpdater Start() {
        return new PasswordUpdater(UUID.randomUUID().toString(), LocalDateTime.now().plusHours(1));
    }

    public void Check(String inputToken) {
        if (!this.token.equals(inputToken)) {
            ValidationFailedException ex = new ValidationFailedException("Invalid token");
            log.error("Security: Invalid token", ex);
            throw ex;
        }
        if (LocalDateTime.now().isAfter(this.expiration)) {
            ValidationFailedException ex = new ValidationFailedException("Token expired");
            log.error("Security: Expired token", ex);
            throw ex;
        }
    }
}