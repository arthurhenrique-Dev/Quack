package com.quack.quack_app.Application.UseCases.Games;

import com.quack.quack_app.Application.DTOs.Games.DTOSaveGame;
import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Application.Mappers.Games.GameMapper;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleGamesUseCasesTests {

    @Mock private GameRepository gameRepository;
    @Mock private GameMapper gameMapper;
    @Mock private Game gameMock;

    @Test
    @DisplayName("Deve salvar jogo")
    void testSaveGame() {
        DTOSaveGame dto = mock(DTOSaveGame.class);
        when(gameMapper.ToRegister(dto)).thenReturn(gameMock);

        new SaveGameUseCase(gameMapper, gameRepository).saveGame(dto);

        verify(gameRepository).saveGame(gameMock);
    }

    @Test
    @DisplayName("Deve buscar jogo por id")
    void testGetGameById() {
        UUID gameId = UUID.randomUUID();
        when(gameRepository.getGameById(gameId)).thenReturn(Optional.of(gameMock));

        Game result = new GetGameByIdUseCase(gameRepository).getGameById(gameId);

        assertThat(result).isEqualTo(gameMock);
    }

    @Test
    @DisplayName("Deve listar jogos")
    void testGetGames() {
        DTOSearchGame dto = mock(DTOSearchGame.class);
        Natural pages = mock(Natural.class);
        Natural size = mock(Natural.class);
        when(gameRepository.getGames(dto, pages, size)).thenReturn(List.of(gameMock));

        List<Game> result = new GetGamesUseCase(gameRepository).getGames(dto, pages, size);

        assertThat(result).containsExactly(gameMock);
    }
}