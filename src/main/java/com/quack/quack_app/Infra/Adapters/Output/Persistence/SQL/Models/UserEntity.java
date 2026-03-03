package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity{

    @Column(name = "username", columnDefinition = "TEXT")
    private String username;
    private String photoUrl;
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UUID> favoriteGames;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UUID> friends;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UUID> followers;
}
