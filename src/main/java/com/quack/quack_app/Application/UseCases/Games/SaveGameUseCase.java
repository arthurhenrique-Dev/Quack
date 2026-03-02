package com.quack.quack_app.Application.UseCases.Games;

import com.quack.quack_app.Application.DTOs.Games.DTOSaveGame;
import com.quack.quack_app.Application.Mappers.Games.GameMapper;
import com.quack.quack_app.Application.Ports.Input.Games.SaveGamePort;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Application.UseCases.Services.TrySaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveGameUseCase implements SaveGamePort {

    private static final Logger log = LoggerFactory.getLogger(SaveGameUseCase.class);

    private final GameMapper gameMapper;
    private final GameRepository repository;

    public SaveGameUseCase(GameMapper gameMapper, GameRepository repository) {
        this.gameMapper = gameMapper;
        this.repository = repository;
    }

    @Override
    public void saveGame(DTOSaveGame dtoSaveGame) {
        TrySaveService.execute(
                gameMapper.ToRegister(dtoSaveGame),
                repository::saveGame,
                log
        );
    }
}