package com.quack.quack_app.TestModels;

import com.quack.quack_app.Application.DTOs.Reviews.DTOReturnReview;
import com.quack.quack_app.Application.DTOs.Reviews.DTOSaveReview;
import com.quack.quack_app.Application.DTOs.Users.DTOSaveUser;
import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Reviews.Status;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TestModels {

    // ─── IDs fixos ────────────────────────────────────────────────────────────
    public static final UUID USER_ID     = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001");
    public static final UUID GAME_ID     = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000002");
    public static final UUID REVIEW_ID   = UUID.fromString("cccccccc-0000-0000-0000-000000000003");
    public static final UUID TARGET_ID   = UUID.fromString("dddddddd-0000-0000-0000-000000000004");

    // ─── Value Objects ────────────────────────────────────────────────────────
    public static Username username()   { return new Username("quackUser"); }
    public static Email    email()      { return new Email("quack@quack.com"); }
    public static Password password()   {
        return new Password("$2a$10$7EqJtq98hPqEX7fNZaFWoOa6sT6/bGzBqBJCJjJlJlJlJlJlJlJlJ");
    }
    public static Description description() { return new Description("Sou um pato gamer."); }
    public static Rating rating()           { return new Rating(new BigDecimal("8.5")); }

    // ─── User ─────────────────────────────────────────────────────────────────

    public static User activeUser() {
        TwoFA twoFA = new TwoFA(true, "JBSWY3DPEHPK3PXP");
        TokenUpdater passwordUpdater = new TokenUpdater(
                UUID.randomUUID().toString(),
                java.time.LocalDateTime.now().plusHours(1)
        );
        return new User(
                USER_ID,
                password(),
                email(),
                com.quack.quack_app.Domain.Users.Role.USER,
                com.quack.quack_app.Domain.Users.Status.ACTIVE,
                passwordUpdater,
                null,
                twoFA,
                username(),
                "https://cdn.quack.com/photos/quackUser.png",
                description(),
                new FavoriteGames(new ArrayList<>()),
                new Connections(new ArrayList<>()),
                new Connections(new ArrayList<>())
        );
    }


    public static User pendingUser() {
        return new User(
                username(),
                new Password("Quack@123"),
                email(),
                description(),
                "https://cdn.quack.com/photos/quackUser.png"
        );
    }

    // ─── Game ─────────────────────────────────────────────────────────────────
    public static Game gameWithoutReviews() {
        return new Game(
                GAME_ID,
                "Quack Adventure",
                "Um jogo épico de patos.",
                LocalDate.of(2023, 6, 15),
                "Adventure",
                "Quack Studios",
                "Quack Publishing",
                "https://cdn.quack.com/games/quack-adventure.png",
                "PC, PS5",
                null,
                Reviews.Start()
        );
    }

    public static Game gameWithOneReview() {
        Game game = gameWithoutReviews();
        game.addReview(activeReview());
        game.updateRating();
        return game;
    }

    // ─── Review ───────────────────────────────────────────────────────────────
    public static Review activeReview() {
        return new Review(
                USER_ID,
                GAME_ID,
                REVIEW_ID,
                LocalDate.of(2024, 1, 10),
                Status.ON,
                rating(),
                "Muito bom, recomendo!"
        );
    }

    public static Review removedReview() {
        Review r = activeReview();
        r.removeReview();
        return r;
    }

    // ─── DTOs ─────────────────────────────────────────────────────────────────
    public static DTOSaveReview dtoSaveReview() {
        return new DTOSaveReview(
                USER_ID,
                GAME_ID,
                rating(),
                "Muito bom, recomendo!"
        );
    }

    public static DTOSearchUser dtoSearchUser() {
        return new DTOSearchUser(USER_ID, "quackUser");
    }

    public static DTOSaveUser dtoSaveUser() {
        return new DTOSaveUser(
                username(),
                new Password("Quack@123"),
                email(),
                description(),
                "https://cdn.quack.com/photos/quackUser.png"
        );
    }

    public static DTOReturnReview dtoReturnReview() {
        return new DTOReturnReview(
                username(),
                GAME_ID,
                LocalDate.of(2024, 1, 10),
                rating(),
                "Muito bom, recomendo!"
        );
    }

    // ─── Reviews (Value Object) ───────────────────────────────────────────────
    public static Reviews reviewsWithOneActive() {
        return new Reviews(new ArrayList<>(List.of(activeReview())));
    }

    public static Reviews emptyReviews() {
        return Reviews.Start();
    }
}
