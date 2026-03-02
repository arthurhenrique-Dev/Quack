package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.DTOs.Games.DTOSaveGame;
import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Application.UseCases.Games.GetGameByIdUseCase;
import com.quack.quack_app.Application.UseCases.Games.GetGamesUseCase;
import com.quack.quack_app.Application.UseCases.Games.SaveGameUseCase;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("quack/games")
public class GameController {

    private final GetGameByIdUseCase getGameByIdUseCase;
    private final GetGamesUseCase getGamesUseCase;
    private final SaveGameUseCase saveGameUseCase;

    public GameController(GetGameByIdUseCase getGameByIdUseCase, GetGamesUseCase getGamesUseCase, SaveGameUseCase saveGameUseCase) {
        this.getGameByIdUseCase = getGameByIdUseCase;
        this.getGamesUseCase = getGamesUseCase;
        this.saveGameUseCase = saveGameUseCase;
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable UUID id) {
        return getGameByIdUseCase.getGameById(id);
    }
    @GetMapping()
    public List<Game> getGames(DTOSearchGame searchGame,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        var p = new Natural(page);
        var s = new Natural(size);
        return getGamesUseCase.getGames(searchGame, p, s);
    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveGame(@RequestBody DTOSaveGame game) {
        saveGameUseCase.saveGame(game);
    }
}
