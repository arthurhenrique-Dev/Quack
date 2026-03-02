package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Domain.ValueObjects.Password;

import java.util.UUID;

public interface ChangePasswordPort {
    void changePassword(UUID id, UUID token, Password password);
}
