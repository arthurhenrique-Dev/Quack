package com.quack.quack_app.Application.UseCases.Services.User;

import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

    @Mock UserRepository userRepository;
    @Mock Logger log;

    private User actor;
    private User target;

    @BeforeEach
    void setUp() {
        actor  = TestModels.activeUser();
        target = new User(
                TestModels.TARGET_ID,
                TestModels.password(),
                new com.quack.quack_app.Domain.ValueObjects.Email("target@quack.com"),
                com.quack.quack_app.Domain.Users.Role.USER,
                com.quack.quack_app.Domain.Users.Status.ACTIVE,
                null, null,
                new com.quack.quack_app.Domain.ValueObjects.TwoFA(true, "TARGETSECRET"),
                new com.quack.quack_app.Domain.ValueObjects.Username("targetUser"),
                "https://cdn.quack.com/target.png",
                new com.quack.quack_app.Domain.ValueObjects.Description(""),
                new com.quack.quack_app.Domain.ValueObjects.FavoriteGames(new java.util.ArrayList<>()),
                new com.quack.quack_app.Domain.ValueObjects.Connections(new java.util.ArrayList<>()),
                new com.quack.quack_app.Domain.ValueObjects.Connections(new java.util.ArrayList<>())
        );
    }

    @Test
    @DisplayName("Deve executar ação sobre o target e salvar com sucesso")
    void execute_success() {
        AtomicBoolean actionCalled = new AtomicBoolean(false);

        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(actor));
        when(userRepository.getUserById(TestModels.TARGET_ID)).thenReturn(Optional.of(target));

        assertDoesNotThrow(() ->
                ConnectionService.execute(
                        TestModels.USER_ID,
                        TestModels.TARGET_ID,
                        userRepository,
                        u -> actionCalled.set(true),
                        log
                )
        );

        assertTrue(actionCalled.get());
        verify(userRepository).saveUser(target);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando actor não existir")
    void execute_actorNotFound_throwsUserNotFoundException() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                ConnectionService.execute(
                        TestModels.USER_ID,
                        TestModels.TARGET_ID,
                        userRepository,
                        u -> {},
                        log
                )
        );

        verify(userRepository, never()).saveUser(any());
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando target não existir")
    void execute_targetNotFound_throwsUserNotFoundException() {
        when(userRepository.getUserById(TestModels.USER_ID)).thenReturn(Optional.of(actor));
        when(userRepository.getUserById(TestModels.TARGET_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                ConnectionService.execute(
                        TestModels.USER_ID,
                        TestModels.TARGET_ID,
                        userRepository,
                        u -> {},
                        log
                )
        );

        verify(userRepository, never()).saveUser(any());
    }
}