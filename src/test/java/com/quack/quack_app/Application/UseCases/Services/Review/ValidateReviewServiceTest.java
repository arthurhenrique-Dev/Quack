package com.quack.quack_app.Application.UseCases.Services.Review;

import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateReviewServiceTest {

    @Mock ReviewRepository reviewRepository;
    @Mock UserRepository userRepository;

    @InjectMocks ValidateReviewService service;

    private Review review;
    private User user;

    @BeforeEach
    void setUp() {
        review = TestModels.activeReview();
        user   = TestModels.activeUser();
    }

    @Test
    @DisplayName("Deve retornar review quando tudo estiver válido")
    void validateReview_success() {
        when(reviewRepository.getReview(TestModels.REVIEW_ID)).thenReturn(Optional.of(review));
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));

        Review result = service.validateReview(TestModels.REVIEW_ID, TestModels.USER_ID);

        assertNotNull(result);
        assertEquals(TestModels.REVIEW_ID, result.getReviewId());
    }

    @Test
    @DisplayName("Deve lançar InvalidDataException quando review não existir")
    void validateReview_reviewNotFound_throwsInvalidData() {
        when(reviewRepository.getReview(TestModels.REVIEW_ID)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class,
                () -> service.validateReview(TestModels.REVIEW_ID, TestModels.USER_ID));

        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Deve lançar ValidationFailedException quando userId não corresponder ao review")
    void validateReview_wrongUser_throwsValidationFailed() {
        UUID otherUserId = UUID.randomUUID();
        when(reviewRepository.getReview(TestModels.REVIEW_ID)).thenReturn(Optional.of(review));

        assertThrows(ValidationFailedException.class,
                () -> service.validateReview(TestModels.REVIEW_ID, otherUserId));

        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando usuário do review não existir")
    void validateReview_userNotFound_throwsUserNotFoundException() {
        when(reviewRepository.getReview(TestModels.REVIEW_ID)).thenReturn(Optional.of(review));
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.validateReview(TestModels.REVIEW_ID, TestModels.USER_ID));
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando userRepository lançar exceção inesperada")
    void validateReview_repositoryThrows_throwsProcessingError() {
        when(reviewRepository.getReview(TestModels.REVIEW_ID)).thenReturn(Optional.of(review));
        when(userRepository.getUserById(TestModels.USER_ID))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class,
                () -> service.validateReview(TestModels.REVIEW_ID, TestModels.USER_ID));
    }
}