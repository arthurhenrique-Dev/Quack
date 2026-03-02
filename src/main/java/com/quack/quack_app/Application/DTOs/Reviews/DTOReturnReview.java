package com.quack.quack_app.Application.DTOs.Reviews;

import com.quack.quack_app.Domain.ValueObjects.Rating;
import com.quack.quack_app.Domain.ValueObjects.Username;

import java.time.LocalDate;

public record DTOReturnReview(

        Username username,
        LocalDate reviewDate,
        Rating rating,
        String review
) {
}
