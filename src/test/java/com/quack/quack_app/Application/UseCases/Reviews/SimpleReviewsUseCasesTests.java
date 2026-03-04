package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.UseCases.Services.Review.ValidateReviewService;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Rating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleReviewsUseCasesTests {

    @Mock private ReviewRepository reviewRepository;
    @Mock private ValidateReviewService validateReviewService;
    @Mock private Review reviewMock;

    @Test
    @DisplayName("Deve deletar review")
    void testDeleteReview() {
        UUID reviewId = UUID.randomUUID();
        when(reviewRepository.getReview(reviewId)).thenReturn(Optional.of(reviewMock));

        new DeleteReviewUseCase(reviewRepository).DeleteReview(reviewId);

        verify(reviewMock).removeReview();
        verify(reviewRepository).saveReview(reviewMock);
    }

    @Test
    @DisplayName("Deve atualizar rating da review")
    void testUpdateRatingReview() {
        UUID reviewId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        Rating rating = mock(Rating.class);
        when(validateReviewService.validateReview(reviewId, profileId)).thenReturn(reviewMock);

        new UpdateRatingReviewUseCase(reviewRepository, validateReviewService).updateRatingReview(reviewId, profileId, rating);

        verify(reviewMock).updateRating(rating);
        verify(reviewRepository).saveReview(reviewMock);
    }

    @Test
    @DisplayName("Deve atualizar conteúdo da review")
    void testUpdateReview() {
        UUID reviewId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        when(validateReviewService.validateReview(reviewId, profileId)).thenReturn(reviewMock);

        new UpdateReviewUseCase(reviewRepository, validateReviewService).updateReview(reviewId, profileId, "novo conteudo");

        verify(reviewMock).updateReview("novo conteudo");
        verify(reviewRepository).saveReview(reviewMock);
    }
}