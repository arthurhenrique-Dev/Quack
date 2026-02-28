package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Input.Users.Users.ChangePasswordPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.UseCases.Services.User.ChangePasswordService;
import com.quack.quack_app.Domain.ValueObjects.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ChangePasswordUseCase implements ChangePasswordPort{

    private static final Logger log = LoggerFactory.getLogger(ChangePasswordUseCase.class);

    private final ModeratorRepository repository;

    public ChangePasswordUseCase(ModeratorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void changePassword(UUID id, UUID token, Password password) {
        ChangePasswordService.execute(
                id,
                token,
                password,
                () -> repository.getModeratorById(id),
                moderator -> repository.saveModerator(moderator),
                log
        );
    }
}
