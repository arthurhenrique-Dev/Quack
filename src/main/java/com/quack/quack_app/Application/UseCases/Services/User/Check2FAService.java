package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Application.UseCases.Services.Utilities.TryGetByIdService;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Users.BaseUser;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Check2FAService {

    public static <T extends BaseUser> void execute(
            Supplier<Optional<T>> findMethod,
            String code,
            TwoFAService service,
            Consumer<T> saveMethod,
            Logger log
    ){
        var user = TryGetByIdService.execute(
                findMethod,
                ()-> new UserNotFoundException(),
                log
        );
        boolean isValid;

        try {
            isValid = service.checkCode(user.getTwoFA().secret(), code);
        } catch (Exception e) {
            log.error("Technical failure while validating 2FA", e);
            throw new ProcessingErrorException();
        }

        if (!isValid) {
            var e = new ValidationFailedException("Invalid code");
            log.warn(e.getMessage());
            throw e;
        }

        try {
            user.enableTwoFA();
            user.activate();
            saveMethod.accept(user);
        } catch (Exception e) {
            log.error("Error persisting 2FA activation", e);
            throw new ProcessingErrorException();
        }
    }
}