package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Adapters;

import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers.SQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaModeratorRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ModeratorRepositoryAdapter implements ModeratorRepository {

    private final JpaModeratorRepository jpaRepository;
    private final SQLMapper mapper;

    public ModeratorRepositoryAdapter(JpaModeratorRepository jpaRepository, SQLMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @CachePut(value = "moderators", key = "#moderator.id", unless = "#result == null")
    public void saveModerator(Moderator moderator) {
        jpaRepository.save(mapper.toEntity(moderator));
    }

    @Override
    @Cacheable(value = "moderators", key = "#id")
    public Optional<Moderator> getModeratorById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Cacheable(value = "moderators", key = "#email.email()", unless = "#result == null")
    public Optional<Moderator> getModeratorByEmail(Email email) {
        return jpaRepository.findByEmail(email.email())
                .map(mapper::toDomain);
    }
}