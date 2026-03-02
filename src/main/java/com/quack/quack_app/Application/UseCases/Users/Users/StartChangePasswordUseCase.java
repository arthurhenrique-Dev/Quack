package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.StartChangePasswordPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.UseCases.Services.User.StartChangeService;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class StartChangePasswordUseCase implements StartChangePasswordPort {

    private static final Logger logger = LoggerFactory.getLogger(StartChangePasswordUseCase.class);

    private final UserRepository repository;
    private final EmailService service;

    public StartChangePasswordUseCase(UserRepository repository, EmailService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void startChangePassword(UUID id) {
        StartChangeService.execute(
                () -> repository.getUserById(id),
                BaseUser::sendUpdatePasswordToken,
                BaseUser::getPasswordUpdater,
                service,
                "Password Reset",
                repository::saveUser,
                logger
        );
    }
}
