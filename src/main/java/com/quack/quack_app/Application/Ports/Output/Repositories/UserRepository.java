package com.quack.quack_app.Application.Ports.Output.Repositories;

import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Username;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void saveUser(User user);
    List<User> getUsers(DTOSearchUser dtoSearchUser, Natural pages, Natural size);
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByName(Username username);
}
