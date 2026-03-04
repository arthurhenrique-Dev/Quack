package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Reviews.Status;
import com.quack.quack_app.TestModels.TestModels;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    // ─── Email ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Email")
    class EmailTest {

        @Test
        @DisplayName("Deve criar Email válido")
        void email_valid() {
            assertDoesNotThrow(() -> new Email("quack@quack.com"));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para email sem @")
        void email_missingAt_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Email("quackquack.com"));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para email nulo")
        void email_null_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Email(null));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para email vazio")
        void email_empty_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Email(""));
        }
    }

    // ─── Password ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Password")
    class PasswordTest {

        @Test
        @DisplayName("Deve aceitar senha forte no formato plaintext")
        void password_strongPlaintext_valid() {
            assertDoesNotThrow(() -> new Password("Quack@123"));
        }

        @Test
        @DisplayName("Deve aceitar senha já em formato BCrypt")
        void password_bcryptFormat_valid() {
            assertDoesNotThrow(() -> new Password("$2a$10$7EqJtq98hPqEX7fNZaFWoOa6sTXXXXXXXXXXXXXXXXXXXXXXXXXXX"));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para senha fraca")
        void password_weak_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Password("simples"));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para senha sem caractere especial")
        void password_noSpecialChar_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Password("Quack1234"));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para senha nula")
        void password_null_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Password(null));
        }
    }

    // ─── Username ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Username")
    class UsernameTest {

        @Test
        @DisplayName("Deve criar Username válido")
        void username_valid() {
            assertDoesNotThrow(() -> new Username("quackUser"));
        }

        @Test
        @DisplayName("Deve aceitar Username vazio")
        void username_empty_valid() {
            assertDoesNotThrow(() -> new Username(""));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Username com mais de 130 caracteres")
        void username_tooLong_throwsInvalidData() {
            String longName = "a".repeat(131);
            assertThrows(InvalidDataException.class, () -> new Username(longName));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Username nulo")
        void username_null_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Username(null));
        }
    }

    // ─── Description ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Description")
    class DescriptionTest {

        @Test
        @DisplayName("Deve criar Description válida")
        void description_valid() {
            assertDoesNotThrow(() -> new Description("Sou um pato gamer."));
        }

        @Test
        @DisplayName("Deve aceitar Description vazia")
        void description_empty_valid() {
            assertDoesNotThrow(() -> new Description(""));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Description com mais de 250 caracteres")
        void description_tooLong_throwsInvalidData() {
            String longDesc = "a".repeat(251);
            assertThrows(InvalidDataException.class, () -> new Description(longDesc));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Description nula")
        void description_null_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Description(null));
        }
    }

    // ─── Rating ───────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Rating")
    class RatingTest {

        @Test
        @DisplayName("Deve criar Rating válido com valor 0")
        void rating_zero_valid() {
            assertDoesNotThrow(() -> new Rating(BigDecimal.ZERO));
        }

        @Test
        @DisplayName("Deve criar Rating válido com valor 10")
        void rating_ten_valid() {
            assertDoesNotThrow(() -> new Rating(BigDecimal.TEN));
        }

        @Test
        @DisplayName("Deve criar Rating válido com uma casa decimal")
        void rating_oneDecimal_valid() {
            assertDoesNotThrow(() -> new Rating(new BigDecimal("8.5")));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Rating negativo")
        void rating_negative_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Rating(new BigDecimal("-1")));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Rating acima de 10")
        void rating_aboveTen_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Rating(new BigDecimal("10.1")));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Rating com mais de uma casa decimal")
        void rating_twoDecimals_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Rating(new BigDecimal("8.55")));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para Rating nulo")
        void rating_null_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Rating(null));
        }
    }

    // ─── Natural ──────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Natural")
    class NaturalTest {

        @Test
        @DisplayName("Deve criar Natural válido com zero")
        void natural_zero_valid() {
            assertDoesNotThrow(() -> new Natural(0));
        }

        @Test
        @DisplayName("Deve criar Natural válido com número positivo")
        void natural_positive_valid() {
            assertDoesNotThrow(() -> new Natural(42));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para número negativo")
        void natural_negative_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Natural(-1));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException para valor nulo")
        void natural_null_throwsInvalidData() {
            assertThrows(InvalidDataException.class, () -> new Natural(null));
        }
    }

    // ─── TwoFA ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("TwoFA")
    class TwoFATest {

        @Test
        @DisplayName("Deve criar TwoFA desabilitado sem secret")
        void twoFA_disabled_valid() {
            assertDoesNotThrow(() -> TwoFA.disabled());
            assertFalse(TwoFA.disabled().enabled());
            assertNull(TwoFA.disabled().secret());
        }

        @Test
        @DisplayName("Deve criar TwoFA habilitado com secret válido")
        void twoFA_enabled_withSecret_valid() {
            assertDoesNotThrow(() -> new TwoFA(true, "SECRETBASE32"));
        }

        @Test
        @DisplayName("Deve lançar ValidationFailedException ao habilitar sem secret")
        void twoFA_enabled_withoutSecret_throwsValidationFailed() {
            assertThrows(ValidationFailedException.class, () -> new TwoFA(true, null));
        }

        @Test
        @DisplayName("Deve lançar ValidationFailedException ao habilitar com secret em branco")
        void twoFA_enabled_blankSecret_throwsValidationFailed() {
            assertThrows(ValidationFailedException.class, () -> new TwoFA(true, "   "));
        }
    }

    // ─── TokenUpdater ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("TokenUpdater")
    class TokenUpdaterTest {

        @Test
        @DisplayName("Deve criar TokenUpdater via Start com token e expiração válidos")
        void tokenUpdater_start_valid() {
            TokenUpdater updater = TokenUpdater.Start();
            assertNotNull(updater.token());
            assertNotNull(updater.expiration());
            assertTrue(updater.expiration().isAfter(java.time.LocalDateTime.now()));
        }

        @Test
        @DisplayName("Deve validar token correto sem lançar exceção")
        void tokenUpdater_check_validToken_success() {
            TokenUpdater updater = TokenUpdater.Start();
            assertDoesNotThrow(() -> updater.Check(updater.token()));
        }

        @Test
        @DisplayName("Deve lançar ValidationFailedException para token incorreto")
        void tokenUpdater_check_invalidToken_throwsValidationFailed() {
            TokenUpdater updater = TokenUpdater.Start();
            assertThrows(ValidationFailedException.class, () -> updater.Check("token-errado"));
        }

        @Test
        @DisplayName("Deve lançar ValidationFailedException para token expirado")
        void tokenUpdater_check_expiredToken_throwsValidationFailed() {
            TokenUpdater expired = new TokenUpdater(
                    "token-valido",
                    java.time.LocalDateTime.now().minusMinutes(1)
            );
            assertThrows(ValidationFailedException.class, () -> expired.Check("token-valido"));
        }
    }

    // ─── FavoriteGames ────────────────────────────────────────────────────────

    @Nested
    @DisplayName("FavoriteGames")
    class FavoriteGamesTest {

        @Test
        @DisplayName("Deve criar FavoriteGames vazio")
        void favoriteGames_empty_valid() {
            assertDoesNotThrow(() -> FavoriteGames.start());
        }

        @Test
        @DisplayName("Deve adicionar jogo com sucesso")
        void favoriteGames_addFavorite_success() {
            FavoriteGames fg = FavoriteGames.start();
            assertDoesNotThrow(() -> fg.addFavorite(UUID.randomUUID()));
            assertEquals(1, fg.gameIds().size());
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException ao adicionar jogo duplicado")
        void favoriteGames_addDuplicate_throwsInvalidData() {
            UUID id = UUID.randomUUID();
            FavoriteGames fg = FavoriteGames.start();
            fg.addFavorite(id);
            assertThrows(InvalidDataException.class, () -> fg.addFavorite(id));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException ao exceder 5 jogos")
        void favoriteGames_exceedLimit_throwsInvalidData() {
            FavoriteGames fg = FavoriteGames.start();
            for (int i = 0; i < 5; i++) fg.addFavorite(UUID.randomUUID());
            assertThrows(InvalidDataException.class, () -> fg.addFavorite(UUID.randomUUID()));
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException na construção com mais de 5 jogos")
        void favoriteGames_constructWithMoreThanFive_throwsInvalidData() {
            List<UUID> ids = new ArrayList<>();
            for (int i = 0; i < 6; i++) ids.add(UUID.randomUUID());
            assertThrows(InvalidDataException.class, () -> new FavoriteGames(ids));
        }

        @Test
        @DisplayName("Deve remover jogo com sucesso")
        void favoriteGames_removeFavorite_success() {
            UUID id = UUID.randomUUID();
            FavoriteGames fg = FavoriteGames.start();
            fg.addFavorite(id);
            assertDoesNotThrow(() -> fg.removeFavorite(id));
            assertTrue(fg.gameIds().isEmpty());
        }

        @Test
        @DisplayName("Deve lançar InvalidDataException ao remover jogo inexistente")
        void favoriteGames_removeNotFound_throwsInvalidData() {
            FavoriteGames fg = FavoriteGames.start();
            assertThrows(InvalidDataException.class, () -> fg.removeFavorite(UUID.randomUUID()));
        }
    }

    // ─── Connections ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Connections")
    class ConnectionsTest {

        @Test
        @DisplayName("Deve adicionar conexão com sucesso")
        void connections_addConnection_success() {
            Connections c = new Connections(new ArrayList<>());
            UUID id = UUID.randomUUID();
            c.addConnection(id);
            assertEquals(1, c.nOfConnections());
            assertTrue(c.connections().contains(id));
        }

        @Test
        @DisplayName("Deve remover conexão com sucesso")
        void connections_removeConnection_success() {
            UUID id = UUID.randomUUID();
            Connections c = new Connections(new ArrayList<>(List.of(id)));
            c.removeConnection(id);
            assertEquals(0, c.nOfConnections());
        }

        @Test
        @DisplayName("Deve retornar contagem correta de conexões")
        void connections_nOfConnections_returnsCorrectCount() {
            Connections c = new Connections(new ArrayList<>(List.of(UUID.randomUUID(), UUID.randomUUID())));
            assertEquals(2, c.nOfConnections());
        }
    }

    // ─── Reviews ──────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Reviews")
    class ReviewsTest {

        @Test
        @DisplayName("Deve iniciar Reviews vazio via Start()")
        void reviews_start_isEmpty() {
            Reviews reviews = Reviews.Start();
            assertEquals(0, reviews.getReviewCount());
        }

        @Test
        @DisplayName("Deve adicionar review com sucesso")
        void reviews_addReview_success() {
            Reviews reviews = Reviews.Start();
            reviews.addReview(TestModels.activeReview());
            assertEquals(1, reviews.getReviewCount());
        }

        @Test
        @DisplayName("Deve filtrar apenas reviews com status ON")
        void reviews_filterActiveReviews_returnsOnlyActive() {
            Review active  = TestModels.activeReview();
            Review removed = new Review(
                    UUID.randomUUID(), UUID.randomUUID(),
                    UUID.randomUUID(), LocalDate.now(),
                    Status.OFF, new Rating(new BigDecimal("5.0")), "removida"
            );
            Reviews reviews = new Reviews(new ArrayList<>(List.of(active, removed)));
            assertEquals(1, reviews.filterActiveReviews().reviews().size());
        }

        @Test
        @DisplayName("Deve calcular rating médio corretamente")
        void reviews_actualRating_calculatesAverage() {
            Review r1 = new Review(UUID.randomUUID(), UUID.randomUUID(),
                    UUID.randomUUID(), LocalDate.now(), Status.ON,
                    new Rating(new BigDecimal("8.0")), "ótimo");
            Review r2 = new Review(UUID.randomUUID(), UUID.randomUUID(),
                    UUID.randomUUID(), LocalDate.now(), Status.ON,
                    new Rating(new BigDecimal("6.0")), "bom");
            Reviews reviews = new Reviews(new ArrayList<>(List.of(r1, r2)));

            Rating result = reviews.actualRating();
            assertNotNull(result);
            assertEquals(0, new BigDecimal("7.0").compareTo(result.rate()));
        }

        @Test
        @DisplayName("Deve retornar null para rating quando não houver reviews ativas")
        void reviews_actualRating_noActiveReviews_returnsNull() {
            assertNull(Reviews.Start().actualRating());
        }

        @Test
        @DisplayName("Deve remover review ativa pelo id")
        void reviews_removeReview_success() {
            Review review = TestModels.activeReview();
            Reviews reviews = new Reviews(new ArrayList<>(List.of(review)));
            assertDoesNotThrow(() -> reviews.removeReview(review.getReviewId()));
        }

        @Test
        @DisplayName("Deve lançar exceção ao remover review com id inexistente")
        void reviews_removeReview_notFound_throwsException() {
            Reviews reviews = Reviews.Start();
            assertThrows(Exception.class, () -> reviews.removeReview(UUID.randomUUID()));
        }
    }
}
