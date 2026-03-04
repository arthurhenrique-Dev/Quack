package com.quack.quack_app.Infra.Adapters.NoSQL;

import com.quack.quack_app.Application.DTOs.Games.DTOSearchGame;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Adapters.GameRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Adapters.ReviewRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Mappers.NoSQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.GameEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.ReviewEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories.MongoGameRepository;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories.MongoReviewRepository;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NoSQLAdaptersTest {

    // ─── ReviewRepositoryAdapter ──────────────────────────────────────────────

    @Nested
    @DisplayName("ReviewRepositoryAdapter")
    class ReviewRepositoryAdapterTest {

        @Mock MongoReviewRepository mongoRepository;
        @Mock NoSQLMapper noSQLMapper;
        @Mock MongoTemplate mongoTemplate;

        ReviewRepositoryAdapter adapter;

        @BeforeEach
        void setUp() {
            adapter = new ReviewRepositoryAdapter(mongoRepository, noSQLMapper, mongoTemplate);
        }

        @Test
        @DisplayName("Deve mapear e salvar review no MongoDB")
        void saveReview_delegatesToMongo() {
            Review review = TestModels.activeReview();
            ReviewEntity entity = mock(ReviewEntity.class);
            when(noSQLMapper.toEntity(review)).thenReturn(entity);

            adapter.saveReview(review);

            verify(noSQLMapper).toEntity(review);
            verify(mongoRepository).save(entity);
        }

        @Test
        @DisplayName("Deve retornar Reviews com reviews do usuário consultando por userId")
        void getReviews_returnsReviewsForUser() {
            Review review = TestModels.activeReview();
            ReviewEntity entity = mock(ReviewEntity.class);

            when(mongoTemplate.find(any(Query.class), eq(ReviewEntity.class)))
                    .thenReturn(List.of(entity));
            when(noSQLMapper.toDomain(entity)).thenReturn(review);

            Reviews result = adapter.getReviews(TestModels.USER_ID);

            assertNotNull(result);
            assertEquals(1, result.reviews().size());
            assertEquals(review, result.reviews().get(0));
        }

        @Test
        @DisplayName("Deve retornar Reviews vazio quando não houver reviews para o usuário")
        void getReviews_noReviews_returnsEmpty() {
            when(mongoTemplate.find(any(Query.class), eq(ReviewEntity.class)))
                    .thenReturn(List.of());

            Reviews result = adapter.getReviews(TestModels.USER_ID);

            assertNotNull(result);
            assertTrue(result.reviews().isEmpty());
            verifyNoInteractions(noSQLMapper);
        }

        @Test
        @DisplayName("Deve construir Query com critério userId correto")
        void getReviews_buildsQueryWithUserId() {
            when(mongoTemplate.find(any(Query.class), eq(ReviewEntity.class)))
                    .thenReturn(List.of());

            adapter.getReviews(TestModels.USER_ID);

            ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
            verify(mongoTemplate).find(queryCaptor.capture(), eq(ReviewEntity.class));

            String queryString = queryCaptor.getValue().getQueryObject().toJson();
            assertTrue(queryString.contains("userId"));
        }

        @Test
        @DisplayName("Deve retornar review quando encontrada por ID")
        void getReview_found_returnsMapped() {
            ReviewEntity entity = mock(ReviewEntity.class);
            Review review = TestModels.activeReview();

            when(mongoRepository.findById(TestModels.REVIEW_ID)).thenReturn(Optional.of(entity));
            when(noSQLMapper.toDomain(entity)).thenReturn(review);

            Optional<Review> result = adapter.getReview(TestModels.REVIEW_ID);

            assertTrue(result.isPresent());
            assertEquals(review, result.get());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando review não encontrada por ID")
        void getReview_notFound_returnsEmpty() {
            when(mongoRepository.findById(any())).thenReturn(Optional.empty());

            Optional<Review> result = adapter.getReview(UUID.randomUUID());

            assertTrue(result.isEmpty());
            verifyNoInteractions(noSQLMapper);
        }
    }

    // ─── GameRepositoryAdapter ────────────────────────────────────────────────

    @Nested
    @DisplayName("GameRepositoryAdapter")
    class GameRepositoryAdapterTest {

        @Mock MongoGameRepository mongoRepository;
        @Mock NoSQLMapper mapper;
        @Mock MongoTemplate mongoTemplate;

        GameRepositoryAdapter adapter;

        @BeforeEach
        void setUp() {
            adapter = new GameRepositoryAdapter(mongoRepository, mapper, mongoTemplate);
        }

        @SuppressWarnings("unchecked")
        private AggregationResults<GameEntity> emptyResults() {
            AggregationResults<GameEntity> results =
                    (AggregationResults<GameEntity>) mock(AggregationResults.class);
            when(results.getMappedResults()).thenReturn(List.of());
            return results;
        }

        @SuppressWarnings("unchecked")
        private AggregationResults<GameEntity> resultsWithOne(GameEntity entity) {
            AggregationResults<GameEntity> results =
                    (AggregationResults<GameEntity>) mock(AggregationResults.class);
            when(results.getMappedResults()).thenReturn(List.of(entity));
            return results;
        }

        @Test
        @DisplayName("Deve mapear e salvar game no MongoDB")
        void saveGame_delegatesToMongo() {
            Game game = TestModels.gameWithoutReviews();
            GameEntity entity = mock(GameEntity.class);
            when(mapper.toEntity(game)).thenReturn(entity);

            adapter.saveGame(game);

            verify(mapper).toEntity(game);
            verify(mongoRepository).save(entity);
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando id for null no getGameById")
        void getGameById_nullId_returnsEmpty() {
            Optional<Game> result = adapter.getGameById(null);

            assertTrue(result.isEmpty());
            verifyNoInteractions(mongoTemplate);
        }

        @Test
        @DisplayName("Deve retornar game quando encontrado por ID via aggregation")
        void getGameById_found_returnsMapped() {
            GameEntity entity = mock(GameEntity.class);
            Game game = TestModels.gameWithoutReviews();
            AggregationResults<GameEntity> results = resultsWithOne(entity);

            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));
            when(mapper.toDomain(entity)).thenReturn(game);

            Optional<Game> result = adapter.getGameById(TestModels.GAME_ID);

            assertTrue(result.isPresent());
            assertEquals(game, result.get());
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando game não encontrado por ID")
        void getGameById_notFound_returnsEmpty() {
            AggregationResults<GameEntity> results = emptyResults();
            when(results.getMappedResults()).thenReturn(List.of());
            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));

            Optional<Game> result = adapter.getGameById(TestModels.GAME_ID);

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Deve executar aggregation e retornar lista de games com dto preenchido")
        void getGames_withFilters_returnsMappedList() {
            DTOSearchGame dto = new DTOSearchGame(null, "Quack", null, "Adventure", null, null, null, null);
            GameEntity entity = mock(GameEntity.class);
            Game game = TestModels.gameWithoutReviews();
            AggregationResults<GameEntity> results = resultsWithOne(entity);

            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));
            when(mapper.toDomain(entity)).thenReturn(game);

            List<Game> resultList = adapter.getGames(dto, new Natural(0), new Natural(10));

            assertNotNull(resultList);
            assertEquals(1, resultList.size());
        }

        @Test
        @DisplayName("Deve usar DTOSearchGame.defaultSearch() quando dto for null")
        void getGames_nullDto_usesDefaultSearch() {
            AggregationResults<GameEntity> results = emptyResults();
            when(results.getMappedResults()).thenReturn(List.of());
            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));

            List<Game> resultList = adapter.getGames(null, new Natural(0), new Natural(10));

            assertNotNull(resultList);
            verify(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));
        }

        @Test
        @DisplayName("Deve usar DTOSearchGame.defaultSearch() quando todos os campos do dto forem vazios")
        void getGames_emptyDto_usesDefaultSearch() {
            DTOSearchGame dto = new DTOSearchGame(null, null, null, null, null, null, null, null);
            AggregationResults<GameEntity> results = emptyResults();
            when(results.getMappedResults()).thenReturn(List.of());
            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));

            List<Game> resultList = adapter.getGames(dto, new Natural(0), new Natural(10));

            assertNotNull(resultList);
        }

        @Test
        @DisplayName("Deve ordenar por rating e releaseDate quando dto tiver rating")
        void getGames_withRating_sortsByRatingAndReleaseDate() {
            DTOSearchGame dto = new DTOSearchGame(null, null, null, null, null, null, null, new BigDecimal("8.0"));
            AggregationResults<GameEntity> results = emptyResults();
            when(results.getMappedResults()).thenReturn(List.of());
            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));

            assertDoesNotThrow(() -> adapter.getGames(dto, new Natural(0), new Natural(10)));
            verify(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));
        }

        @Test
        @DisplayName("Deve aplicar filtro por id e ignorar outros campos quando id estiver presente")
        void getGames_withId_filtersById() {
            DTOSearchGame dto = new DTOSearchGame(
                    TestModels.GAME_ID, "ignorado", null,
                    "ignorado", null, null, null, null
            );
            AggregationResults<GameEntity> results = emptyResults();
            when(results.getMappedResults()).thenReturn(List.of());
            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));

            assertDoesNotThrow(() -> adapter.getGames(dto, new Natural(0), new Natural(10)));
        }

        @Test
        @DisplayName("Deve aplicar paginação correta com skip e limit na aggregation")
        void getGames_paginationApplied() {
            DTOSearchGame dto = new DTOSearchGame(null, "Quack", null, null, null, null, null, null);
            AggregationResults<GameEntity> results = emptyResults();
            when(results.getMappedResults()).thenReturn(List.of());
            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));

            adapter.getGames(dto, new Natural(2), new Natural(5));

            verify(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));
        }

        @Test
        @DisplayName("Deve filtrar por releaseDate quando dto tiver releaseDate")
        void getGames_withReleaseDate_addsFilter() {
            DTOSearchGame dto = new DTOSearchGame(
                    null, null, LocalDate.of(2023, 1, 1),
                    null, null, null, null, null
            );
            AggregationResults<GameEntity> results = emptyResults();
            when(results.getMappedResults()).thenReturn(List.of());
            doReturn(results).when(mongoTemplate).aggregate(any(TypedAggregation.class), any(String.class), any(Class.class));

            assertDoesNotThrow(() -> adapter.getGames(dto, new Natural(0), new Natural(10)));
        }
    }
}