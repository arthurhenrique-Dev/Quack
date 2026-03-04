package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.ChangeUsernamePort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.Utilities.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.ValueObjects.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ChangeUsernameUseCase implements ChangeUsernamePort {

    private static final Logger log = LoggerFactory.getLogger(ChangeUsernameUseCase.class);

    private final UserRepository repository;

    public ChangeUsernameUseCase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void changeUsername(UUID id, Username username) {
        VerifyIfExistsModifyAndSaveService.execute(
                ()-> repository.getUserById(id),
                user -> user.changeUsername(username),
                ()-> new UserNotFoundException(),
                user -> repository.saveUser(user),
                log
        );
    }
}
