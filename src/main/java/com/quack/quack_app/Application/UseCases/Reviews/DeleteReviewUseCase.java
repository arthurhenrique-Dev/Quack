package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.Ports.Input.Reviews.DeleteReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.UseCases.Services.Utilities.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DeleteReviewUseCase implements DeleteReviewPort {

    private static final Logger log = LoggerFactory.getLogger(DeleteReviewUseCase.class);
    private final ReviewRepository repository;

    public DeleteReviewUseCase(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public void DeleteReview(UUID id) {
        VerifyIfExistsModifyAndSaveService.execute(
                ()-> repository.getReview(id),
                review1 -> review1.removeReview(),
                ()-> new InvalidDataException("Review with this id not found"),
                repository::saveReview,
                log
        );
    }
}