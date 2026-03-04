package com.quack.quack_app.Application.Mappers;

import com.quack.quack_app.Application.DTOs.Games.DTOSaveGame;
import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Reviews.DTOSaveReview;
import com.quack.quack_app.Application.DTOs.Users.DTOGetUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Application.Mappers.Games.GameMapper;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Reviews.Status;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationMappersTest {

    // ─── GameMapper ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GameMapper")
    class GameMapperTest {

        private GameMapper mapper;

        @BeforeEach
        void setUp() {
            mapper = new GameMapper();
        }

        @Test
        @DisplayName("Deve mapear DTOSaveGame para Game com todos os campos")
        void toRegister_mapsAllFields() {
            DTOSaveGame dto = new DTOSaveGame(
                    "Quack Adventure",
                    "Um jogo épico de patos.",
                    LocalDate.of(2023, 6, 15),
                    "Adventure",
                    "Quack Studios",
                    "Quack Publishing",
                    "https://cdn.quack.com/photo.png",
                    "PC, PS5"
            );

            Game game = mapper.ToRegister(dto);

            assertNotNull(game);
            assertNotNull(game.getId());
            assertEquals("Quack Adventure", game.getName());
            assertEquals("Um jogo épico de patos.", game.getDescription());
            assertEquals(LocalDate.of(2023, 6, 15), game.getReleaseDate());
            assertEquals("Adventure", game.getGenre());
            assertEquals("Quack Studios", game.getDeveloper());
            assertEquals("Quack Publishing", game.getPublisher());
            assertEquals("https://cdn.quack.com/photo.png", game.getPhotoUrl());
            assertEquals("PC, PS5", game.getPlatforms());
        }

        @Test
        @DisplayName("Deve inicializar Reviews vazio ao mapear novo Game")
        void toRegister_startsWithEmptyReviews() {
            DTOSaveGame dto = new DTOSaveGame(
                    "Quack Adventure", "desc",
                    LocalDate.now(), "RPG",
                    "Dev", "Pub", "photo.png", "PC"
            );

            Game game = mapper.ToRegister(dto);

            assertNotNull(game.getReviews());
            assertEquals(0, game.getReviews().getReviewCount());
        }

        @Test
        @DisplayName("Cada chamada deve gerar um ID diferente para o Game")
        void toRegister_generatesUniqueId() {
            DTOSaveGame dto = new DTOSaveGame(
                    "Quack Adventure", "desc",
                    LocalDate.now(), "RPG",
                    "Dev", "Pub", "photo.png", "PC"
            );

            Game g1 = mapper.ToRegister(dto);
            Game g2 = mapper.ToRegister(dto);

            assertNotEquals(g1.getId(), g2.getId());
        }
    }

    // ─── ReviewMapper ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("ReviewMapper")
    class ReviewMapperTest {

        private ReviewMapper mapper;

        @BeforeEach
        void setUp() {
            mapper = new ReviewMapper();
        }

        @Test
        @DisplayName("Deve mapear DTOSaveReview para Review com status ON e data de hoje")
        void toRegister_mapsAllFields() {
            DTOSaveReview dto = TestModels.dtoSaveReview();

            Review review = mapper.toRegister(dto);

            assertNotNull(review);
            assertNotNull(review.getReviewId());
            assertEquals(dto.userId(), review.getUserId());
            assertEquals(dto.gameId(), review.getGameId());
            assertEquals(Status.ON, review.getStatus());
            assertEquals(dto.rating(), review.getRating());
            assertEquals(dto.review(), review.getReview());
            assertEquals(LocalDate.now(), review.getReviewDate());
        }

        @Test
        @DisplayName("Cada chamada deve gerar um reviewId diferente")
        void toRegister_generatesUniqueReviewId() {
            DTOSaveReview dto = TestModels.dtoSaveReview();

            Review r1 = mapper.toRegister(dto);
            Review r2 = mapper.toRegister(dto);

            assertNotEquals(r1.getReviewId(), r2.getReviewId());
        }

        @Test
        @DisplayName("Deve mapear Review para DTOReturnReview com todos os campos")
        void dtoReturnReview_mapsAllFields() {
            Review review = TestModels.activeReview();

            DTOReturnReview dto = mapper.dtoReturnReview(review, TestModels.username());

            assertNotNull(dto);
            assertEquals(TestModels.username(), dto.username());
            assertEquals(review.getGameId(), dto.gameId());
            assertEquals(review.getRating(), dto.rating());
            assertEquals(review.getReview(), dto.review());
            assertEquals(review.getReviewDate(), dto.reviewDate());
        }
    }

    // ─── UserMapper ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("UserMapper")
    class UserMapperTest {

        private UserMapper mapper;

        @BeforeEach
        void setUp() {
            mapper = new UserMapper();
        }

        @Test
        @DisplayName("Deve mapear DTOSaveUser para User com todos os campos")
        void userToRegister_mapsAllFields() {
            DTOSaveUser dto = TestModels.dtoSaveUser();

            User user = mapper.userToRegister(dto);

            assertNotNull(user);
            assertNotNull(user.getId());
            assertEquals(dto.username(), user.getUsername());
            assertEquals(dto.email(), user.getEmail());
            assertEquals(dto.description(), user.getDescription());
            assertEquals(dto.photoUrl(), user.getPhotoUrl());
        }

        @Test
        @DisplayName("Deve inicializar listas vazias ao mapear novo User")
        void userToRegister_initializesEmptyLists() {
            User user = mapper.userToRegister(TestModels.dtoSaveUser());

            assertTrue(user.getFavoriteGames().gameIds().isEmpty());
            assertTrue(user.getFriends().connections().isEmpty());
            assertTrue(user.getFollowers().connections().isEmpty());
        }

        @Test
        @DisplayName("Deve mapear User para DTOGetUser com todos os campos")
        void userToReturn_mapsAllFields() {
            User user = TestModels.activeUser();
            List<DTOReturnReview> reviews = List.of(TestModels.dtoReturnReview());

            DTOGetUser dto = mapper.userToReturn(user, reviews);

            assertNotNull(dto);
            assertEquals(user.getUsername(), dto.username());
            assertEquals(user.getPhotoUrl(), dto.photoUrl());
            assertEquals(user.getDescription(), dto.description());
            assertEquals(user.getFavoriteGames(), dto.favoriteGames());
            assertEquals(user.getFriends(), dto.friends());
            assertEquals(user.getFollowers(), dto.followers());
            assertEquals(reviews, dto.reviews());
        }

        @Test
        @DisplayName("Deve mapear User para DTOGetUser com lista de reviews vazia")
        void userToReturn_emptyReviews() {
            User user = TestModels.activeUser();

            DTOGetUser dto = mapper.userToReturn(user, List.of());

            assertNotNull(dto);
            assertTrue(dto.reviews().isEmpty());
        }
    }
}
