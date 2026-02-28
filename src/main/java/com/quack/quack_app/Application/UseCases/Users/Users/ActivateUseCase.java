package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.Users.ActivatePort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.User.ActivationService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ActivateUseCase implements ActivatePort {

    Logger logger = LoggerFactory.getLogger(ActivateUseCase.class);

    private final UserRepository repository;

    public ActivateUseCase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void activate(UUID id) {
        ActivationService.execute(
                ()-> repository.getUserById(id),
                ()-> new UserNotFoundException(),
                user -> repository.saveUser(user),
                logger
        );
    }
}

