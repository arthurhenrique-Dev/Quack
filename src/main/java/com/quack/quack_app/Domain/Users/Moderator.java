package com.quack.quack_app.Domain.Users;

import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Password;
import com.quack.quack_app.Domain.ValueObjects.PasswordUpdater;

import java.util.UUID;

public class Moderator extends BaseUser{
    public Moderator(UUID id, Password password, Email email, Role role, Status status, PasswordUpdater passwordUpdater, boolean twoFactorAuthEnabled) {
        super(id, password, email, role, status, passwordUpdater, twoFactorAuthEnabled);
    }

    public Moderator(Password password, Email email) {
        super(password, email);
        this.role = Role.MODERATOR;
    }
}
