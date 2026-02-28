package com.quack.quack_app.Application.Ports.Input.Users.Users;

import com.quack.quack_app.Domain.ValueObjects.Description;

import java.util.UUID;

public interface ChangeDescriptionPort {

    void changeDescription(UUID idUser, Description description);
}
