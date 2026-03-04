package com.quack.quack_app.Application.UseCases.Users.Users;

import static org.junit.jupiter.api.Assertions.*;

import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BanUseCaseTest {

    @Mock UserRepository userRepository;
    @Mock ReviewRepository reviewRepository;

    @InjectMocks BanUseCase useCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestModels.activeUser();
    }

    @Test
    @DisplayName("Deve banir usuário e remover reviews ativas com sucesso")
    void ban_success() {
        Reviews reviews = TestModels.reviewsWithOneActive();

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID)).thenReturn(reviews);

        assertDoesNotThrow(() -> useCase.ban(TestModels.USER_ID));

        verify(userRepository).saveUser(user);
        verify(reviewRepository, times(reviews.reviews().size())).saveReview(any(Review.class));
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando usuário não existir")
    void ban_userNotFound_throwsUserNotFoundException() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.ban(TestModels.USER_ID));

        verifyNoInteractions(reviewRepository);
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando getReviews falhar")
    void ban_reviewRepositoryFails_throwsProcessingError() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class, () -> useCase.ban(TestModels.USER_ID));
    }

    @Test
    @DisplayName("Deve banir usuário mesmo sem reviews ativas")
    void ban_noActiveReviews_success() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID)).thenReturn(TestModels.emptyReviews());

        assertDoesNotThrow(() -> useCase.ban(TestModels.USER_ID));

        verify(userRepository).saveUser(user);
        verify(reviewRepository, never()).saveReview(any());
    }
}