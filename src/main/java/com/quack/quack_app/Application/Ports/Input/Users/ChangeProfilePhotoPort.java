package com.quack.quack_app.Application.Ports.Input.Users;

import java.util.UUID;

public interface ChangeProfilePhotoPort {

    void changeProfilePhoto(UUID idUser, String photoUrl);
}
