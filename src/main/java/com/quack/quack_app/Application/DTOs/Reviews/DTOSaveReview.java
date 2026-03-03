package com.quack.quack_app.Application.DTOs.Reviews;

import com.quack.quack_app.Domain.ValueObjects.Rating;

import java.util.UUID;

public record DTOSaveReview(

        UUID userId,
        UUID gameId,
        Rating rating,
        String review
) {
}
