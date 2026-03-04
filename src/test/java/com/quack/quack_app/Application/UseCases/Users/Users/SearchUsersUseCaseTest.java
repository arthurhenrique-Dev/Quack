package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchUsersUseCaseTest {

    @Mock UserRepository userRepository;
    @Mock ReviewRepository reviewRepository;
    @Mock UserMapper userMapper;
    @Mock ReviewMapper reviewMapper;

    @InjectMocks SearchUsersUseCase useCase;

    private User user;
    private DTOSearchUser searchDto;
    private Natural pages;
    private Natural size;

    @BeforeEach
    void setUp() {
        user      = TestModels.activeUser();
        searchDto = new DTOSearchUser(TestModels.USER_ID, "quack");
        pages     = new Natural(0);
        size      = new Natural(10);
    }

    @Test
    @DisplayName("Deve retornar lista de usuários com suas reviews mapeadas")
    void getUsers_success() {
        DTOReturnReview dtoReview = TestModels.dtoReturnReview();
        DTOGetUser dtoGetUser    = mock(DTOGetUser.class);

        when(userRepository.getUsers(searchDto, pages, size)).thenReturn(List.of(user));
        when(reviewRepository.getReviews(user.getId())).thenReturn(TestModels.reviewsWithOneActive());
        when(reviewMapper.dtoReturnReview(any(), eq(user.getUsername()))).thenReturn(dtoReview);
        when(userMapper.userToReturn(eq(user), anyList())).thenReturn(dtoGetUser);

        List<DTOGetUser> result = useCase.getUsers(searchDto, pages, size);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum usuário for encontrado")
    void getUsers_noResults_returnsEmpty() {
        when(userRepository.getUsers(searchDto, pages, size)).thenReturn(List.of());

        List<DTOGetUser> result = useCase.getUsers(searchDto, pages, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(reviewRepository, reviewMapper, userMapper);
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando userRepository falhar")
    void getUsers_repositoryFails_throwsProcessingError() {
        when(userRepository.getUsers(searchDto, pages, size))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class,
                () -> useCase.getUsers(searchDto, pages, size));
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando reviewRepository falhar para um usuário")
    void getUsers_reviewRepositoryFails_throwsProcessingError() {
        when(userRepository.getUsers(searchDto, pages, size)).thenReturn(List.of(user));
        when(reviewRepository.getReviews(user.getId()))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(ProcessingErrorException.class,
                () -> useCase.getUsers(searchDto, pages, size));
    }
}