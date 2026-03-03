package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Reviews.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Reviews(List<Review> reviews) {

    private static final Logger log = LoggerFactory.getLogger(Reviews.class);

    public static Reviews Start() {
        return new Reviews(new ArrayList<>());
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public Reviews filterActiveReviews() {
        if (this.reviews == null) return new Reviews(new ArrayList<>());

        return new Reviews(reviews.stream()
                .filter(review -> review.getStatus() == Status.ON)
                .toList()
        );
    }

    public void removeReview(UUID reviewId) {
        try {
            filterActiveReviews().reviews.stream()
                    .filter(review -> review.getReviewId().equals(reviewId))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId))
                    .removeReview();
        } catch (Exception ex) {
            log.error("Technical failure: Review ID {} not found in active list", reviewId, ex);
            throw ex;
        }
    }

    public long getReviewCount() {
        return filterActiveReviews().reviews.size();
    }

    public Rating actualRating() {
        long count = getReviewCount();

        if (count == 0) return null;

        BigDecimal sum = filterActiveReviews().reviews.stream()
                .map(review -> review.getRating().rate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Rating(sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
    }
}