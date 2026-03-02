package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.UnfollowPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.User.UserConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class UnfollowUseCase implements UnfollowPort {

    private static final Logger log = LoggerFactory.getLogger(UnfollowUseCase.class);

    private final UserRepository repository;

    public UnfollowUseCase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void unfollow(UUID unfollower, UUID unfollowing) {
        UserConnectionService.execute(
                unfollower,
                unfollowing,
                repository,
                actor -> actor.unfollow(unfollowing),
                target -> target.otherPartRemoveFriend(unfollower),
                log
        );
    }
}
