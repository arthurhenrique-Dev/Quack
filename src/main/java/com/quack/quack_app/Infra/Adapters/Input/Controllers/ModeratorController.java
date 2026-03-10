package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.UseCases.Reviews.DeleteReviewUseCase;
import com.quack.quack_app.Application.UseCases.Users.Moderators.*;
import com.quack.quack_app.Domain.ValueObjects.*;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final com.quack.quack_app.Application.UseCases.Users.Users.ActivateUseCase activateUseCaseUsers;
    private final com.quack.quack_app.Application.UseCases.Users.Users.BanUseCase banUseCaseUsers;
    private final DeleteReviewUseCase deleteReviewUseCase;

    @PatchMapping("/moderators/{id}/activate")
    @Operation(summary = "activate moderator", description = "requisition that makes a moderator active again")
    @ApiResponse(responseCode = "200", description = "moderator activated with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void activateModerator(@PathVariable UUID id) {
        activateUseCase.activate(id);
    }

    @DeleteMapping("/moderators/{id}/ban")
    @Operation(summary = "ban moderator", description = "requisition that ban a moderator")
    @ApiResponse(responseCode = "200", description = "moderator banned with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void banModerator(@PathVariable UUID id) {
        banUseCase.ban(id);
    }

    @PatchMapping("/users/{id}/activate")
    @Operation(summary = "activate user", description = "requisition that makes a moderator active a user again")
    @ApiResponse(responseCode = "200", description = "user activated with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void activateUser(@PathVariable UUID id) {
        activateUseCaseUsers.activate(id);
    }

    @DeleteMapping("/users/{id}/ban")
    @Operation(summary = "ban user", description = "requisition that makes a moderator ban a user")
    @ApiResponse(responseCode = "200", description = "user banned with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void banUser(@PathVariable UUID id) {
        banUseCaseUsers.ban(id);
    }

    @DeleteMapping("moderators/review/{idReview}/userid/{idUser}")
    @Operation(summary = "delete review", description = "requisition that makes a moderator delete a review")
    @ApiResponse(responseCode = "200", description = "review deleted with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void deleteReview(@PathVariable UUID idReview, @PathVariable UUID idUser) {
        deleteReviewUseCase.DeleteReview(idReview, idUser);
    }

    @PatchMapping("/customize/email/send")
    @Operation(summary = "send token for email change", description = "requisition that send a token to use in email change")
    @ApiResponse(responseCode = "200", description = "token sended with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void startChangeEmail(@AuthenticationPrincipal UserDetailsImpl moderator) {
        startChangeEmailUseCase.startChangeEmail(moderator.getId());
    }

    @PatchMapping("/customize/email/confirm/{token}")
    @Operation(summary = "change email", description = "requisition that validate a token and then update a moderator email")
    @ApiResponse(responseCode = "200", description = "moderator saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changeEmailConfirm(
            @AuthenticationPrincipal UserDetailsImpl moderator,
            @PathVariable UUID token,
            @RequestBody Email email) {
        changeEmailUseCase.changeEmail(moderator.getId(), token, email);
    }

    @PatchMapping("/customize/password/send")
    @Operation(summary = "send token for password change", description = "requisition that send a token to use in password change")
    @ApiResponse(responseCode = "200", description = "token sended with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void startChangePassword(@AuthenticationPrincipal UserDetailsImpl moderator) {
        startChangePasswordUseCase.startChangePassword(moderator.getId());
    }

    @PatchMapping("/customize/password/confirm/{token}")
    @Operation(summary = "change password", description = "requisition that validate a token and then update a moderator password")
    @ApiResponse(responseCode = "200", description = "moderator saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changePasswordConfirm(
            @AuthenticationPrincipal UserDetailsImpl moderator,
            @PathVariable UUID token,
            @RequestBody Password password) {
        changePasswordUseCase.changePassword(moderator.getId(), token, password);
    }
}