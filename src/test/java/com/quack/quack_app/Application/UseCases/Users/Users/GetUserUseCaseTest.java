package com.quack.quack_app.Application.UseCases.Users.Users;

import static org.junit.jupiter.api.Assertions.*;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @Mock UserRepository userRepository;
    @Mock ReviewRepository reviewRepository;
    @Mock UserMapper userMapper;
    @Mock ReviewMapper reviewMapper;

    @InjectMocks GetUserUseCase useCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestModels.activeUser();
    }

    @Test
    @DisplayName("Deve retornar DTOGetUser com reviews ativas mapeadas")
    void getUser_success() {
        Reviews reviews = TestModels.reviewsWithOneActive();
        DTOReturnReview dtoReview = TestModels.dtoReturnReview();
        DTOGetUser expected = mock(DTOGetUser.class);

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID)).thenReturn(reviews);
        when(reviewMapper.dtoReturnReview(any(), eq(user.getUsername()))).thenReturn(dtoReview);
        when(userMapper.userToReturn(eq(user), anyList())).thenReturn(expected);

        DTOGetUser result = useCase.getUser(TestModels.USER_ID);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(reviewMapper, times(1)).dtoReturnReview(any(), any());
    }

    @Test
    @DisplayName("Deve retornar DTOGetUser sem reviews quando não houver reviews ativas")
    void getUser_noActiveReviews_success() {
        DTOGetUser expected = mock(DTOGetUser.class);

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID)).thenReturn(TestModels.emptyReviews());
        when(userMapper.userToReturn(eq(user), eq(List.of()))).thenReturn(expected);

        DTOGetUser result = useCase.getUser(TestModels.USER_ID);

        assertNotNull(result);
        verifyNoInteractions(reviewMapper);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando usuário não existir")
    void getUser_userNotFound_throwsUserNotFoundException() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.getUser(TestModels.USER_ID));

        verifyNoInteractions(reviewRepository, reviewMapper, userMapper);
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando reviewRepository falhar")
    void getUser_reviewRepositoryFails_throwsProcessingError() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.getReviews(TestModels.USER_ID))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class, () -> useCase.getUser(TestModels.USER_ID));
    }
}