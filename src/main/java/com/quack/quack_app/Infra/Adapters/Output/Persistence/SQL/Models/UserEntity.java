package com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    private String username;
    private String photoUrl;
    private String description;
    @ElementCollection
    private List<UUID> favoriteGames;
    @ElementCollection
    private List<UUID> friends;
    @ElementCollection
    private List<UUID> followers;
}
