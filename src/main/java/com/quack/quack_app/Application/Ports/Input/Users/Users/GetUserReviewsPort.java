package com.quack.quack_app.Application.Ports.Input.Users.Users;

import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Reviews;

import java.util.UUID;

public interface GetUserReviewsPort {

    Reviews getReviewsByUser(UUID id, Natural pages, Natural size);
}
