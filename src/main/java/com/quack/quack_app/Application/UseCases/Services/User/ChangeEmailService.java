package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.UseCases.Services.Utilities.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChangeEmailService {

    public static <T extends BaseUser> void execute(
            UUID id,
            Email email,
            Supplier<Optional<T>> findAction,
            Consumer<T> saveAction,
            Logger log) {
        VerifyIfExistsModifyAndSaveService.execute(
                findAction,
                user -> user.checkAndChangeEmail(id, email),
                ()-> new UserNotFoundException(),
                saveAction,
                log
        );
    }
}
