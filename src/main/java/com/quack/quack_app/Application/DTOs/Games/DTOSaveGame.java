package com.quack.quack_app.Application.DTOs.Games;

import java.time.LocalDate;

public record DTOSaveGame(

        String name,
        String description,
        LocalDate releaseDate,
        String genre,
        String developer,
        String publisher,
        String photoUrl,
        String platforms
) {
}
