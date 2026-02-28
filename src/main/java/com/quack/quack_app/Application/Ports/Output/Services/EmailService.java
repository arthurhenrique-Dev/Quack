package com.quack.quack_app.Application.Ports.Output.Services;

import com.quack.quack_app.Domain.ValueObjects.Email;

import java.util.UUID;

public interface EmailService {

    void ValidateEmail(Email email);
    void UpdatePassword(UUID token, Email email);
}
