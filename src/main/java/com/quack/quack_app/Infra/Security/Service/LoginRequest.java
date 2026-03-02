package com.quack.quack_app.Infra.Security.Service;

import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Password;

public record LoginRequest(Email email, Password password) {
}
