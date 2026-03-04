package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.Ports.Input.Reviews.UpdateRatingReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.UseCases.Services.Review.ValidateReviewService;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Rating;

import java.util.UUID;


public class UpdateRatingReviewUseCase implements UpdateRatingReviewPort {

    private final ReviewRepository repository;
    private final ValidateReviewService validateReviewService;

    public UpdateRatingReviewUseCase(ReviewRepository repository, ValidateReviewService validateReviewService) {
        this.repository = repository;
        this.validateReviewService = validateReviewService;
    }
    @Override
    public void updateRatingReview(UUID reviewId, UUID profileId, Rating rating) {
        Review review = validateReviewService.validateReview(reviewId, profileId);
        review.updateRating(rating);
        repository.saveReview(review);
    }
}
