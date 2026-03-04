package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.ChangeProfilePhotoPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.Utilities.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ChangeProfilePhotoUseCase implements ChangeProfilePhotoPort {

    private static final Logger log = LoggerFactory.getLogger(ChangeProfilePhotoUseCase.class);

    private final UserRepository repository;

    public ChangeProfilePhotoUseCase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void changeProfilePhoto(UUID idUser, String photoUrl) {
        VerifyIfExistsModifyAndSaveService.execute(
                ()-> repository.getUserById(idUser),
                user -> user.changePhoto(photoUrl),
                ()-> new UserNotFoundException(),
                user -> repository.saveUser(user),
                log
        );
    }
}
