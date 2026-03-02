package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.Check2FAPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Application.UseCases.Services.User.Check2FAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Check2FAUseCase implements Check2FAPort {

    private static final Logger log = LoggerFactory.getLogger(Check2FAUseCase.class);

    private final UserRepository repository;
    private final TwoFAService service;

    public Check2FAUseCase(UserRepository repository, TwoFAService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void check2FA(UUID idUser, String code) {
        Check2FAService.execute(
                ()-> repository.getUserById(idUser),
                code,
                service,
                repository::saveUser,
                log
        );
    }
}
