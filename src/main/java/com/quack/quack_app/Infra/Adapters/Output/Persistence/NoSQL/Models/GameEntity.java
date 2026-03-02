package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models;

import com.quack.quack_app.Domain.ValueObjects.Reviews;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "games")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity {

    @MongoId
    private UUID id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String genre;
    private String developer;
    private String publisher;
    private String photoUrl;
    private String platforms;
    private BigDecimal rating;
    private Reviews reviews;
}
