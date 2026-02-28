package com.quack.quack_app.Domain.Games;

import com.quack.quack_app.Domain.ValueObjects.Rating;
import com.quack.quack_app.Domain.ValueObjects.Reviews;

import java.time.LocalDate;
import java.util.UUID;

public class Game {

    private UUID id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String genre;
    private String developer;
    private String publisher;
    private String photoUrl;
    private String platforms;
    private Rating rating;
    private Reviews reviews;

    public Game(UUID id, String name, String description, LocalDate releaseDate, String genre, String developer, String publisher, String photoUrl, String platforms, Rating rating, Reviews reviews) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.photoUrl = photoUrl;
        this.platforms = platforms;
        this.rating = rating;
        this.reviews = reviews;
    }

    public Game(String name, String description, LocalDate releaseDate, String genre, String developer, String publisher, String photoUrl, String platforms) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.photoUrl = photoUrl;
        this.platforms = platforms;
        this.reviews = Reviews.Start();
        this.rating = this.reviews.actualRating();
    }

    public void updateRating(){
        this.rating = this.reviews.actualRating();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPlatforms() {
        return platforms;
    }

    public Rating getRating() {
        return rating;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public Reviews getNormalReviews(){
        return reviews.getActiveReviews();
    }
}
