package com.quack.quack_app.Application.Ports.Input.Users.Users;

import java.util.UUID;

public interface FollowPort {

    void follow(UUID idFollower, UUID idFollowing);
}
