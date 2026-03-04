package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Input.Users.SearchUsersPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.Utilities.TryGetService;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersUseCase implements SearchUsersPort {

    private static final Logger log = LoggerFactory.getLogger(SearchUsersUseCase.class);

    private final UserRepository repository;
    private final ReviewRepository reviewRepository;
    private final UserMapper userMapper;
    private final ReviewMapper reviewMapper;

    public SearchUsersUseCase(UserRepository repository, ReviewRepository reviewRepository, UserMapper userMapper, ReviewMapper reviewMapper) {
        this.repository = repository;
        this.reviewRepository = reviewRepository;
        this.userMapper = userMapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<DTOGetUser> getUsers(DTOSearchUser dtoSearchUser, Natural pages, Natural size) {
        var users = TryGetService.execute(()-> repository.getUsers(dtoSearchUser, pages, size), log);
        List<DTOGetUser> list = new ArrayList<>();
        users.stream()
                .forEach(u-> {
                    var reviews = TryGetService.execute(()-> reviewRepository.getReviews(u.getId()), log);
                    var reviewsMapped = reviews.filterActiveReviews()
                            .reviews()
                            .stream()
                            .map(r-> reviewMapper.dtoReturnReview(r, u.getUsername()))
                            .toList();
                    list.add(userMapper.userToReturn(u, reviewsMapped));
                });
        return list;
    }
}
