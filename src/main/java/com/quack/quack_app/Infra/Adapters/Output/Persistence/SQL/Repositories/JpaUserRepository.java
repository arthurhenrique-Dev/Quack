package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories;

import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    @Query(value = """
    SELECT * FROM users u
    WHERE (:id IS NULL OR u.id = CAST(:id AS uuid))
    AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', CAST(:username AS text), '%')))
    """, nativeQuery = true)
    List<UserEntity> searchUsers(
            @Param("id") UUID id,
            @Param("username") String username,
            Pageable pageable
    );

    @Query("SELECT u FROM UserEntity u WHERE u.email = :encryptedEmail")
    Optional<UserEntity> findByEmail(@Param("encryptedEmail") String encryptedEmail);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);
}
