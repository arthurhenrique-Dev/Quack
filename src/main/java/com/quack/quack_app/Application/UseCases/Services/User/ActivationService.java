package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.UseCases.Services.VerifyIfExistsModifyAndSaveService;
import com.quack.quack_app.Domain.Users.BaseUser;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ActivationService {

    public static <T extends BaseUser, X extends RuntimeException> void execute(
            Supplier<Optional<T>> findAction,
            Supplier<X> e,
            Consumer<T> saveAction,
            Logger log) {
        VerifyIfExistsModifyAndSaveService.execute(
                findAction,
                T::activate,
                e,
                saveAction,
                log
        );
    }
}
