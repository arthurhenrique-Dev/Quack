package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Input.Users.SaveUserPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Application.UseCases.Services.User.SaveService;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveUserUseCase implements SaveUserPort {

    private static final Logger log = LoggerFactory.getLogger(SaveUserUseCase.class);

    private final UserRepository repository;
    private final UserMapper mapper;
    private final TwoFAService twoFAService;
    private final EmailService emailService;

    public SaveUserUseCase(UserRepository repository, UserMapper mapper, TwoFAService twoFAService, EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.twoFAService = twoFAService;
        this.emailService = emailService;
    }

    @Override
    public String saveUser(DTOSaveUser dto) {
        var user = repository.getUserByName(dto.username());
        if (user.isPresent()) {
            var e = new InvalidDataException("User with this username already exists");
            log.warn(e.getMessage(), e);
            throw e;
        }
        return SaveService.execute(
                mapper.userToRegister(dto),
                () -> repository.getUserByEmail(dto.email()),
                this.twoFAService,
                repository::saveUser,
                (u)-> emailService.sendToken(u.getTwoFA().secret(), u.getEmail(), "2 factor authentication"),
                log
        );
    }
}