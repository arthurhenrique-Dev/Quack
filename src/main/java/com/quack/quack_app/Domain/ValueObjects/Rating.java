package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

public record Rating(BigDecimal rate) {
    private static final Logger log = LoggerFactory.getLogger(Rating.class);

    public Rating {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.TEN) > 0 || rate.stripTrailingZeros().scale() > 1) {
            InvalidDataException ex = new InvalidDataException("Invalid rating value: " + rate);
            log.error("Validation failed for Rating: ", ex);
            throw ex;
        }
    }
}
