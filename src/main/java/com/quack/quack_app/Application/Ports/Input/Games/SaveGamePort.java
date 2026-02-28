package com.quack.quack_app.Application.Ports.Input.Games;

import com.quack.quack_app.Application.DTOs.Games.DTOSaveGame;
import com.quack.quack_app.Domain.Games.Game;

public interface SaveGamePort {

    void saveGame(DTOSaveGame dtoSaveGame);
}
