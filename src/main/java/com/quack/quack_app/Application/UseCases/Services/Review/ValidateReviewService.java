package com.quack.quack_app.Application.UseCases.Services.Review;

import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Reviews.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class ValidateReviewService {

    private static final Logger log = LoggerFactory.getLogger(ValidateReviewService.class);
    private final ReviewRepository repository;
    private final UserRepository userRepository;

    public ValidateReviewService(ReviewRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Review validateReview(UUID reviewId, UUID userId) {
        Review review = repository.getReview(reviewId)
                .orElseThrow(() -> {
                    log.warn("Review with id {} not found", reviewId);
                    return new InvalidDataException("Id " + reviewId + " incompatible");
                });

        if (!review.getUserId().equals(userId)) {
            log.warn("VALIDATION FAILED: User {} unauthorized to access review {}", userId, reviewId);
            throw new ValidationFailedException();
        }

        try {
            userRepository.getUserById(userId)
                    .orElseThrow(() -> new UserNotFoundException());
        } catch (UserNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error validating user {}", userId, e);
            throw new ProcessingErrorException();
        }

        return review;
    }
}

