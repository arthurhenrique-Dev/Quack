package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Application.UseCases.Users.Users.*;
import com.quack.quack_app.Domain.ValueObjects.*;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
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


    // ROTAS DE CUSTOMIZAÇÃO: Não precisam de ID na URL. O ID vem do TOKEN.

    @PutMapping("/customize/photo")
    public void changeProfilePhoto(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody String photo) {
        changeProfilePhotoUseCase.changeProfilePhoto(user.getId(), photo);
    }

    @PutMapping("/customize/description")
    public void changeDescription(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody Description description) {
        changeDescriptionUseCase.changeDescription(user.getId(), description);
    }

    @PutMapping("/customize/username")
    public void changeUsername(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody Username username) {
        changeUsernameUseCase.changeUsername(user.getId(), username);
    }

    @PutMapping("/customize/email/confirm/{token}")
    public void changeEmailConfirm(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID token, @RequestBody Email email) {
        changeEmailUseCase.changeEmail(user.getId(), token, email);
    }

    @PutMapping("/customize/password/confirm/{token}")
    public void changePasswordConfirm(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID token, @RequestBody Password password) {
        changePasswordUseCase.changePassword(user.getId(), token, password);
    }

    @PutMapping("/customize/email/send")
    public void changeEmailSend(@AuthenticationPrincipal UserDetailsImpl user) {
        startChangeEmailUseCase.startChangeEmail(user.getId());
    }

    @PutMapping("/customize/password/send")
    public void changePasswordSend(@AuthenticationPrincipal UserDetailsImpl user) {
        startChangePasswordUseCase.startChangePassword(user.getId());
    }

    @DeleteMapping("/customize/delete")
    public void deleteAccount(@AuthenticationPrincipal UserDetailsImpl user) {
        banUseCase.ban(user.getId());
    }


    @PostMapping("/follow/{idFollowing}")
    public void follow(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID idFollowing) {
        followUseCase.follow(user.getId(), idFollowing);
    }

    @DeleteMapping("/unfollow/{idUnfollowing}")
    public void unfollow(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID idUnfollowing) {
        unfollowUseCase.unfollow(user.getId(), idUnfollowing);
    }

    @GetMapping("/{id}")
    public DTOGetUser getUser(@PathVariable UUID id) {
        return getUserUseCase.getUser(id);
    }

    @GetMapping("/{id}/reviews")
    public List<DTOReturnReview> getUserReviews(@PathVariable UUID id, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "30") Integer size) {
        return getUserReviewsUseCase.getReviewsByUser(id, new Natural(page), new Natural(size));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public List<DTOGetUser> getUsers(@RequestBody DTOSearchUser dtoSearchUser, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        return searchUsersUseCase.getUsers(dtoSearchUser, new Natural(page), new Natural(size));
    }
}