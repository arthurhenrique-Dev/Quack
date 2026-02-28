package com.quack.quack_app.Application.Ports.Input.Users.Users;

import com.quack.quack_app.Domain.ValueObjects.Username;

import java.util.UUID;

public interface ChangeUsernamePort {

    void changeUsername(UUID id, Username username);
}
