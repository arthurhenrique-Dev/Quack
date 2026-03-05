package com.quack.quack_app.Infra.Adapters.Input.Controllers;

import com.quack.quack_app.Application.DTOs.Reviews.DTOSaveReview;
import com.quack.quack_app.Application.UseCases.Reviews.DeleteReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.SaveReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.UpdateRatingReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.UpdateReviewUseCase;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Rating;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Operation(summary = "delete review by id", description = "requisition that delete a review")
    @ApiResponse(responseCode = "200", description = "review deleteted with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void deleteReview(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable UUID idReview) {
        deleteReviewUseCase.DeleteReview(user.getId(), idReview);
    }
    @PostMapping()
    @Operation(summary = "save review by id", description = "requisition that save a review")
    @ApiResponse(responseCode = "200", description = "review saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void saveReview(@RequestBody DTOSaveReview review) {
        saveReviewUseCase.saveReviews(review);
    }
    @PutMapping("/{id}/rating_update")
    @Operation(summary = "save review by id", description = "requisition that update a review")
    @ApiResponse(responseCode = "200", description = "review saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void updateReview(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl user, @RequestBody Rating rating) {
        updateRatingReviewUseCase.updateRatingReview(id, user.getId(), rating);
    }
    @PutMapping("/{id}/review_update")
    @Operation(summary = "save review by id", description = "requisition that update a rating of a review")
    @ApiResponse(responseCode = "200", description = "review saved with sucess")
    @ApiResponse(responseCode = "400", description = "invalid request content")
    @ApiResponse(responseCode = "401", description = "validation failed")
    @ApiResponse(responseCode = "404", description = "user not found")
    @ApiResponse(responseCode = "500", description = "internal server error")
    public void updateReview(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl user, @RequestBody String review) {
        updateReviewUseCase.updateReview(id, user.getId(), review);
    }
}
