package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.StartChangeEmailPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.UseCases.Services.User.StartChangeService;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class StartChangeEmailUseCase implements StartChangeEmailPort {

    private static final Logger log = LoggerFactory.getLogger(StartChangeEmailUseCase.class);

    private final UserRepository repository;
    private final EmailService service;

    public StartChangeEmailUseCase(UserRepository repository, EmailService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void startChangeEmail(UUID id) {
        StartChangeService.execute(
                () -> repository.getUserById(id),
                BaseUser::sendUpdateEmailToken,
                BaseUser::getEmailUpdater,
                service,
                "Email Reset",
                repository::saveUser,
                log
        );
    }
}
