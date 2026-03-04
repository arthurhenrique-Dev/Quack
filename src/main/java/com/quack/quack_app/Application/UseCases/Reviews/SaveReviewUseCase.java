package com.quack.quack_app.Application.UseCases.Reviews;

import com.quack.quack_app.Application.DTOs.Reviews.DTOSaveReview;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Ports.Input.Reviews.SaveReviewPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.UseCases.Services.TryGetService;
import com.quack.quack_app.Application.UseCases.Services.TrySaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveReviewUseCase implements SaveReviewPort {

    private static final Logger logger = LoggerFactory.getLogger(SaveReviewUseCase.class);
    private final ReviewRepository repository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ReviewMapper mapper;
    private final EmailService emailService;

    public SaveReviewUseCase(ReviewRepository repository, GameRepository gameRepository, UserRepository userRepository, ReviewMapper mapper, EmailService emailService) {
        this.repository = repository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.emailService = emailService;
    }

    @Override
    public void saveReviews(DTOSaveReview review) {
        var userTry = TryGetService.execute(()-> userRepository.getUserById(review.userId()), logger);
        var gameTry = TryGetService.execute(()-> gameRepository.getGameById(review.gameId()), logger);
        var reviewReady = mapper.toRegister(review);
        var user = userTry.get();
        var game = gameTry.get();
        game.addReview(reviewReady);
        game.updateRating();
        emailService.send("your review for " + game.getName() + " has sucessfuly been added", user.getEmail(), "your review");
        TrySaveService.execute(reviewReady, repository::saveReview, logger);
        TrySaveService.execute(game, gameRepository::saveGame, logger);
    }
}
