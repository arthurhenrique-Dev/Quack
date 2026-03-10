package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Application.UseCases.Users.Users.*;
import com.quack.quack_app.Domain.ValueObjects.*;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("quack/user")
@RequiredArgsConstructor
public class UserController {

    private final ChangeProfilePhotoUseCase changeProfilePhotoUseCase;
    private final ChangeDescriptionUseCase changeDescriptionUseCase;
    private final ChangeUsernameUseCase changeUsernameUseCase;
    private final ChangeEmailUseCase changeEmailUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final StartChangeEmailUseCase startChangeEmailUseCase;
    private final StartChangePasswordUseCase startChangePasswordUseCase;
    private final FollowUseCase followUseCase;
    private final UnfollowUseCase unfollowUseCase;
    private final GetUserReviewsUseCase getUserReviewsUseCase;
    private final GetUserUseCase getUserUseCase;
    private final SearchUsersUseCase searchUsersUseCase;
    private final BanUseCase banUseCase;


    @PatchMapping("/customize/photo")
    @Operation(summary = "change photo", description = "requisition that update a user photo")
    @ApiResponse(responseCode = "200", description = "user saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changeProfilePhoto(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody String photo) {
        changeProfilePhotoUseCase.changeProfilePhoto(user.getId(), photo);
    }

    @PatchMapping("/customize/description")
    @Operation(summary = "change description", description = "requisition that update a user description")
    @ApiResponse(responseCode = "200", description = "user saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changeDescription(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody Description description) {
        changeDescriptionUseCase.changeDescription(user.getId(), description);
    }

    @PatchMapping("/customize/username")
    @Operation(summary = "change username", description = "requisition that update a user username")
    @ApiResponse(responseCode = "200", description = "user saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changeUsername(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody Username username) {
        changeUsernameUseCase.changeUsername(user.getId(), username);
    }

    @PatchMapping("/customize/email/confirm/{token}")
    @Operation(summary = "change email", description = "requisition that validate a token and then update a user email")
    @ApiResponse(responseCode = "200", description = "user saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changeEmailConfirm(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID token, @RequestBody Email email) {
        changeEmailUseCase.changeEmail(user.getId(), token, email);
    }

    @PatchMapping("/customize/password/confirm/{token}")
    @Operation(summary = "change password", description = "requisition that validate a token and then update a user password")
    @ApiResponse(responseCode = "200", description = "user saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changePasswordConfirm(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID token, @RequestBody Password password) {
        changePasswordUseCase.changePassword(user.getId(), token, password);
    }

    @PatchMapping("/customize/email/send")
    @Operation(summary = "send token for email change", description = "requisition that send a token to use in email change")
    @ApiResponse(responseCode = "200", description = "token sended with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changeEmailSend(@AuthenticationPrincipal UserDetailsImpl user) {
        startChangeEmailUseCase.startChangeEmail(user.getId());
    }

    @PatchMapping("/customize/password/send")
    @Operation(summary = "send token for password change", description = "requisition that send a token to use in password change")
    @ApiResponse(responseCode = "200", description = "token sended with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void changePasswordSend(@AuthenticationPrincipal UserDetailsImpl user) {
        startChangePasswordUseCase.startChangePassword(user.getId());
    }

    @DeleteMapping("/customize/delete")
    @Operation(summary = "delete user", description = "requisition that delete a user")
    @ApiResponse(responseCode = "200", description = "user deleted with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void deleteAccount(@AuthenticationPrincipal UserDetailsImpl user) {
        banUseCase.ban(user.getId());
    }


    @PostMapping("/follow/{idFollowing}")
    @Operation(summary = "follow user", description = "requisition that makes a user follow another")
    @ApiResponse(responseCode = "200", description = "user followed with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void follow(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID idFollowing) {
        followUseCase.follow(user.getId(), idFollowing);
    }

    @DeleteMapping("/unfollow/{idUnfollowing}")
    @Operation(summary = "unfollow user", description = "requisition that makes a user unfollow another")
    @ApiResponse(responseCode = "200", description = "user unfollowed with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void unfollow(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID idUnfollowing) {
        unfollowUseCase.unfollow(user.getId(), idUnfollowing);
    }

    @GetMapping("/{id}")
    @Operation(summary = "return user", description = "requisition that return user")
    @ApiResponse(responseCode = "200", description = "user returned with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public DTOGetUser getUser(@PathVariable UUID id) {
        return getUserUseCase.getUser(id);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "return user reviews", description = "requisition that return a user reviews")
    @ApiResponse(responseCode = "200", description = "user reviews returned with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public List<DTOReturnReview> getUserReviews(@PathVariable UUID id, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "30") Integer size) {
        return getUserReviewsUseCase.getReviewsByUser(id, new Natural(page), new Natural(size));
    }

    @GetMapping()
    @Operation(summary = "return users", description = "requisition that return users based in a filter")
    @ApiResponse(responseCode = "200", description = "users returned with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public List<DTOGetUser> getUsers(@RequestBody DTOSearchUser dtoSearchUser, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        return searchUsersUseCase.getUsers(dtoSearchUser, new Natural(page), new Natural(size));
    }
}