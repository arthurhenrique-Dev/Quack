package com.quack.quack_app.Application.DTOs.Users;

import com.quack.quack_app.Domain.ValueObjects.Username;

import java.util.UUID;

public record DTOSearchUser(
        UUID id,
        Username usename
) {
}
