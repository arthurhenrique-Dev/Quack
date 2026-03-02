package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Input.Users.ActivatePort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.UseCases.Services.User.ActivationService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ActivateUseCase implements ActivatePort {

    Logger logger = LoggerFactory.getLogger(ActivateUseCase.class);

    private final ModeratorRepository repository;

    public ActivateUseCase(ModeratorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void activate(UUID id) {
        ActivationService.execute(
                ()-> repository.getModeratorById(id),
                ()-> new UserNotFoundException(),
                user -> repository.saveModerator(user),
                logger
        );
    }
}

