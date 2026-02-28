package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.Users.GetUserReviewsPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.TryGetByIdService;
import com.quack.quack_app.Application.UseCases.Services.TryGetService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class GetUserReviewsUseCase implements GetUserReviewsPort {

    private static final Logger log = LoggerFactory.getLogger(GetUserReviewsUseCase.class);

    private final UserRepository userRepository;
    private final ReviewRepository repository;

    public GetUserReviewsUseCase(UserRepository userRepository, ReviewRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }

    @Override
    public Reviews getReviewsByUser(UUID id, Natural pages, Natural size) {
        TryGetByIdService.execute(
                () -> userRepository.getUserById(id),
                () -> new UserNotFoundException(),
                log
        );

        var list = TryGetService.execute(
                () -> repository.getReviews(id),
                log
        );

        return list;
    }
}
