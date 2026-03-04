package com.quack.quack_app.Infra.Mappers;

import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Reviews.Status;
import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.Users.Role;
import com.quack.quack_app.Domain.ValueObjects.*;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Mappers.NoSQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.GameEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models.ReviewEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers.SQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.ModeratorEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.UserEntity;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InfraMappersTest {

    // ─── NoSQLMapper ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("NoSQLMapper")
    class NoSQLMapperTest {

        private NoSQLMapper mapper;

        @BeforeEach
        void setUp() {
            mapper = new NoSQLMapper();
        }

        // Game → GameEntity
        @Test
        @DisplayName("Deve mapear Game para GameEntity com todos os campos")
        void toEntity_game_mapsAllFields() {
            Game game = TestModels.gameWithOneReview();

            GameEntity entity = mapper.toEntity(game);

            assertNotNull(entity);
            assertEquals(game.getId(), entity.getId());
            assertEquals(game.getName(), entity.getName());
            assertEquals(game.getDescription(), entity.getDescription());
            assertEquals(game.getGenre(), entity.getGenre());
            assertEquals(game.getDeveloper(), entity.getDeveloper());
            assertEquals(game.getPublisher(), entity.getPublisher());
            assertEquals(game.getPhotoUrl(), entity.getPhotoUrl());
            assertEquals(game.getPlatforms(), entity.getPlatforms());
            assertEquals(game.getReleaseDate(), entity.getReleaseDate());
        }

        @Test
        @DisplayName("Deve usar BigDecimal zero quando Game não tiver rating")
        void toEntity_game_nullRating_usesZero() {
            Game game = TestModels.gameWithoutReviews(); // rating null

            GameEntity entity = mapper.toEntity(game);

            assertEquals(BigDecimal.ZERO, entity.getRating());
        }

        // GameEntity → Game
        @Test
        @DisplayName("Deve mapear GameEntity para Game com lista de reviews")
        void toDomain_gameEntity_mapsReviews() {
            GameEntity entity = buildGameEntity(true);

            Game game = mapper.toDomain(entity);

            assertNotNull(game);
            assertEquals(entity.getId(), game.getId());
            assertNotNull(game.getReviews());
        }

        @Test
        @DisplayName("Deve mapear GameEntity para Game com reviews nulas como Reviews.Start()")
        void toDomain_gameEntity_nullReviews_startsEmpty() {
            GameEntity entity = buildGameEntity(false);

            Game game = mapper.toDomain(entity);

            assertNotNull(game.getReviews());
            assertEquals(0, game.getReviews().getReviewCount());
        }

        // Review → ReviewEntity
        @Test
        @DisplayName("Deve mapear Review para ReviewEntity com todos os campos")
        void toEntity_review_mapsAllFields() {
            Review review = TestModels.activeReview();

            ReviewEntity entity = mapper.toEntity(review);

            assertNotNull(entity);
            assertEquals(review.getReviewId(), entity.getId());
            assertEquals(review.getUserId(), entity.getUserId());
            assertEquals(review.getGameId(), entity.getGameId());
            assertEquals(review.getReviewDate(), entity.getReviewDate());
            assertEquals(review.getStatus(), entity.getStatus());
            assertEquals(review.getRating().rate(), entity.getRating());
            assertEquals(review.getReview(), entity.getReview());
        }

        // ReviewEntity → Review
        @Test
        @DisplayName("Deve mapear ReviewEntity para Review com todos os campos")
        void toDomain_reviewEntity_mapsAllFields() {
            ReviewEntity entity = buildReviewEntity();

            Review review = mapper.toDomain(entity);

            assertNotNull(review);
            assertEquals(entity.getId(), review.getReviewId());
            assertEquals(entity.getUserId(), review.getUserId());
            assertEquals(entity.getGameId(), review.getGameId());
            assertEquals(entity.getReviewDate(), review.getReviewDate());
            assertEquals(entity.getStatus(), review.getStatus());
            assertEquals(0, entity.getRating().compareTo(review.getRating().rate()));
            assertEquals(entity.getReview(), review.getReview());
        }

        // ─── helpers ──────────────────────────────────────────────────────────

        private GameEntity buildGameEntity(boolean withReviews) {
            GameEntity entity = new GameEntity(
                    TestModels.GAME_ID,
                    new BigDecimal("8.5"),
                    "PC, PS5",
                    "https://cdn.quack.com/photo.png",
                    "Quack Publishing",
                    "Quack Studios",
                    LocalDate.of(2023, 6, 15),
                    "Adventure",
                    "Um jogo épico de patos.",
                    "Quack Adventure"
            );
            if (withReviews) {
                entity.setReviewsList(List.of(buildReviewEntity()));
            }
            return entity;
        }

        private ReviewEntity buildReviewEntity() {
            return new ReviewEntity(
                    TestModels.REVIEW_ID,
                    TestModels.USER_ID,
                    TestModels.GAME_ID,
                    LocalDate.of(2024, 1, 10),
                    Status.ON,
                    new BigDecimal("8.5"),
                    "Muito bom!"
            );
        }
    }

    // ─── SQLMapper ────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("SQLMapper")
    class SQLMapperTest {

        private SQLMapper mapper;

        @BeforeEach
        void setUp() {
            mapper = new SQLMapper();
        }

        // User → UserEntity
        @Test
        @DisplayName("Deve mapear User para UserEntity com todos os campos")
        void toEntity_user_mapsAllFields() {
            User user = TestModels.activeUser();

            UserEntity entity = mapper.toEntity(user);

            assertNotNull(entity);
            assertEquals(user.getId(), entity.getId());
            assertEquals(user.getEmail().email(), entity.getEmail());
            assertEquals(user.getRole(), entity.getRole());
            assertEquals(user.getStatus(), entity.getStatus());
            assertEquals(user.getUsername().username(), entity.getUsername());
            assertEquals(user.getPhotoUrl(), entity.getPhotoUrl());
            assertEquals(user.getDescription().description(), entity.getDescription());
            assertTrue(entity.isEnabled2Fa());
            assertEquals(user.getTwoFA().secret(), entity.getSecret2Fa());
        }

        @Test
        @DisplayName("Deve encriptar senha plaintext ao mapear User para UserEntity")
        void toEntity_user_encodesPlaintextPassword() {
            User user = TestModels.pendingUser();

            UserEntity entity = mapper.toEntity(user);

            assertNotNull(entity.getPassword());
            assertTrue(entity.getPassword().startsWith("$2"));
        }

        @Test
        @DisplayName("Deve manter senha BCrypt sem re-encriptar ao mapear User para UserEntity")
        void toEntity_user_keepsBcryptPassword() {
            User user = TestModels.activeUser();

            UserEntity entity = mapper.toEntity(user);

            assertTrue(entity.getPassword().matches("^\\$2[aby]?\\$\\d{2}\\$.{53}$"));
        }

        // UserEntity → User
        @Test
        @DisplayName("Deve mapear UserEntity para User com todos os campos")
        void toDomain_userEntity_mapsAllFields() {
            UserEntity entity = buildUserEntity();

            User user = mapper.toDomain(entity);

            assertNotNull(user);
            assertEquals(entity.getId(), user.getId());
            assertEquals(entity.getEmail(), user.getEmail().email());
            assertEquals(entity.getRole(), user.getRole());
            assertEquals(entity.getStatus(), user.getStatus());
            assertEquals(entity.getUsername(), user.getUsername().username());
            assertEquals(entity.getPhotoUrl(), user.getPhotoUrl());
            assertEquals(entity.getDescription(), user.getDescription().description());
        }

        @Test
        @DisplayName("Deve mapear FavoriteGames, Friends e Followers corretamente")
        void toDomain_userEntity_mapsCollections() {
            UserEntity entity = buildUserEntity();
            UUID favId = UUID.randomUUID();
            UUID friendId = UUID.randomUUID();
            UUID followerId = UUID.randomUUID();
            entity.setFavoriteGames(new ArrayList<>(List.of(favId)));
            entity.setFriends(new ArrayList<>(List.of(friendId)));
            entity.setFollowers(new ArrayList<>(List.of(followerId)));

            User user = mapper.toDomain(entity);

            assertTrue(user.getFavoriteGames().gameIds().contains(favId));
            assertTrue(user.getFriends().connections().contains(friendId));
            assertTrue(user.getFollowers().connections().contains(followerId));
        }

        @Test
        @DisplayName("Deve mapear listas nulas como listas vazias")
        void toDomain_userEntity_nullCollections_mapsAsEmpty() {
            UserEntity entity = buildUserEntity();
            entity.setFavoriteGames(null);
            entity.setFriends(null);
            entity.setFollowers(null);

            User user = mapper.toDomain(entity);

            assertTrue(user.getFavoriteGames().gameIds().isEmpty());
            assertTrue(user.getFriends().connections().isEmpty());
            assertTrue(user.getFollowers().connections().isEmpty());
        }

        // Moderator → ModeratorEntity
        @Test
        @DisplayName("Deve mapear Moderator para ModeratorEntity com campos base")
        void toEntity_moderator_mapsBaseFields() {
            Moderator moderator = new Moderator(
                    TestModels.password(),
                    TestModels.email()
            );

            ModeratorEntity entity = mapper.toEntity(moderator);

            assertNotNull(entity);
            assertEquals(moderator.getId(), entity.getId());
            assertEquals(moderator.getEmail().email(), entity.getEmail());
            assertEquals(moderator.getRole(), entity.getRole());
        }

        // ModeratorEntity → Moderator
        @Test
        @DisplayName("Deve mapear ModeratorEntity para Moderator com campos base")
        void toDomain_moderatorEntity_mapsAllFields() {
            ModeratorEntity entity = buildModeratorEntity();

            Moderator moderator = mapper.toDomain(entity);

            assertNotNull(moderator);
            assertEquals(entity.getId(), moderator.getId());
            assertEquals(entity.getEmail(), moderator.getEmail().email());
            assertEquals(entity.getRole(), moderator.getRole());
        }

        // ─── helpers ──────────────────────────────────────────────────────────

        private UserEntity buildUserEntity() {
            UserEntity entity = new UserEntity();
            entity.setId(TestModels.USER_ID);
            entity.setPassword("$2a$10$7EqJtq98hPqEX7fNZaFWoOa6sTXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            entity.setEmail("quack@quack.com");
            entity.setRole(Role.USER);
            entity.setStatus(com.quack.quack_app.Domain.Users.Status.ACTIVE);
            entity.setEnabled2Fa(true);
            entity.setSecret2Fa("JBSWY3DPEHPK3PXP");
            entity.setUsername("quackUser");
            entity.setPhotoUrl("https://cdn.quack.com/photos/quackUser.png");
            entity.setDescription("Sou um pato gamer.");
            entity.setPasswordUpdaterToken(UUID.randomUUID().toString());
            entity.setPasswordUpdaterexpiration(LocalDateTime.now().plusHours(1));
            entity.setEmailUpdaterToken(UUID.randomUUID().toString());
            entity.setEmailUpdaterexpiration(LocalDateTime.now().plusHours(1));
            entity.setFavoriteGames(new ArrayList<>());
            entity.setFriends(new ArrayList<>());
            entity.setFollowers(new ArrayList<>());
            return entity;
        }

        private ModeratorEntity buildModeratorEntity() {
            ModeratorEntity entity = new ModeratorEntity();
            entity.setId(UUID.randomUUID());
            entity.setPassword("$2a$10$7EqJtq98hPqEX7fNZaFWoOa6sTXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            entity.setEmail("mod@quack.com");
            entity.setRole(Role.MODERATOR);
            entity.setStatus(com.quack.quack_app.Domain.Users.Status.ACTIVE);
            entity.setEnabled2Fa(false);
            entity.setSecret2Fa(null);
            entity.setPasswordUpdaterToken(null);
            entity.setPasswordUpdaterexpiration(null);
            entity.setEmailUpdaterToken(null);
            entity.setEmailUpdaterexpiration(null);
            return entity;
        }
    }
}
