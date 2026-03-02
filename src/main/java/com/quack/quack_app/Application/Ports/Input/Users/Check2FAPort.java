package com.quack.quack_app.Application.Ports.Input.Users;

import java.util.UUID;

public interface Check2FAPort {

    void check2FA(UUID idUser, String code);
}
