package com.quack.quack_app.Application.UseCases.Users.Moderators;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveModerator;
import com.quack.quack_app.Application.Mappers.Users.ModeratorMapper;
import com.quack.quack_app.Application.Ports.Input.Users.SaveModeratorPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Application.UseCases.Services.User.SaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveModeratorUseCase implements SaveModeratorPort {

    private static final Logger log = LoggerFactory.getLogger(SaveModeratorUseCase.class);

    private final ModeratorRepository repository;
    private final ModeratorMapper mapper;
    private final TwoFAService twoFAService;
    private final EmailService emailService;

    public SaveModeratorUseCase(ModeratorRepository repository, ModeratorMapper mapper, TwoFAService twoFAService, EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.twoFAService = twoFAService;
        this.emailService = emailService;
    }

    @Override
    public String saveModerator(DTOSaveModerator moderator) {
        return SaveService.execute(
                mapper.moderatorToRegister(moderator),
                () -> repository.getModeratorByEmail(moderator.email()),
                this.twoFAService,
                repository::saveModerator,
                (u)-> emailService.sendToken(u.getTwoFA().secret(), u.getEmail(), "2 factor authentication"),
                log
        );
    }
}