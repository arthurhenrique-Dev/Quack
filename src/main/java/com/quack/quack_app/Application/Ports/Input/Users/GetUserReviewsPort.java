package com.quack.quack_app.Application.Ports.Input.Users;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Domain.ValueObjects.Natural;

import java.util.List;
import java.util.UUID;

public interface GetUserReviewsPort {

    List<DTOReturnReview> getReviewsByUser(UUID id, Natural pages, Natural size);
}
