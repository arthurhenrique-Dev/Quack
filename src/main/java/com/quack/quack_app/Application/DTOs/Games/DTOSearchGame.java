package com.quack.quack_app.Application.DTOs.Games;

import com.quack.quack_app.Domain.ValueObjects.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DTOSearchGame(
        UUID id,
        String name,
        LocalDate releaseDate,
        String genre,
        String developer,
        String publisher,
        String platforms,
        BigDecimal rating
) {
    public static DTOSearchGame defaultSearch(){
        return new DTOSearchGame(
                null,
                null,
                LocalDate.now(),
                null,
                null,
                null,
                null,
                new BigDecimal(5)
        );
    }
}
