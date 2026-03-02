package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.UseCases.Users.Moderators.*;
import com.quack.quack_app.Domain.ValueObjects.*;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("quack/management")
@RequiredArgsConstructor
public class ModeratorController {

    private final ActivateUseCase activateUseCase;
    private final BanUseCase banUseCase;
    private final ChangeEmailUseCase changeEmailUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final StartChangeEmailUseCase startChangeEmailUseCase;
    private final StartChangePasswordUseCase startChangePasswordUseCase;

    @PostMapping("/users/{id}/activate")
    public void activateUser(@PathVariable UUID id) {
        activateUseCase.activate(id);
    }

    @DeleteMapping("/users/{id}/ban")
    public void banUser(@PathVariable UUID id) {
        banUseCase.ban(id);
    }


    @PutMapping("/customize/email/send")
    public void startChangeEmail(@AuthenticationPrincipal UserDetailsImpl moderator) {
        startChangeEmailUseCase.startChangeEmail(moderator.getId());
    }

    @PutMapping("/customize/email/confirm/{token}")
    public void changeEmailConfirm(
            @AuthenticationPrincipal UserDetailsImpl moderator,
            @PathVariable UUID token,
            @RequestBody Email email) {
        changeEmailUseCase.changeEmail(moderator.getId(), token, email);
    }

    @PutMapping("/customize/password/send")
    public void startChangePassword(@AuthenticationPrincipal UserDetailsImpl moderator) {
        startChangePasswordUseCase.startChangePassword(moderator.getId());
    }

    @PutMapping("/customize/password/confirm/{token}")
    public void changePasswordConfirm(
            @AuthenticationPrincipal UserDetailsImpl moderator,
            @PathVariable UUID token,
            @RequestBody Password password) {
        changePasswordUseCase.changePassword(moderator.getId(), token, password);
    }
}