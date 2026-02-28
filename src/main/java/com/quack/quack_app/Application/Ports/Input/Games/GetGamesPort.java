package com.quack.quack_app.Application.Ports.Input.Games;

import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.ValueObjects.Natural;

import java.util.List;

public interface GetGamesPort {

    List<Game> getGames(DTOSearchGame dtoSearchGame, Natural pages, Natural size);
}
