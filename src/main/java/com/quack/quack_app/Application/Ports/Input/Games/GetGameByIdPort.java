package com.quack.quack_app.Application.Ports.Input.Games;

import com.quack.quack_app.Domain.Games.Game;

import java.util.UUID;

public interface GetGameByIdPort {

    Game getGameById(UUID gameId);
}
