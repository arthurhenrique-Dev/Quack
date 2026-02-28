package com.quack.quack_app.Application.DTOs.Games;

import com.quack.quack_app.Domain.ValueObjects.Rating;

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
        Rating rating
) {
}
