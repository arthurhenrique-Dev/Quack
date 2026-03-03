package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveModerator;
import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Users.Moderators.SaveModeratorUseCase;
import com.quack.quack_app.Application.UseCases.Users.Users.Check2FAUseCase;
import com.quack.quack_app.Application.UseCases.Users.Users.SaveUserUseCase;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Password;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.UserEntity;
import com.quack.quack_app.Infra.Security.Service.AesEncryptor;
import com.quack.quack_app.Infra.Security.Service.LoginRequest;
import com.quack.quack_app.Infra.Security.Service.TokenService;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("quack/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    UserRepository userRepository;

    private final SaveUserUseCase saveUserUseCase;
    private final SaveModeratorUseCase saveModeratorUseCase;
    private final Check2FAUseCase check2FAUseCase;
    private final com.quack.quack_app.Application.UseCases.Users.Moderators.Check2FAUseCase moderatorCheck2FAUseCase;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
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
            System.out.println("Senha incorreta para o usuário: " + loginRequest.email().email());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/signUp")
    public ResponseEntity registerUser(@RequestBody DTOSaveUser dtoSaveUser){
        String qrCodeUrl= saveUserUseCase.saveUser(dtoSaveUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(qrCodeUrl);
    }
    @PostMapping("/moderator/signUp")
    public ResponseEntity registerModerator(@RequestBody DTOSaveModerator dtoSaveModerator){
        saveModeratorUseCase.saveModerator(dtoSaveModerator);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/check")
    public ResponseEntity twoFACheck(@RequestBody UUID id, String token){
        check2FAUseCase.check2FA(id, token);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/management/check")
    public ResponseEntity check2FA(UUID id, String token){
        moderatorCheck2FAUseCase.check2FA(id, token);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/test")
    public User testGetUser(Email email){
        return userRepository.getUserByEmail(email).get();
    }
}
