package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories;

import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.GameEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoGameRepository extends MongoRepository<GameEntity, UUID> {
}
