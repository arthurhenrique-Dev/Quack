package com.quack.quack_app.Application.Mappers.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Username;

public class ReviewMapper {

    public DTOReturnReview dtoReturnReview(Review review, Username username){
        return new DTOReturnReview(
                username,
                review.getReviewDate(),
                review.getRating(),
                review.getReview()
        );
    }
}
