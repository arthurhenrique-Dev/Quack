package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Adapters;

import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers.SQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaModeratorRepository;
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
    public void saveModerator(Moderator moderator) {
        jpaRepository.save(mapper.toEntity(moderator));
    }

    @Override
    public Optional<Moderator> getModeratorById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Moderator> getModeratorByEmail(Email email) {
        return jpaRepository.findByEmail(email.email())
                .map(mapper::toDomain);
    }
}