package com.quack.quack_app.Application.UseCases.Games;

import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Application.Ports.Input.Games.GetGamesPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GetGamesUseCase implements GetGamesPort {

    private static final Logger log = LoggerFactory.getLogger(GetGamesUseCase.class);
    private final GameRepository repository;

    public GetGamesUseCase(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Game> getGames(DTOSearchGame dtoSearchGame, Natural pages, Natural size) {
        try {
            return Optional.ofNullable(repository.getGames(dtoSearchGame))
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .toList();
        } catch (Exception ex) {
            log.error("Technical failure retrieving games with filter [{}]: ", dtoSearchGame, ex);
            throw new ProcessingErrorException("Could not retrieve games list");
        }
    }
}