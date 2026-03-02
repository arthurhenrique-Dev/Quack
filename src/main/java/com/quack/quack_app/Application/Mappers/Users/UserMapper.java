package com.quack.quack_app.Application.Mappers.Users;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Domain.Users.User;

import java.util.List;

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
    public DTOGetUser userToReturn(User user, List<DTOReturnReview> dtoReturnReview){
        return new DTOGetUser(
                user.getUsername(),
                user.getPhotoUrl(),
                user.getDescription(),
                user.getFavoriteGames(),
                user.getFriends(),
                user.getFollowers(),
                dtoReturnReview
        );
    }
}
