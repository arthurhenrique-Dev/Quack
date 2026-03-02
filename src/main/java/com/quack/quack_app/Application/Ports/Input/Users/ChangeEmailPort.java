package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Domain.ValueObjects.Email;

import java.util.UUID;

public interface ChangeEmailPort {

    void changeEmail(UUID id, UUID token, Email email);
}
