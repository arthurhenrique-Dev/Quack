package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories;

import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models.ModeratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaModeratorRepository extends JpaRepository<ModeratorEntity, UUID> {

    @Query("SELECT m FROM ModeratorEntity m WHERE m.email = :encryptedEmail")
    Optional<ModeratorEntity> findByEmail(@Param("encryptedEmail") String encryptedEmail);
}
