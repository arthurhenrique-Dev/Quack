package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Domain.ValueObjects.Natural;

import java.util.List;

public interface SearchUsersPort {

    List<DTOGetUser> getUsers(DTOSearchUser dtoSearchUser, Natural pages, Natural size);
}
