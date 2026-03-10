package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveModerator;
import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;

import com.quack.quack_app.Application.UseCases.Users.Moderators.SaveModeratorUseCase;
import com.quack.quack_app.Application.UseCases.Users.Users.Check2FAUseCase;
import com.quack.quack_app.Application.UseCases.Users.Users.SaveUserUseCase;
import com.quack.quack_app.Infra.Security.Service.LoginRequest;
import com.quack.quack_app.Infra.Security.Service.TokenService;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("quack/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SaveUserUseCase saveUserUseCase;
    private final SaveModeratorUseCase saveModeratorUseCase;
    private final Check2FAUseCase check2FAUseCase;
    private final com.quack.quack_app.Application.UseCases.Users.Moderators.Check2FAUseCase moderatorCheck2FAUseCase;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "login", description = "requisition that makes login for users and moderators in Quack system")
    @ApiResponse(responseCode = "201", description = "user created with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public ResponseEntity logIn(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            String emailPuro = loginRequest.email().email();
            String senhaPura = loginRequest.password().password();

            System.out.println("Tentando login para: " + emailPuro);

            var authCredentials = new UsernamePasswordAuthenticationToken(
                    emailPuro,
                    senhaPura
            );

            var authentication = authenticationManager.authenticate(authCredentials);

            var user = (UserDetailsImpl) authentication.getPrincipal();
            String token = tokenService.generateToken(user.getBaseUser());

            return ResponseEntity.ok(token);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credencials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/signUp")
    @Operation(summary = "register users", description = "requisition that makes a new user in Quack system")
    @ApiResponse(responseCode = "201", description = "user created with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public ResponseEntity registerUser(@RequestBody DTOSaveUser dtoSaveUser){
        String qrCodeUrl = saveUserUseCase.saveUser(dtoSaveUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(qrCodeUrl);
    }
    @PostMapping("/moderator/signUp")
    @Operation(summary = "register moderator", description = "requisition that makes a new moderator in Quack system")
    @ApiResponse(responseCode = "201", description = "moderator created with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public ResponseEntity registerModerator(@RequestBody DTOSaveModerator dtoSaveModerator){
        String qrCodeUrl = saveModeratorUseCase.saveModerator(dtoSaveModerator);
        return ResponseEntity.status(HttpStatus.CREATED).body(qrCodeUrl);
    }
    @PatchMapping("/check")
    @Operation(summary = "2FA check users", description = "requisition that makes a 2FA validation for a user in Quack system")
    @ApiResponse(responseCode = "200", description = "user validated with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public ResponseEntity twoFACheck(UUID id, String token){
        check2FAUseCase.check2FA(id, token);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/management/check")
    @Operation(summary = "2FA check moderators", description = "requisition that makes a 2FA validation for a moderator in Quack system")
    @ApiResponse(responseCode = "200", description = "moderator validated with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "moderator not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public ResponseEntity check2FA(UUID id, String token){
        moderatorCheck2FAUseCase.check2FA(id, token);
        return ResponseEntity.ok().build();
    }
}
