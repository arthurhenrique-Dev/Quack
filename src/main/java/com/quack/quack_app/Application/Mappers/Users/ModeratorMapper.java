package com.quack.quack_app.Application.Mappers.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveModerator;
import com.quack.quack_app.Domain.Users.Moderator;

public class ModeratorMapper {

    public Moderator moderatorToRegister(DTOSaveModerator dtoSaveModerator) {
        return new Moderator(
                dtoSaveModerator.password(),
                dtoSaveModerator.email()
        );
    }
}
