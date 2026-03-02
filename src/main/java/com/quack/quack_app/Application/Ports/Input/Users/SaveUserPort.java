package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;

public interface SaveUserPort {

    String saveUser(DTOSaveUser dto);
}
