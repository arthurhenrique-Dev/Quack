package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.Ports.Input.Users.Check2FAPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Application.UseCases.Services.User.Check2FAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Check2FAUseCase implements Check2FAPort {

    private static final Logger log = LoggerFactory.getLogger(Check2FAUseCase.class);

    private final ModeratorRepository repository;
    private final TwoFAService service;

    public Check2FAUseCase(ModeratorRepository repository, TwoFAService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void check2FA(UUID idUser, String code) {
        Check2FAService.execute(
                ()-> repository.getModeratorById(idUser),
                code,
                service,
                repository::saveModerator,
                log
        );
    }
}
