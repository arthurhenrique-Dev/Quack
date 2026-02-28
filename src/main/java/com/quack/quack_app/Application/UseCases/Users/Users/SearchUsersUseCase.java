package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Application.Ports.Input.Users.Users.SearchUsersPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.TryGetService;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchUsersUseCase implements SearchUsersPort {

    private static final Logger log = LoggerFactory.getLogger(SearchUsersUseCase.class);

    private final UserRepository repository;

    public SearchUsersUseCase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getUsers(DTOSearchUser dtoSearchUser, Natural pages, Natural size) {
        return TryGetService.execute(()-> repository.getUsers(dtoSearchUser, pages, size), log);
    }
}
