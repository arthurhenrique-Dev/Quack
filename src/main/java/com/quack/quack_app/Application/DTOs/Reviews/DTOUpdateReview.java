package com.quack.quack_app.Application.DTOs.Reviews;

import java.util.UUID;

public record DTOUpdateReview(UUID reviewId, UUID profileId, String reviewContent) {
}
