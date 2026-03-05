package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.DTOs.Games.DTOSaveGame;
import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Application.UseCases.Games.GetGameByIdUseCase;
import com.quack.quack_app.Application.UseCases.Games.GetGamesUseCase;
import com.quack.quack_app.Application.UseCases.Games.SaveGameUseCase;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "return games by id", description = "requisition that return games by a id")
    @ApiResponse(responseCode = "200", description = "game returned with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public Game getGameById(@PathVariable UUID id) {
        return getGameByIdUseCase.getGameById(id);
    }
    @GetMapping()
    @Operation(summary = "return games by filter", description = "requisition that return games by a filter")
    @ApiResponse(responseCode = "200", description = "games returned with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public List<Game> getGames(DTOSearchGame searchGame,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        var p = new Natural(page);
        var s = new Natural(size);
        return getGamesUseCase.getGames(searchGame, p, s);
    }
    @PostMapping()
    @Operation(summary = "register game", description = "requisition that register a new game in Quack system")
    @ApiResponse(responseCode = "201", description = "game created with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "500", description = "internal server error")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveGame(@RequestBody DTOSaveGame game) {
        saveGameUseCase.saveGame(game);
    }
}
