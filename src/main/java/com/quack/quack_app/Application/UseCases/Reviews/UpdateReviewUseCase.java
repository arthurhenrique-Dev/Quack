package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOUpdateReview;
import com.quack.quack_app.Application.Ports.Input.Reviews.UpdateReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.UseCases.Services.Review.ValidateReviewService;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Rating;

import java.util.UUID;


public class UpdateReviewUseCase implements UpdateReviewPort {

    private final ReviewRepository repository;
    private final ValidateReviewService validateReviewService;

    public UpdateReviewUseCase(ReviewRepository repository, ValidateReviewService validateReviewService) {
        this.repository = repository;
        this.validateReviewService = validateReviewService;
    }

    @Override
    public void updateReview(UUID reviewId, UUID profileId, String reviewContent) {
        Review review = validateReviewService.validateReview(reviewId, profileId);
        review.updateReview(reviewContent);
        repository.saveReview(review);
    }
}
