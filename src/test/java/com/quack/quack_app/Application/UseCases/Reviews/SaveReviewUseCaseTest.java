package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.quack.quack_app.Application.DTOs.Reviews.DTOSaveReview;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveReviewUseCaseTest {

    @Mock ReviewRepository reviewRepository;
    @Mock GameRepository gameRepository;
    @Mock UserRepository userRepository;
    @Mock ReviewMapper mapper;
    @Mock EmailService emailService;

    @InjectMocks SaveReviewUseCase useCase;

    private User user;
    private Game game;
    private Review review;
    private DTOSaveReview dto;

    @BeforeEach
    void setUp() {
        user   = TestModels.activeUser();
        game   = TestModels.gameWithoutReviews();
        review = TestModels.activeReview();
        dto    = TestModels.dtoSaveReview();
    }

    @Test
    @DisplayName("Deve salvar review, atualizar rating e enviar e-mail com sucesso")
    void saveReview_success() {
        when(userRepository.getUserById(dto.userId())).thenReturn(Optional.of(user));
        when(gameRepository.getGameById(dto.gameId())).thenReturn(Optional.of(game));
        when(mapper.toRegister(dto)).thenReturn(review);

        assertDoesNotThrow(() -> useCase.saveReviews(dto));

        verify(gameRepository).saveGame(game);
        verify(reviewRepository).saveReview(review);
        verify(emailService).send(
                contains(game.getName()),
                eq(user.getEmail()),
                anyString()
        );
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando userRepository falhar")
    void saveReview_userRepositoryFails_throwsProcessingError() {
        when(userRepository.getUserById(dto.userId()))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class, () -> useCase.saveReviews(dto));

        verifyNoInteractions(gameRepository, reviewRepository, emailService);
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando gameRepository falhar")
    void saveReview_gameRepositoryFails_throwsProcessingError() {
        when(userRepository.getUserById(dto.userId())).thenReturn(Optional.of(user));
        when(gameRepository.getGameById(dto.gameId()))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class, () -> useCase.saveReviews(dto));

        verifyNoInteractions(reviewRepository, emailService);
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando salvar review falhar")
    void saveReview_saveFails_throwsProcessingError() {
        when(userRepository.getUserById(dto.userId())).thenReturn(Optional.of(user));
        when(gameRepository.getGameById(dto.gameId())).thenReturn(Optional.of(game));
        when(mapper.toRegister(dto)).thenReturn(review);
        doThrow(new RuntimeException("DB down")).when(reviewRepository).saveReview(any());

        assertThrows(ProcessingErrorException.class, () -> useCase.saveReviews(dto));
    }
}
