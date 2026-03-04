package com.quack.quack_app.Application.UseCases.Games;

import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Application.Ports.Input.Games.GetGamesPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Application.UseCases.Services.Utilities.TryGetService;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetGamesUseCase implements GetGamesPort {

    private static final Logger log = LoggerFactory.getLogger(GetGamesUseCase.class);
    private final GameRepository repository;

    public GetGamesUseCase(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Game> getGames(DTOSearchGame dtoSearchGame, Natural pages, Natural size) {
        return TryGetService.execute(
                ()-> repository.getGames(dtoSearchGame, pages, size),
                log
        );
    }
}