package com.quack.quack_app.Domain.ValueObjects;

import com.quack.quack_app.Domain.Exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record FavoriteGames(List<UUID> gameIds) {
    private static final Logger log = LoggerFactory.getLogger(FavoriteGames.class);

    public FavoriteGames {
        gameIds = (gameIds == null) ? new ArrayList<>() : new ArrayList<>(gameIds);
        if (gameIds.size() > 5) {
            InvalidDataException ex = new InvalidDataException("Limit of 5 games exceeded");
            log.error("Validation failed for FavoriteGames: ", ex);
            throw ex;
        }
    }

    public static FavoriteGames start() {
        return new FavoriteGames(new ArrayList<>());
    }

    public void addFavorite(UUID gameId) {
        if (gameIds.size() >= 5) {
            InvalidDataException ex = new InvalidDataException("List is full");
            log.error("AddFavorite failed: List full", ex);
            throw ex;
        }
        if (gameIds.contains(gameId)) {
            InvalidDataException ex = new InvalidDataException("Duplicate game");
            log.error("AddFavorite failed: Duplicate entry", ex);
            throw ex;
        }
        gameIds.add(gameId);
    }

    public void removeFavorite(UUID gameId) {
        if (!gameIds.contains(gameId)) {
            InvalidDataException ex = new InvalidDataException("Game not found");
            log.error("RemoveFavorite failed: ID not in list", ex);
            throw ex;
        }
        gameIds.remove(gameId);
    }
}