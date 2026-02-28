package com.quack.quack_app.Application.Ports.Output.Repositories;

import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Domain.Games.Game;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository {

    void saveGame(Game game);
    void deleteGame(Game game);
    List<Game> getGames(DTOSearchGame dtoSearchGame);
    Optional<Game> getGameById(UUID id);
}
