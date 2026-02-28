package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOUpdateRatingReview;
import com.quack.quack_app.Application.Ports.Input.Reviews.UpdateRatingReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.UseCases.Services.Review.ValidateReviewService;
import com.quack.quack_app.Domain.Reviews.Review;


public class UpdateRatingReviewUseCase implements UpdateRatingReviewPort {

    private final ReviewRepository repository;
    private final ValidateReviewService validateReviewService;

    public UpdateRatingReviewUseCase(ReviewRepository repository, ValidateReviewService validateReviewService) {
        this.repository = repository;
        this.validateReviewService = validateReviewService;
    }
    @Override
    public void updateRatingReview(DTOUpdateRatingReview dtoUpdateRatingReview) {
        Review review = validateReviewService.validateReview(dtoUpdateRatingReview.reviewId(), dtoUpdateRatingReview.profileId());
        review.updateRating(dtoUpdateRatingReview.rating());
        repository.saveReview(review);
    }
}
