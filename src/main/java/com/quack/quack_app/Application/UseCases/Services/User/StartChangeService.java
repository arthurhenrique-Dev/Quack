package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.UseCases.Services.TryGetByIdService;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.TokenUpdater;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class StartChangeService {

    public static <T extends BaseUser> void execute(
            Supplier<Optional<T>> findMethod,
            Consumer<T> action,
            Function<T, TokenUpdater> tokenExtractor,
            EmailService emailService,
            String emailSubject,
            Consumer<T> saveMethod,
            Logger log
    ){
        var user = TryGetByIdService.execute(
                findMethod,
                () -> new UserNotFoundException(),
                log
        );

        try {
            action.accept(user);

            TokenUpdater updater = tokenExtractor.apply(user);

            if (updater == null || updater.token() == null) {
                var e = new ValidationFailedException("Token was not properly initialized.");
                log.warn(e.getMessage(), e);
                throw e;
            }

            emailService.sendToken(updater.token(), user.getEmail(), emailSubject);

            saveMethod.accept(user);

        } catch (Exception e) {
            log.error("Failed to start change process for user: {}", user.getId(), e);
            throw new ProcessingErrorException("Error initiating update via email.");
        }
    }
}