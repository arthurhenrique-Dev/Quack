package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.TryGetByIdService;
import com.quack.quack_app.Application.UseCases.Services.TrySaveService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Users.User;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.function.Consumer;

public class ConnectionService {

    public static void execute(
            UUID actorId,
            UUID targetId,
            UserRepository repository,
            Consumer<User> actionTarget,
            Logger log
    ) {
        TryGetByIdService.execute(
                () -> repository.getUserById(actorId),
                UserNotFoundException::new,
                log
        );
        User target = TryGetByIdService.execute(
                () -> repository.getUserById(targetId),
                UserNotFoundException::new,
                log
        );

        actionTarget.accept(target);
        TrySaveService.execute(target, repository::saveUser, log);
    }
}
