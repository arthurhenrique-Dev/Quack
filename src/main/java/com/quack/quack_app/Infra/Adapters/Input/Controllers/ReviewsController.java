package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.UseCases.Reviews.DeleteReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.SaveReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.UpdateRatingReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.UpdateReviewUseCase;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Rating;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("quack/review")
public class ReviewsController {

    private final DeleteReviewUseCase deleteReviewUseCase;
    private final SaveReviewUseCase saveReviewUseCase;
    private final UpdateReviewUseCase updateReviewUseCase;
    private final UpdateRatingReviewUseCase updateRatingReviewUseCase;

    public ReviewsController(DeleteReviewUseCase deleteReviewUseCase, SaveReviewUseCase saveReviewUseCase, UpdateReviewUseCase updateReviewUseCase, UpdateRatingReviewUseCase updateRatingReviewUseCase) {
        this.deleteReviewUseCase = deleteReviewUseCase;
        this.saveReviewUseCase = saveReviewUseCase;
        this.updateReviewUseCase = updateReviewUseCase;
        this.updateRatingReviewUseCase = updateRatingReviewUseCase;
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable UUID id) {
        deleteReviewUseCase.DeleteReview(id);
    }
    @PostMapping()
    public void saveReview(@RequestBody Review review) {
        saveReviewUseCase.saveReviews(review);
    }
    @PutMapping("/{id}/rating_update/{idUser}")
    public void updateReview(@PathVariable UUID id, @PathVariable UUID idUser, @RequestBody Rating rating) {
        updateRatingReviewUseCase.updateRatingReview(id, idUser, rating);
    }
    @PutMapping("/{id}/review_update/{idUser}")
    public void updateReview(@PathVariable UUID id, @PathVariable UUID idUser, @RequestBody String review) {
        updateReviewUseCase.updateReview(id, idUser, review);
    }
}
