package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.Ports.Input.Reviews.DeleteReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.Utilities.TryGetByIdService;
import com.quack.quack_app.Application.UseCases.Services.Utilities.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DeleteReviewUseCase implements DeleteReviewPort {

    private static final Logger log = LoggerFactory.getLogger(DeleteReviewUseCase.class);
    private final ReviewRepository repository;
    private final UserRepository userRepository;

    public DeleteReviewUseCase(ReviewRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public void DeleteReview(UUID idUser, UUID idReview) {

        TryGetByIdService.execute(
                ()-> userRepository.getUserById(idUser),
                ()-> new UserNotFoundException(),
                log
        );

        VerifyIfExistsModifyAndSaveService.execute(
                ()-> repository.getReview(idReview),
                review1 -> review1.removeReview(),
                ()-> new InvalidDataException("Review with this id not found"),
                repository::saveReview,
                log
        );
    }
}