package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.UseCases.Services.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.Password;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChangePasswordService {

    public static <T extends BaseUser> void execute(
            UUID token,
            Password password,
            Supplier<Optional<T>> findAction,
            Consumer<T> saveAction,
            Logger logger) {

        VerifyIfExistsModifyAndSaveService.execute(
                findAction,
                user-> user.checkAndChangePassword(token, password),
                ()-> new UserNotFoundException(),
                saveAction,
                logger
        );
    }
}
