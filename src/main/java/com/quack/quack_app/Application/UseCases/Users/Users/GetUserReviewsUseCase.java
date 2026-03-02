package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Input.Users.GetUserReviewsPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.TryGetByIdService;
import com.quack.quack_app.Application.UseCases.Services.TryGetService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class GetUserReviewsUseCase implements GetUserReviewsPort {

    private static final Logger log = LoggerFactory.getLogger(GetUserReviewsUseCase.class);

    private final UserRepository userRepository;
    private final ReviewRepository repository;
    private final ReviewMapper reviewMapper;

    public GetUserReviewsUseCase(UserRepository userRepository, ReviewRepository repository, ReviewMapper reviewMapper) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<DTOReturnReview> getReviewsByUser(UUID id, Natural pages, Natural size) {
        var user = TryGetByIdService.execute(
                () -> userRepository.getUserById(id),
                () -> new UserNotFoundException(),
                log
        );

        var list = TryGetService.execute(
                () -> repository.getReviews(id),
                log
        );

        return list.reviews().stream()
                .map(r-> reviewMapper.dtoReturnReview(r, user.getUsername()))
                .toList();
    }
}
