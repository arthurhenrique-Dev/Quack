package com.quack.quack_app.Application.Ports.Output.Services;

import com.quack.quack_app.Domain.ValueObjects.Email;

public interface EmailService {

    void send(String content, Email email, String subject);
}
