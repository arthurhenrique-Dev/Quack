package com.quack.quack_app.Application.DTOs.Users;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Domain.ValueObjects.*;

import java.util.List;

public record DTOGetUser(

        Username username,
        String photoUrl,
        Description description,
        FavoriteGames favoriteGames,
        Connections friends,
        Connections followers,
        List<DTOReturnReview> reviews
) {
}
