package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Input.Users.GetUserPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.TryGetByIdService;
import com.quack.quack_app.Application.UseCases.Services.TryGetService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class GetUserUseCase implements GetUserPort {

    private static final Logger log = LoggerFactory.getLogger(GetUserUseCase.class);

    private final UserRepository repository;
    private final ReviewRepository reviewRepository;
    private final UserMapper mapper;
    private final ReviewMapper reviewMapper;

    public GetUserUseCase(UserRepository repository, ReviewRepository reviewRepository, UserMapper mapper, ReviewMapper reviewMapper) {
        this.repository = repository;
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public DTOGetUser getUser(UUID idUser) {
        var user = TryGetByIdService.execute(
                ()-> repository.getUserById(idUser),
                ()-> new UserNotFoundException(),
                log
        );
        var reviews = TryGetService.execute(
                ()-> reviewRepository.getReviews(idUser),
                log
        );
        var reviewsMapped = reviews.getActiveReviews()
                .reviews()
                .stream()
                .map(r-> reviewMapper.dtoReturnReview(r, user.getUsername()))
                .toList();
        return mapper.userToReturn(user, reviewsMapped);
    }
}
