package com.quack.quack_app.Application.DTOs.Reviews;

import com.quack.quack_app.Domain.ValueObjects.Rating;

import java.util.UUID;

public record DTOUpdateRatingReview(UUID reviewId, UUID profileId, Rating rating) {
}
