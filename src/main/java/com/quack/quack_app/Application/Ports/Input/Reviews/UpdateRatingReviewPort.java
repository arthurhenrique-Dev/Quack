package com.quack.quack_app.Application.Ports.Input.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOUpdateRatingReview;

import java.util.UUID;

public interface UpdateRatingReviewPort {

    void updateRatingReview(DTOUpdateRatingReview dtoUpdateRatingReview);
}
