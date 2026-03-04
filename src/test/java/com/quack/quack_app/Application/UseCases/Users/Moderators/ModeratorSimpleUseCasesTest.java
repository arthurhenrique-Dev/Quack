package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Password;
import com.quack.quack_app.Domain.ValueObjects.TwoFA;
import com.quack.quack_app.Domain.ValueObjects.TokenUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeratorSimpleUseCasesTest {

    @Mock private ModeratorRepository moderatorRepository;
    @Mock private EmailService emailService;
    @Mock private TwoFAService twoFAService;

    @Mock private Moderator moderatorMock;
    @Mock private TwoFA twoFAMock;
    @Mock private TokenUpdater tokenUpdaterMock;

    private UUID moderatorId;
    private String tokenStr;

    @BeforeEach
    void setUp() {
        moderatorId = UUID.randomUUID();
        tokenStr = UUID.randomUUID().toString();

        lenient().when(moderatorRepository.getModeratorById(moderatorId)).thenReturn(Optional.of(moderatorMock));

        lenient().when(moderatorMock.getId()).thenReturn(moderatorId);
        lenient().when(moderatorMock.getTwoFA()).thenReturn(twoFAMock);

        lenient().when(twoFAMock.secret()).thenReturn(tokenStr);
        lenient().when(moderatorMock.getEmail()).thenReturn(new Email("test@quack.com"));

        lenient().when(tokenUpdaterMock.token()).thenReturn(tokenStr);
    }

    @Test
    @DisplayName("Deve ativar moderador")
    void testActivate() {
        new ActivateUseCase(moderatorRepository).activate(moderatorId);
        verify(moderatorMock).activate();
        verify(moderatorRepository).saveModerator(moderatorMock);
    }

    @Test
    @DisplayName("Deve banir moderador")
    void testBan() {
        new BanUseCase(moderatorRepository).ban(moderatorId);
        verify(moderatorMock).ban();
        verify(moderatorRepository).saveModerator(moderatorMock);
    }

    @Test
    @DisplayName("Deve alterar email do moderador")
    void testChangeEmail() {
        Email novoEmail = new Email("novo@test.com");
        new ChangeEmailUseCase(moderatorRepository).changeEmail(moderatorId, UUID.fromString(tokenStr), novoEmail);
        verify(moderatorMock).checkAndChangeEmail(eq(UUID.fromString(tokenStr)), eq(novoEmail));
        verify(moderatorRepository).saveModerator(moderatorMock);
    }

    @Test
    @DisplayName("Deve alterar senha do moderador")
    void testChangePassword() {
        Password novaSenha = new Password("NovaSenha123!");
        new ChangePasswordUseCase(moderatorRepository).changePassword(moderatorId, UUID.fromString(tokenStr), novaSenha);
        verify(moderatorMock).checkAndChangePassword(eq(UUID.fromString(tokenStr)), eq(novaSenha));
        verify(moderatorRepository).saveModerator(moderatorMock);
    }

    @Test
    @DisplayName("Deve iniciar troca de email do moderador")
    void testStartChangeEmail() {
        lenient().when(moderatorMock.getEmailUpdater()).thenReturn(tokenUpdaterMock);

        new StartChangeEmailUseCase(moderatorRepository, emailService).startChangeEmail(moderatorId);

        verify(moderatorMock).sendUpdateEmailToken();
        verify(emailService).send(eq(tokenStr), any(), anyString());
        verify(moderatorRepository).saveModerator(moderatorMock);
    }

    @Test
    @DisplayName("Deve iniciar troca de senha do moderador")
    void testStartChangePassword() {
        lenient().when(moderatorMock.getPasswordUpdater()).thenReturn(tokenUpdaterMock);

        new StartChangePasswordUseCase(moderatorRepository, emailService).startChangePassword(moderatorId);

        verify(moderatorMock).sendUpdatePasswordToken();
        verify(emailService).send(eq(tokenStr), any(), anyString());
        verify(moderatorRepository).saveModerator(moderatorMock);
    }

    @Test
    @DisplayName("Deve verificar 2FA do moderador")
    void testCheck2FA() {
        String code = "123456";
        when(twoFAService.checkCode(anyString(), eq(code))).thenReturn(true);

        new Check2FAUseCase(moderatorRepository, twoFAService).check2FA(moderatorId, code);

        verify(twoFAService).checkCode(anyString(), eq(code));
    }
}