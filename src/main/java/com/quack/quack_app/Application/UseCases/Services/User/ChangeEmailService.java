package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.UseCases.Services.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChangeEmailService {

    public static <T extends BaseUser, X extends RuntimeException> void execute(
            Email email,
            Supplier<Optional<T>> findAction,
            Consumer<T> saveAction,
            Logger log) {
        VerifyIfExistsModifyAndSaveService.execute(
                findAction,
                user -> user.changeEmail(email),
                ()-> new UserNotFoundException(),
                saveAction,
                log
        );
    }
}
