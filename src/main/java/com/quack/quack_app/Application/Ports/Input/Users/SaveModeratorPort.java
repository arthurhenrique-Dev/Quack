package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveModerator;

public interface SaveModeratorPort {

    String saveModerator(DTOSaveModerator moderator);
}
