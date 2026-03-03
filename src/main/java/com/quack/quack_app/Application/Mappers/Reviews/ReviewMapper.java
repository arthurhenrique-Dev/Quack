package com.quack.quack_app.Application.Mappers.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Reviews.DTOSaveReview;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Reviews.Status;
import com.quack.quack_app.Domain.ValueObjects.Username;

import java.time.LocalDate;
import java.util.UUID;

public class ReviewMapper {

    public DTOReturnReview dtoReturnReview(Review review, Username username) {
        return new DTOReturnReview(
                username,
                review.getGameId(),
                review.getReviewDate(),
                review.getRating(),
                review.getReview()
        );
    }

    public Review toRegister(DTOSaveReview dtoSaveReview) {
        return new Review(
                dtoSaveReview.userId(),
                dtoSaveReview.gameId(),
                UUID.randomUUID(),
                LocalDate.now(),
                Status.ON,
                dtoSaveReview.rating(),
                dtoSaveReview.review()
        );
    }
}
