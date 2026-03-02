package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Input.Users.ChangeEmailPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.UseCases.Services.User.ChangeEmailService;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ChangeEmailUseCase implements ChangeEmailPort {

    private static final Logger log = LoggerFactory.getLogger(ChangeEmailUseCase.class);

    private final ModeratorRepository repository;

    public ChangeEmailUseCase(ModeratorRepository repository) {
        this.repository = repository;
    }


    @Override
    public void changeEmail(UUID id, UUID token, Email email) {
        ChangeEmailService.execute(
                token,
                email,
                ()-> repository.getModeratorById(id),
                user-> repository.saveModerator(user),
                log
        );
    }
}
