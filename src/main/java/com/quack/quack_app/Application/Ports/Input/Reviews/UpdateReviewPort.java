package com.quack.quack_app.Application.Ports.Input.Reviews;

import java.util.UUID;

public interface UpdateReviewPort {

    void updateReview(UUID reviewId, UUID profileId, String reviewContent);
}
