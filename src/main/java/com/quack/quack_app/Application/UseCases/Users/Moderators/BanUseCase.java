package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Input.Users.BanPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.UseCases.Services.User.BanimentService;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class BanUseCase implements BanPort {

    private static final Logger log = LoggerFactory.getLogger(BanUseCase.class);

    private final ModeratorRepository repository;

    public BanUseCase(ModeratorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void ban(UUID id) {
        BanimentService.execute(
                ()-> repository.getModeratorById(id),
                ()-> new UserNotFoundException(),
                user -> repository.saveModerator(user),
                log
        );
    }
}
