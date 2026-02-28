package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOUpdateReview;
import com.quack.quack_app.Application.Ports.Input.Reviews.UpdateReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.UseCases.Services.Review.ValidateReviewService;
import com.quack.quack_app.Domain.Reviews.Review;


public class UpdateReviewUseCase implements UpdateReviewPort {

    private final ReviewRepository repository;
    private final ValidateReviewService validateReviewService;

    public UpdateReviewUseCase(ReviewRepository repository, ValidateReviewService validateReviewService) {
        this.repository = repository;
        this.validateReviewService = validateReviewService;
    }

    @Override
    public void updateReview(DTOUpdateReview dtoUpdateReview) {
        Review review = validateReviewService.validateReview(dtoUpdateReview.reviewId(), dtoUpdateReview.profileId());
        review.updateReview(dtoUpdateReview.reviewContent());
        repository.saveReview(review);
    }
}
