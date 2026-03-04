package com.quack.quack_app.Application.UseCases.Users.Users;

import static org.junit.jupiter.api.Assertions.*;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserReviewsUseCaseTest {

    @Mock UserRepository userRepository;
    @Mock ReviewRepository reviewRepository;
    @Mock ReviewMapper reviewMapper;

    @InjectMocks GetUserReviewsUseCase useCase;

    private User user;
    private Natural pages;
    private Natural size;

    @BeforeEach
    void setUp() {
        user  = TestModels.activeUser();
        pages = new Natural(0);
        size  = new Natural(10);
    }

    @Test
    @DisplayName("Deve retornar lista de reviews do usuário com sucesso")
    void getReviewsByUser_success() {
        Reviews reviews = TestModels.reviewsWithOneActive();
        DTOReturnReview dto = TestModels.dtoReturnReview();

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID)).thenReturn(reviews);
        when(reviewMapper.dtoReturnReview(any(), eq(user.getUsername()))).thenReturn(dto);

        List<DTOReturnReview> result = useCase.getReviewsByUser(TestModels.USER_ID, pages, size);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver reviews")
    void getReviewsByUser_noReviews_returnsEmpty() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID)).thenReturn(TestModels.emptyReviews());

        List<DTOReturnReview> result = useCase.getReviewsByUser(TestModels.USER_ID, pages, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando usuário não existir")
    void getReviewsByUser_userNotFound_throwsUserNotFoundException() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> useCase.getReviewsByUser(TestModels.USER_ID, pages, size));

        verifyNoInteractions(reviewRepository, reviewMapper);
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando reviewRepository falhar")
    void getReviewsByUser_reviewRepositoryFails_throwsProcessingError() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class,
                () -> useCase.getReviewsByUser(TestModels.USER_ID, pages, size));
    }
}