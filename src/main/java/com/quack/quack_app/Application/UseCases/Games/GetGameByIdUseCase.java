package com.quack.quack_app.Application.UseCases.Games;

import com.quack.quack_app.Application.Ports.Input.Games.GetGameByIdPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Application.UseCases.Services.Utilities.TryGetByIdService;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Games.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class GetGameByIdUseCase implements GetGameByIdPort {

    private static final Logger log = LoggerFactory.getLogger(SaveGameUseCase.class);

    private final GameRepository repository;

    public GetGameByIdUseCase(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public Game getGameById(UUID gameId) {
        return TryGetByIdService.execute(
                ()-> repository.getGameById(gameId),
                ()-> new InvalidDataException("Game with id " + gameId + " not found"),
                log
        );
    }
}
