package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Input.Users.Users.ActivatePort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.UseCases.Services.User.ActivationService;
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
                id,
                () -> repository.getModeratorById(id),
                moderator -> repository.saveModerator(moderator),
                logger
        );
    }
}

