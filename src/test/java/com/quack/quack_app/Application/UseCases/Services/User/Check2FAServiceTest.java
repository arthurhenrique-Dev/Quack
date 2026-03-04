package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Check2FAServiceTest {

    @Mock UserRepository userRepository;
    @Mock TwoFAService twoFAService;
    @Mock Logger log;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestModels.activeUser();
    }

    @Test
    @DisplayName("Deve ativar 2FA e usuário com código válido")
    void execute_validCode_activatesUser() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(user));
        when(twoFAService.checkCode(user.getTwoFA().secret(), "123456")).thenReturn(true);

        User pendingUser = TestModels.pendingUser();
        pendingUser.prepareTwoFA("JBSWY3DPEHPK3PXP");

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(pendingUser));
        when(twoFAService.checkCode("JBSWY3DPEHPK3PXP", "123456")).thenReturn(true);

        assertDoesNotThrow(() ->
                Check2FAService.execute(
                        () -> userRepository.getUserById(TestModels.USER_ID),
                        "123456",
                        twoFAService,
                        userRepository::saveUser,
                        log
                )
        );

        verify(userRepository).saveUser(pendingUser);
    }

    @Test
    @DisplayName("Deve lançar ValidationFailedException para código inválido")
    void execute_invalidCode_throwsValidationFailed() {
        User pendingUser = TestModels.pendingUser();
        pendingUser.prepareTwoFA("JBSWY3DPEHPK3PXP");

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(pendingUser));
        when(twoFAService.checkCode("JBSWY3DPEHPK3PXP", "000000")).thenReturn(false);

        assertThrows(ValidationFailedException.class, () ->
                Check2FAService.execute(
                        () -> userRepository.getUserById(TestModels.USER_ID),
                        "000000",
                        twoFAService,
                        userRepository::saveUser,
                        log
                )
        );

        verify(userRepository, never()).saveUser(any());
    }

    @Test
    @DisplayName("Deve lançar ProcessingErrorException quando serviço de 2FA lançar exceção")
    void execute_twoFAServiceThrows_throwsProcessingError() {
        User pendingUser = TestModels.pendingUser();
        pendingUser.prepareTwoFA("JBSWY3DPEHPK3PXP");

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(pendingUser));
        when(twoFAService.checkCode(any(), any())).thenThrow(new RuntimeException("service down"));

        assertThrows(ProcessingErrorException.class, () ->
                Check2FAService.execute(
                        () -> userRepository.getUserById(TestModels.USER_ID),
                        "123456",
                        twoFAService,
                        userRepository::saveUser,
                        log
                )
        );
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando usuário não for encontrado")
    void execute_userNotFound_throwsUserNotFoundException() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                Check2FAService.execute(
                        () -> userRepository.getUserById(TestModels.USER_ID),
                        "123456",
                        twoFAService,
                        userRepository::saveUser,
                        log
                )
        );
    }
}