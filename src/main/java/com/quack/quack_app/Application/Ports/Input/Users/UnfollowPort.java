package com.quack.quack_app.Application.Ports.Input.Users;

import java.util.UUID;

public interface UnfollowPort {

    void unfollow(UUID unfollower, UUID unfollowing);
}
