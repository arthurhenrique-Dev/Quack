package com.quack.quack_app.Application.Ports.Output.Repositories;

import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Reviews;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

    void saveReview(Review review);
    Reviews getReviews(UUID id);
    Optional<Review> getReview(UUID review);
}
