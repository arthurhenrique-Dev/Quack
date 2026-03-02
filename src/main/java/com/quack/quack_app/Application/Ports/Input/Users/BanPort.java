package com.quack.quack_app.Application.Ports.Input.Users;

import java.util.UUID;

public interface BanPort {

    void ban(UUID id);
}
