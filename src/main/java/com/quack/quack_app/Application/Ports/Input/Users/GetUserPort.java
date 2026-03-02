package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;

import java.util.UUID;

public interface GetUserPort {

    DTOGetUser getUser(UUID idUser);
}
