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
            Consumer<T> sendEmailMethod,
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
            } catch (Exception e) {
                var pe = new ProcessingErrorException("Error setting up 2FA via External Service");
                logger.error(pe.getMessage(), e);
                throw pe;
            }

            user.prepareTwoFA(secret);
            saveMethod.accept(user);

            try {
                sendEmailMethod.accept(user);
                logger.info("E-mail de verificação enviado para: {}", user.getEmail().email());
            } catch (Exception e) {
                logger.error("Falha ao enviar e-mail, mas usuário foi salvo", e);
            }

            return service.getQrCodeUrl(secret, user.getId());
        }
    }
}
