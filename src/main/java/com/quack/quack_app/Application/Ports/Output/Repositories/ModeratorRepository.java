package com.quack.quack_app.Application.Ports.Output.Repositories;

import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.ValueObjects.Natural;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModeratorRepository {

    void saveModerator(Moderator moderator);
    List<Moderator> getModerators(DTOSearchModerator dtoSearchModerator, Natural pages, Natural size);
    Optional<Moderator> getModeratorById(UUID id);
}
