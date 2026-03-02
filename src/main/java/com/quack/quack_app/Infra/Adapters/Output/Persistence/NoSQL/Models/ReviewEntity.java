package com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Models;

import com.quack.quack_app.Domain.Reviews.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {

    @MongoId
    private UUID id;
    private UUID userId;
    private UUID gameId;
    private LocalDate reviewDate;
    private Status status;
    private BigDecimal rating;
    private String review;
}
