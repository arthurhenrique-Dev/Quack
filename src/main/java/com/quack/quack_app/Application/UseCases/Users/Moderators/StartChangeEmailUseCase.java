package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Input.Users.StartChangeEmailPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.UseCases.Services.User.StartChangeService;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class StartChangeEmailUseCase implements StartChangeEmailPort {

    private static final Logger log = LoggerFactory.getLogger(StartChangeEmailUseCase.class);

    private final ModeratorRepository repository;
    private final EmailService service;

    public StartChangeEmailUseCase(ModeratorRepository repository, EmailService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void startChangeEmail(UUID id) {
        StartChangeService.execute(
                () -> repository.getModeratorById(id),
                BaseUser::sendUpdateEmailToken,
                BaseUser::getEmailUpdater,
                service,
                "Email Reset",
                repository::saveModerator,
                log
        );
    }
}
