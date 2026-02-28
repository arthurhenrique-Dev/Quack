package com.quack.quack_app.Application.Ports.Input.Reviews;

import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Reviews;

public interface SaveReviewPort {

    void saveReviews(Review review);
}
