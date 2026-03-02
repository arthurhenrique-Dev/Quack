package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models;

import com.quack.quack_app.Domain.Users.Role;
import com.quack.quack_app.Domain.Users.Status;

import com.quack.quack_app.Infra.Security.Service.AesEncryptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    private UUID id;
    private String password;
    @Convert(converter = AesEncryptor.class)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String passwordUpdaterToken;
    private LocalDateTime passwordUpdaterexpiration;
    private String emailUpdaterToken;
    private LocalDateTime emailUpdaterexpiration;
    private boolean enabled2Fa;
    @Convert(converter = AesEncryptor.class)
    private String secret2Fa;
}
