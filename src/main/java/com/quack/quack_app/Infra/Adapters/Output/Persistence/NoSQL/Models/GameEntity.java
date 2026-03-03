package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models;

import com.quack.quack_app.Domain.ValueObjects.Reviews;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Document(collection = "games")
@Getter
@Setter
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
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal rating;
    @Field("reviewsList")
    private List<ReviewEntity> reviewsList;

    public GameEntity(UUID id, BigDecimal rating, String platforms, String photoUrl, String publisher, String developer, LocalDate releaseDate, String genre, String description, String name) {
        this.id = id;
        this.rating = rating;
        this.platforms = platforms;
        this.photoUrl = photoUrl;
        this.publisher = publisher;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.description = description;
        this.name = name;
    }
}
