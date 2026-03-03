package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Mappers;

import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Rating;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.GameEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.ReviewEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class NoSQLMapper {

    public GameEntity toEntity(Game domain) {

        BigDecimal ratingValue = (domain.getRating() != null) ? domain.getRating().rate() :  new BigDecimal(0);

        return new GameEntity(
                domain.getId(),
                ratingValue,
                domain.getPlatforms(),
                domain.getPhotoUrl(),
                domain.getPublisher(),
                domain.getDeveloper(),
                domain.getReleaseDate(),
                domain.getGenre(),
                domain.getDescription(),
                domain.getName()
        );
    }

    public Game toDomain(GameEntity entity) {

        Reviews reviews = (entity.getReviewsList() != null)
                ? new Reviews(new ArrayList<>(entity.getReviewsList().stream()
                .map(this::toDomain)
                .toList()))
                : Reviews.Start();

        return new Game(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getReleaseDate(),
                entity.getGenre(),
                entity.getDeveloper(),
                entity.getPublisher(),
                entity.getPhotoUrl(),
                entity.getPlatforms(),
                new Rating(entity.getRating()),
                reviews
        );
    }

    public ReviewEntity toEntity(Review domain) {

        return new ReviewEntity(
                domain.getReviewId(),
                domain.getUserId(),
                domain.getGameId(),
                domain.getReviewDate(),
                domain.getStatus(),
                domain.getRating().rate(),
                domain.getReview()
        );
    }

    public Review toDomain(ReviewEntity entity) {

        return new Review(
                entity.getUserId(),
                entity.getGameId(),
                entity.getId(),
                entity.getReviewDate(),
                entity.getStatus(),
                new Rating(entity.getRating()),
                entity.getReview()
        );
    }
}
