package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.Users.ChangeEmailPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.User.ChangeEmailService;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ChangeEmailUseCase implements ChangeEmailPort {

    private static final Logger log = LoggerFactory.getLogger(ChangeEmailUseCase.class);

    private final UserRepository repository;

    public ChangeEmailUseCase(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public void changeEmail(UUID id, Email email) {
        ChangeEmailService.execute(
                email,
                ()-> repository.getUserById(id),
                user-> repository.saveUser(user),
                log
        );
    }
}
