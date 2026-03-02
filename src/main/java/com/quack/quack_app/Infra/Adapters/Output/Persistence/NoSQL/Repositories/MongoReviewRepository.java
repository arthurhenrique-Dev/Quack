package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories;

import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.ReviewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoReviewRepository extends MongoRepository<ReviewEntity, UUID> {
}
