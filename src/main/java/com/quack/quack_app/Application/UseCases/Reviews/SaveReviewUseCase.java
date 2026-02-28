package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.Ports.Input.Reviews.SaveReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Reviews.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveReviewUseCase implements SaveReviewPort {

    private static final Logger logger = LoggerFactory.getLogger(SaveReviewUseCase.class);
    private final ReviewRepository repository;

    public SaveReviewUseCase(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveReviews(Review review) {
        if (repository.getReview(review.getReviewId()).isPresent()) {
            InvalidDataException exception = new InvalidDataException("Review with id " + review.getReviewId() + " already exists");
            logger.warn("Review with id {} already exists", review.getReviewId());
            throw exception;
        }
        try {
            repository.saveReview(review);
        } catch (Exception e) {
            logger.error("Could not save review [{}]: ", review.getReviewId(), e);
            throw new ProcessingErrorException("Internal error, could not save review");
        }
    }
}
