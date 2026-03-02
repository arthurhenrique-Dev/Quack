package com.quack.quack_app.Domain.Reviews;

import com.quack.quack_app.Domain.ValueObjects.Rating;

import java.time.LocalDate;
import java.util.UUID;

public class Review {

    private final UUID userId;
    private final UUID gameId;
    private final UUID reviewId;
    private final LocalDate reviewDate;
    private Status status;
    private Rating rating;
    private String review;

    public Review(UUID userId, UUID gameId, UUID reviewId, LocalDate reviewDate, Rating rating) {
        this.userId = userId;
        this.gameId = gameId;
        this.reviewId = reviewId;
        this.reviewDate = reviewDate;
        this.rating = rating;
    }

    public Review(UUID userId, UUID gameId, UUID reviewId, LocalDate reviewDate, Status status, Rating rating, String review) {
        this.userId = userId;
        this.gameId = gameId;
        this.reviewId = reviewId;
        this.reviewDate = reviewDate;
        this.status = status;
        this.rating = rating;
        this.review = review;
    }

    public Review(UUID userId, UUID gameId, Rating rating) {
        this.userId = userId;
        this.gameId = gameId;
        this.reviewId = UUID.randomUUID();
        this.reviewDate = LocalDate.now();
        this.rating = rating;
    }

    public Review(UUID userId, UUID gameId, Rating rating, String review) {
        this.userId = userId;
        this.gameId = gameId;
        this.reviewId = UUID.randomUUID();
        this.reviewDate = LocalDate.now();
        this.rating = rating;
        this.review = review;
    }

    public void updateRating(Rating rating) {
        this.rating = rating;
    }

    public void updateReview(String review) {
        this.review = review;
    }

    public void removeReview(){
        this.status = this.status.removeReview();
    };

    public UUID getUserId() {
        return userId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public Rating getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public Status getStatus() {
        return status;
    }
}
