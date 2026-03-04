package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveUserUseCaseTest {

    @Mock UserRepository userRepository;
    @Mock UserMapper mapper;
    @Mock TwoFAService twoFAService;

    @InjectMocks SaveUserUseCase useCase;

    private DTOSaveUser dto;
    private User user;

    @BeforeEach
    void setUp() {
        dto  = TestModels.dtoSaveUser();
        user = TestModels.pendingUser();
    }

    @Test
    @DisplayName("Deve registrar novo usuário e retornar URL do QR Code")
    void saveUser_success_returnsQrCodeUrl() {
        when(userRepository.getUserByName(dto.username())).thenReturn(Optional.empty());
        when(userRepository.getUserByEmail(dto.email())).thenReturn(Optional.empty());
        when(mapper.userToRegister(dto)).thenReturn(user);
        when(twoFAService.setupTwoFa()).thenReturn("SECRETBASE32");
        when(twoFAService.getQrCodeUrl(any(), any())).thenReturn("otpauth://totp/quack?secret=SECRETBASE32");

        String result = useCase.saveUser(dto);

        assertNotNull(result);
        assertTrue(result.startsWith("otpauth://"));
        verify(userRepository).saveUser(user);
    }

    @Test
    @DisplayName("Deve lançar InvalidDataException quando username já existir — falha antes de chegar ao SaveService")
    void saveUser_duplicateUsername_throwsInvalidData() {
        when(userRepository.getUserByName(dto.username())).thenReturn(Optional.of(user));

        assertThrows(InvalidDataException.class, () -> useCase.saveUser(dto));

        verifyNoInteractions(mapper, twoFAService);
        verify(userRepository, never()).saveUser(any());
    }

    @Test
    @DisplayName("Deve lançar ValidationFailedException quando email já existir — detectado pelo SaveService")
    void saveUser_duplicateEmail_throwsValidationFailed() {
        when(userRepository.getUserByName(dto.username())).thenReturn(Optional.empty());
        when(mapper.userToRegister(dto)).thenReturn(user);
        when(userRepository.getUserByEmail(dto.email())).thenReturn(Optional.of(user));

        assertThrows(ValidationFailedException.class, () -> useCase.saveUser(dto));

        verifyNoInteractions(twoFAService);
        verify(userRepository, never()).saveUser(any());
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando serviço de 2FA falhar")
    void saveUser_twoFAServiceFails_throwsProcessingError() {
        when(userRepository.getUserByName(dto.username())).thenReturn(Optional.empty());
        when(userRepository.getUserByEmail(dto.email())).thenReturn(Optional.empty());
        when(mapper.userToRegister(dto)).thenReturn(user);
        when(twoFAService.setupTwoFa()).thenThrow(new RuntimeException("External service down"));

        assertThrows(ProcessingErrorException.class, () -> useCase.saveUser(dto));

        verify(userRepository, never()).saveUser(any());
    }
}