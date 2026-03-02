package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.ChangeDescriptionPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.ValueObjects.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ChangeDescriptionUseCase implements ChangeDescriptionPort {

    private static final Logger log = LoggerFactory.getLogger(ChangeDescriptionUseCase.class);

    private final UserRepository repository;

    public ChangeDescriptionUseCase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void changeDescription(UUID idUser, Description description) {
        VerifyIfExistsModifyAndSaveService.execute(
                ()-> repository.getUserById(idUser),
                user -> user.changeDescription(description),
                ()-> new UserNotFoundException(),
                user -> repository.saveUser(user),
                log
        );
    }
}
