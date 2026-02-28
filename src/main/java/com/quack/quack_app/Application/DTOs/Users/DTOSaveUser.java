package com.quack.quack_app.Application.DTOs.Users;

import com.quack.quack_app.Domain.ValueObjects.Description;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Password;
import com.quack.quack_app.Domain.ValueObjects.Username;

public record DTOSaveUser(Username username, Password password, Email email, Description description, String photoUrl) {
}
