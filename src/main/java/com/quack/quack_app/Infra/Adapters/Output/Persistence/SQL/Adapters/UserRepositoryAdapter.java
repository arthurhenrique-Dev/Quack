package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Adapters;

import com.quack.quack_app.Application.DTOs.Users.DTOSearchUser;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Natural;
import com.quack.quack_app.Domain.ValueObjects.Username;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers.SQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaUserRepository;
import com.quack.quack_app.Infra.Security.Service.AesEncryptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;
    private final AesEncryptor aesEncryptor;
    private final SQLMapper mapper;

    public UserRepositoryAdapter(JpaUserRepository jpaRepository, AesEncryptor aesEncryptor, SQLMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.aesEncryptor = aesEncryptor;
        this.mapper = mapper;
    }

    @Override
    public void saveUser(User user) {
        jpaRepository.save(mapper.toEntity(user));
    }

    @Override
    public List<User> getUsers(DTOSearchUser dtoSearchUser, Natural pages, Natural size) {
        Pageable pageable = PageRequest.of(pages.i(), size.i());

        UUID idParam = dtoSearchUser.id();

        String usernameParam = (dtoSearchUser.usename() != null) ? dtoSearchUser.usename() : null;

        return jpaRepository.searchUsers(idParam, usernameParam, pageable)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> getUserByName(Username username) {
        return jpaRepository.findByUsername(username.username()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> getUserByEmail(Email email) {
        return jpaRepository.findByEmail(email.email())
                .map(mapper::toDomain);
    }
}
