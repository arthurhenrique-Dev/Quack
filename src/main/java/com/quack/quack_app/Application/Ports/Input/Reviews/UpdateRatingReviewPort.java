package com.quack.quack_app.Application.Ports.Input.Reviews;

import com.quack.quack_app.Domain.ValueObjects.Rating;

import java.util.UUID;

public interface UpdateRatingReviewPort {

    void updateRatingReview(UUID reviewId, UUID profileId, Rating rating);
}
