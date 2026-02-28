package com.quack.quack_app.Application.Mappers.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Domain.Users.User;

public class UserMapper {

    public User userToRegister(DTOSaveUser dtoSaveUser){
        return new User(
                dtoSaveUser.username(),
                dtoSaveUser.password(),
                dtoSaveUser.email(),
                dtoSaveUser.description(),
                dtoSaveUser.photoUrl()
        );
    }
}
