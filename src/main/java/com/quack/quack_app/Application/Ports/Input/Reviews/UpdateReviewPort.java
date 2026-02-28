package com.quack.quack_app.Application.Ports.Input.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOUpdateReview;

public interface UpdateReviewPort {

    void updateReview(DTOUpdateReview dtoUpdateReview);
}
