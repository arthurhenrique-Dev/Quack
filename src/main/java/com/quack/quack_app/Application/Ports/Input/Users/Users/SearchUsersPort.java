package com.quack.quack_app.Application.Ports.Input.Users.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Natural;

import java.util.List;

public interface SearchUsersPort {

    List<User> getUsers(DTOSearchUser dtoSearchUser, Natural pages, Natural size);
}
