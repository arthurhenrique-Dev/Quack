package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.FollowPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.User.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class FollowUseCase implements FollowPort {

    private static final Logger log = LoggerFactory.getLogger(FollowUseCase.class);

    private final UserRepository repository;

    public FollowUseCase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void follow(UUID idFollower, UUID idFollowing) {
        ConnectionService.execute(
                idFollower,
                idFollowing,
                repository,
                actor -> actor.follow(idFollowing),
                log
        );
    }
}
