package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers;

import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.*;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.BaseEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.ModeratorEntity;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SQLMapper {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String BCRYPT_PATTERN = "^\\$2[aby]?\\$\\d{2}\\$.{53}$";

    public UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        mapBaseToEntity(domain, entity);

        entity.setUsername(domain.getUsername().username());
        entity.setPhotoUrl(domain.getPhotoUrl());
        entity.setDescription(domain.getDescription().description());
        entity.setFavoriteGames(new ArrayList<>(domain.getFavoriteGames().gameIds()));
        entity.setFriends(new ArrayList<>(domain.getFriends().connections()));
        entity.setFollowers(new ArrayList<>(domain.getFollowers().connections()));

        return entity;
    }

    public ModeratorEntity toEntity(Moderator domain) {
        ModeratorEntity entity = new ModeratorEntity();
        mapBaseToEntity(domain, entity);
        return entity;
    }

    private void mapBaseToEntity(BaseUser domain, BaseEntity entity) {
        entity.setId(domain.getId());

        String rawPassword = domain.getPassword().password();
        if (rawPassword.matches(BCRYPT_PATTERN)) {
            entity.setPassword(rawPassword); // já é hash, usa direto
        } else {
            entity.setPassword(passwordEncoder.encode(rawPassword)); // encripta agora
        }

        entity.setEmail(domain.getEmail().email());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        entity.setEnabled2Fa(domain.getTwoFA().enabled());
        entity.setSecret2Fa(domain.getTwoFA().secret());

        if (domain.getPasswordUpdater() != null) {
            entity.setPasswordUpdaterToken(domain.getPasswordUpdater().token());
            entity.setPasswordUpdaterexpiration(domain.getPasswordUpdater().expiration());
        }
        if (domain.getEmailUpdater() != null) {
            entity.setEmailUpdaterToken(domain.getEmailUpdater().token());
            entity.setEmailUpdaterexpiration(domain.getEmailUpdater().expiration());
        }
    }


    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                new Password(entity.getPassword()),
                new Email(entity.getEmail()),
                entity.getRole(),
                entity.getStatus(),
                mapPasswordToken(entity),
                mapEmailToken(entity),
                mapTwoFA(entity),
                new Username(entity.getUsername()),
                entity.getPhotoUrl(),
                new Description(entity.getDescription()),
                new FavoriteGames(entity.getFavoriteGames() != null ? entity.getFavoriteGames() : new ArrayList<>()),
                new Connections(entity.getFriends() != null ? entity.getFriends() : new ArrayList<>()),
                new Connections(entity.getFollowers() != null ? entity.getFollowers() : new ArrayList<>())
        );
    }

    public Moderator toDomain(ModeratorEntity entity) {
        return new Moderator(
                entity.getId(),
                new Password(entity.getPassword()),
                new Email(entity.getEmail()),
                entity.getRole(),
                entity.getStatus(),
                mapPasswordToken(entity),
                mapEmailToken(entity),
                mapTwoFA(entity)
        );
    }

    private TokenUpdater mapPasswordToken(BaseEntity e) {
        return new TokenUpdater(e.getPasswordUpdaterToken(), e.getPasswordUpdaterexpiration());
    }

    private TokenUpdater mapEmailToken(BaseEntity e) {
        return new TokenUpdater(e.getEmailUpdaterToken(), e.getEmailUpdaterexpiration());
    }

    private TwoFA mapTwoFA(BaseEntity e) {
        return new TwoFA(e.isEnabled2Fa(), e.getSecret2Fa());
    }
}

