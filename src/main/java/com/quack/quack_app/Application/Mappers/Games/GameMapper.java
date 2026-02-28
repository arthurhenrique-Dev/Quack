package com.quack.quack_app.Application.Mappers.Games;

import com.quack.quack_app.Application.DTOs.Games.DTOSaveGame;
import com.quack.quack_app.Domain.Games.Game;

public class GameMapper {

    public Game ToRegister(DTOSaveGame dtoSaveGame){
        return new Game(
                dtoSaveGame.name(),
                dtoSaveGame.description(),
                dtoSaveGame.releaseDate(),
                dtoSaveGame.genre(),
                dtoSaveGame.developer(),
                dtoSaveGame.publisher(),
                dtoSaveGame.photoUrl(),
                dtoSaveGame.platforms()
        );
    }
}
