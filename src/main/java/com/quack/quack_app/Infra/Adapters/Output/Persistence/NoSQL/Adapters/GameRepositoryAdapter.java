package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Adapters;

import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Rating;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Mappers.NoSQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.GameEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories.MongoGameRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GameRepositoryAdapter implements GameRepository {

    private final MongoGameRepository mongoRepository;
    private final NoSQLMapper mapper;
    private final MongoTemplate mongoTemplate;

    public GameRepositoryAdapter(MongoGameRepository mongoRepository, NoSQLMapper mapper, MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Game> getGames(DTOSearchGame dto, Natural pages, Natural size) {
        if (dto == null || isSearchEmpty(dto)) {
            dto = DTOSearchGame.defaultSearch();
        }

        List<Criteria> filters = buildFilters(dto);
        List<AggregationOperation> operations = new ArrayList<>();

        if (!filters.isEmpty()) {
            operations.add(Aggregation.match(new Criteria().andOperator(filters.toArray(new Criteria[0]))));
        }

        if (dto.rating() != null) {
            operations.add(Aggregation.sort(Sort.Direction.DESC, "rating", "releaseDate"));
        } else {
            operations.add(Aggregation.sort(Sort.Direction.DESC, "releaseDate"));
        }

        operations.add(Aggregation.skip((long) pages.i() * size.i()));
        operations.add(Aggregation.limit(size.i()));
        operations.addAll(getReviewAggregationSteps());

        return executeAggregation(operations);
    }

    @Override
    public Optional<Game> getGameById(UUID id) {
        if (id == null) return Optional.empty();

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.match(Criteria.where("_id").is(id)));
        operations.addAll(getReviewAggregationSteps());

        var aggregation = Aggregation.newAggregation(GameEntity.class, operations);
        var results = mongoTemplate.aggregate(aggregation, "games", GameEntity.class);

        return results.getMappedResults()
                .stream()
                .map(mapper::toDomain)
                .findFirst();
    }

    private List<AggregationOperation> getReviewAggregationSteps() {
        return List.of(
                Aggregation.lookup("reviews", "_id", "gameId", "reviewsList"),
                Aggregation.project("name", "description", "releaseDate", "genre", "developer", "publisher", "photoUrl", "platforms", "rating")
                        .and("reviewsList").slice(10, 0).as("reviewsList")
        );
    }

    private List<Game> executeAggregation(List<AggregationOperation> operations) {
        return mongoTemplate.aggregate(Aggregation.newAggregation(GameEntity.class, operations), "games", GameEntity.class)
                .getMappedResults()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    private List<Criteria> buildFilters(DTOSearchGame dto) {
        List<Criteria> criteria = new ArrayList<>();

        if (dto.id() != null) {
            criteria.add(Criteria.where("_id").is(dto.id()));
            return criteria;
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            criteria.add(Criteria.where("name").regex(dto.name(), "i"));
        }

        if (dto.genre() != null && !dto.genre().isBlank()) {
            criteria.add(Criteria.where("genre").regex(dto.genre(), "i"));
        }

        if (dto.rating() != null) {
            criteria.add(Criteria.where("rating").lte(dto.rating()));
        }

        if (dto.developer() != null && !dto.developer().isBlank()) {
            criteria.add(Criteria.where("developer").regex(dto.developer(), "i"));
        }

        if (dto.publisher() != null && !dto.publisher().isBlank()) {
            criteria.add(Criteria.where("publisher").regex(dto.publisher(), "i"));
        }

        if (dto.releaseDate() != null) {
            criteria.add(Criteria.where("releaseDate").lte(dto.releaseDate()));
        }

        return criteria;
    }

    private boolean isSearchEmpty(DTOSearchGame dto) {
        return dto.id() == null &&
                (dto.name() == null || dto.name().isBlank()) &&
                (dto.genre() == null || dto.genre().isBlank()) &&
                dto.rating() == null &&
                dto.releaseDate() == null;
    }

    @Override
    public void saveGame(Game game) {
        mongoRepository.save(mapper.toEntity(game));
    }
}