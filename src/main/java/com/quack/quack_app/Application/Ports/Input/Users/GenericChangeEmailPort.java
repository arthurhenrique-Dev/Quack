package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Domain.ValueObjects.Email;

import java.util.UUID;

public interface GenericChangeEmailPort {

    void changeEmail(UUID id, Email email);
}
