package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Adapters;

import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Mappers.NoSQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.ReviewEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories.MongoReviewRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final MongoReviewRepository mongoRepository;
    private final NoSQLMapper noSQLMapper;
    private final MongoTemplate mongoTemplate;

    public ReviewRepositoryAdapter(MongoReviewRepository mongoRepository, NoSQLMapper noSQLMapper, MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.noSQLMapper = noSQLMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @CacheEvict(value = "reviews", key = "#review.reviewId")
    public void saveReview(Review review) {
        mongoRepository.save(noSQLMapper.toEntity(review));
    }

    @Override
    @Cacheable(value = "user_reviews", key = "#id", unless = "#result == null")
    public Reviews getReviews(UUID id) {
        Query query = new Query(Criteria.where("userId").is(id));

        List<ReviewEntity> entities = mongoTemplate.find(query, ReviewEntity.class);

        List<Review> domainReviews = entities.stream()
                .map(noSQLMapper::toDomain)
                .toList();

        return new Reviews(domainReviews);
    }

    @Override
    @Cacheable(value = "reviews", key = "#reviewId", unless = "#result == null")
    public Optional<Review> getReview(UUID review) {
        return mongoRepository.findById(review)
                .map(noSQLMapper::toDomain);
    }
}
