package com.quack.quack_app.Domain.Entities;


import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Users.Role;
import com.quack.quack_app.Domain.Users.Status;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.*;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DomainEntitiesTest {

    // ─── Status ───────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Status")
    class StatusTest {

        @Test
        @DisplayName("Deve banir usuário ACTIVE retornando INACTIVE")
        void ban_fromActive_returnsInactive() {
            assertEquals(Status.INACTIVE, Status.ACTIVE.ban());
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException ao banir usuário INACTIVE")
        void ban_fromInactive_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> Status.INACTIVE.ban());
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException ao banir usuário PENDING")
        void ban_fromPending_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> Status.PENDING.ban());
        }

        @Test
        @DisplayName("Deve ativar usuário PENDING retornando ACTIVE")
        void activate_fromPending_returnsActive() {
            assertEquals(Status.ACTIVE, Status.PENDING.activate());
        }

        @Test
        @DisplayName("Deve ativar usuário INACTIVE retornando ACTIVE")
        void activate_fromInactive_returnsActive() {
            assertEquals(Status.ACTIVE, Status.INACTIVE.activate());
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException ao ativar usuário já ACTIVE")
        void activate_fromActive_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> Status.ACTIVE.activate());
        }
    }

    // ─── BaseUser (via User) ──────────────────────────────────────────────────

    @Nested
    @DisplayName("BaseUser")
    class BaseUserTest {

        private User user;

        @BeforeEach
        void setUp() {
            user = TestModels.activeUser();
        }

        @Test
        @DisplayName("Deve preparar secret do 2FA corretamente")
        void prepareTwoFA_setsSecret() {
            User pending = TestModels.pendingUser();
            pending.prepareTwoFA("NEWSECRET");
            assertEquals("NEWSECRET", pending.getTwoFA().secret());
            assertFalse(pending.getTwoFA().enabled());
        }

        @Test
        @DisplayName("Deve habilitar 2FA quando secret estiver configurado")
        void enableTwoFA_withSecret_success() {
            User pending = TestModels.pendingUser();
            pending.prepareTwoFA("NEWSECRET");
            assertDoesNotThrow(pending::enableTwoFA);
            assertTrue(pending.getTwoFA().enabled());
        }

        @Test
        @DisplayName("Deve lançar ValidationFailedException ao habilitar 2FA sem secret")
        void enableTwoFA_withoutSecret_throwsValidationFailed() {
            User pending = TestModels.pendingUser(); // TwoFA.disabled() → secret null
            assertThrows(ValidationFailedException.class, pending::enableTwoFA);
        }

        @Test
        @DisplayName("Deve gerar token de atualização de senha quando 2FA estiver habilitado")
        void sendUpdatePasswordToken_twoFAEnabled_success() {
            assertDoesNotThrow(user::sendUpdatePasswordToken);
            assertNotNull(user.getPasswordUpdater());
        }

        @Test
        @DisplayName("Deve lançar ProcessingErrorException ao gerar token de senha sem 2FA")
        void sendUpdatePasswordToken_noTwoFA_throwsProcessingError() {
            User pending = TestModels.pendingUser();
            assertThrows(ProcessingErrorException.class, pending::sendUpdatePasswordToken);
        }

        @Test
        @DisplayName("Deve alterar senha com token válido")
        void checkAndChangePassword_validToken_success() {
            user.sendUpdatePasswordToken();
            UUID token = UUID.fromString(user.getPasswordUpdater().token());
            Password newPassword = new Password("NewPass@456");
            assertDoesNotThrow(() -> user.checkAndChangePassword(token, newPassword));
            assertEquals(newPassword, user.getPassword());
        }

        @Test
        @DisplayName("Deve lançar ValidationFailedException ao alterar senha com token inválido")
        void checkAndChangePassword_invalidToken_throwsValidationFailed() {
            user.sendUpdatePasswordToken();
            assertThrows(ValidationFailedException.class,
                    () -> user.checkAndChangePassword(UUID.randomUUID(), new Password("NewPass@456")));
        }

        @Test
        @DisplayName("Deve banir usuário ACTIVE")
        void ban_activeUser_success() {
            user.ban();
            assertEquals(Status.INACTIVE, user.getStatus());
        }

        @Test
        @DisplayName("Deve ativar usuário PENDING")
        void activate_pendingUser_success() {
            User pending = TestModels.pendingUser();
            pending.activate();
            assertEquals(Status.ACTIVE, pending.getStatus());
        }
    }

    // ─── User ─────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("User")
    class UserTest {

        private User user;

        @BeforeEach
        void setUp() {
            user = TestModels.activeUser();
        }

        @Test
        @DisplayName("Deve alterar username com sucesso")
        void changeUsername_success() {
            Username novo = new Username("novoPato");
            user.changeUsername(novo);
            assertEquals(novo, user.getUsername());
        }

        @Test
        @DisplayName("Deve alterar description com sucesso")
        void changeDescription_success() {
            Description nova = new Description("Nova bio de pato.");
            user.changeDescription(nova);
            assertEquals(nova, user.getDescription());
        }

        @Test
        @DisplayName("Deve alterar foto com sucesso")
        void changePhoto_success() {
            user.changePhoto("https://cdn.quack.com/newphoto.png");
            assertEquals("https://cdn.quack.com/newphoto.png", user.getPhotoUrl());
        }

        @Test
        @DisplayName("Deve seguir usuário e adicionar aos followers")
        void follow_addsToFollowers() {
            UUID targetId = UUID.randomUUID();
            user.follow(targetId);
            assertTrue(user.getFollowers().connections().contains(targetId));
        }

        @Test
        @DisplayName("Deve adicionar aos amigos quando seguir alguém que já te segue")
        void follow_mutualFollow_addsToFriends() {
            UUID targetId = UUID.randomUUID();
            user.getFollowers().addConnection(targetId);
            user.follow(targetId);
            assertTrue(user.getFriends().connections().contains(targetId));
        }

        @Test
        @DisplayName("Deve deixar de seguir e remover dos followers")
        void unfollow_removesFromFollowers() {
            UUID targetId = UUID.randomUUID();
            user.follow(targetId);
            user.unfollow(targetId);
            assertFalse(user.getFollowers().connections().contains(targetId));
        }

        @Test
        @DisplayName("Deve remover dos amigos ao deixar de seguir usuário mútuo")
        void unfollow_mutualFollow_removesFromFriends() {
            UUID targetId = UUID.randomUUID();
            user.getFollowers().addConnection(targetId);
            user.follow(targetId);
            user.unfollow(targetId);
            assertFalse(user.getFriends().connections().contains(targetId));
        }

        @Test
        @DisplayName("Deve criar User via construtor simples com valores padrão")
        void user_simpleConstructor_defaultValues() {
            User novo = TestModels.pendingUser();
            assertNotNull(novo.getId());
            assertEquals(Role.USER, novo.getRole());
            assertEquals(Status.PENDING, novo.getStatus());
            assertTrue(novo.getFavoriteGames().gameIds().isEmpty());
            assertTrue(novo.getFriends().connections().isEmpty());
            assertTrue(novo.getFollowers().connections().isEmpty());
        }
    }
}