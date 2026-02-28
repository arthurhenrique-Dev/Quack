package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Input.Users.Users.SaveUserPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.TrySaveService;
import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveUserUseCase implements SaveUserPort {

    private static final Logger log = LoggerFactory.getLogger(SaveUserUseCase.class);

    private final UserRepository repository;
    private final UserMapper mapper;

    public SaveUserUseCase(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveUser(DTOSaveUser dto) {
        var userOpt = repository.getUserByName(dto.username());
        if (userOpt.isPresent()) {
            var e = new InvalidDataException("User with name " + dto.username() + " already exists");
            log.warn(e.getMessage(), e);
            throw e;
        }
        User u = mapper.userToRegister(dto);
        TrySaveService.execute(u, repository::saveUser, log);
    }
}
