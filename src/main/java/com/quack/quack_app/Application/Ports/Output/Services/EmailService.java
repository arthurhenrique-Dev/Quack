package com.quack.quack_app.Application.Ports.Output.Services;

import com.quack.quack_app.Domain.ValueObjects.Email;

import java.util.UUID;

public interface EmailService {

    void sendToken(String token, Email email, String subject);
}
