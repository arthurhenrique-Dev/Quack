package com.quack.quack_app.Application.Ports.Output.Repositories;

import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.ValueObjects.Email;

import java.util.Optional;
import java.util.UUID;

public interface ModeratorRepository {

    void saveModerator(Moderator moderator);
    Optional<Moderator> getModeratorById(UUID id);
    Optional<Moderator> getModeratorByEmail(Email email);
}
