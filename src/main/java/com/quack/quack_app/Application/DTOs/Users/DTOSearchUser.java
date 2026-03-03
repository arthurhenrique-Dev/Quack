package com.quack.quack_app.Application.DTOs.Users;

import java.util.UUID;

public record DTOSearchUser(
        UUID id,
        String usename
) {
}
