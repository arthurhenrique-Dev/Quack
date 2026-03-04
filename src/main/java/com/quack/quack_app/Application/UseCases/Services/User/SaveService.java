package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Users.BaseUser;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SaveService {

    public static <T extends BaseUser> String execute(
            T user,
            Supplier<Optional<T>> checkMethod,
            TwoFAService service,
            Consumer<T> saveMethod,
            Logger logger
    ) {

        var userOpt = checkMethod.get();
        if (userOpt.isPresent()) {
            var e = new ValidationFailedException("Existing user");
            logger.error(e.getMessage(), e);
            throw e;
        } else {
            String secret;
            try {
                secret = service.setupTwoFa();
                user.prepareTwoFA(secret);
            } catch (Exception e) {
                var pe = new ProcessingErrorException("Error setting up 2FA via External Service");
                logger.error(pe.getMessage(), e);
                throw pe;
            }

            saveMethod.accept(user);

            return service.getQrCodeUrl(secret, user.getId());
        }
    }
}
