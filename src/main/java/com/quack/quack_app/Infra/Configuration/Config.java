package com.quack.quack_app.Infra.Configuration;

import com.quack.quack_app.Application.Mappers.Games.GameMapper;
import com.quack.quack_app.Application.Mappers.Reviews.ReviewMapper;
import com.quack.quack_app.Application.Mappers.Users.ModeratorMapper;
import com.quack.quack_app.Application.Mappers.Users.UserMapper;
import com.quack.quack_app.Application.Ports.Output.Repositories.GameRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Application.UseCases.Games.GetGameByIdUseCase;
import com.quack.quack_app.Application.UseCases.Games.GetGamesUseCase;
import com.quack.quack_app.Application.UseCases.Games.SaveGameUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.DeleteReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.SaveReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.UpdateRatingReviewUseCase;
import com.quack.quack_app.Application.UseCases.Reviews.UpdateReviewUseCase;
import com.quack.quack_app.Application.UseCases.Services.Review.ValidateReviewService;
import com.quack.quack_app.Application.UseCases.Users.Moderators.SaveModeratorUseCase;
import com.quack.quack_app.Application.UseCases.Users.Users.*;
import com.quack.quack_app.Infra.Adapters.Output.Gateways.EmailGateway.EmailServiceAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Gateways.TwoFAGateway.TwoFAServiceAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Adapters.GameRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Adapters.ReviewRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Mappers.NoSQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories.MongoGameRepository;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.NoSQL.Repositories.MongoReviewRepository;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Adapters.ModeratorRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Adapters.UserRepositoryAdapter;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers.SQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaModeratorRepository;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaUserRepository;
import com.quack.quack_app.Infra.Security.Service.AesEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class Config {

    @Bean
    UserRepository userRepository(JpaUserRepository jpaUserRepository, AesEncryptor encryptor, SQLMapper sqlMapper) {
        return new UserRepositoryAdapter(jpaUserRepository, encryptor, sqlMapper);
    }
    @Bean
    ModeratorRepository moderatorRepository(JpaModeratorRepository jpaModeratorRepository, SQLMapper sqlMapper) {
        return new ModeratorRepositoryAdapter(jpaModeratorRepository, sqlMapper);
    }
    @Bean
    GameRepository gameRepository(MongoGameRepository mongoGameRepository, NoSQLMapper noSQLMapper, MongoTemplate mongoTemplate) {
        return new GameRepositoryAdapter(mongoGameRepository, noSQLMapper, mongoTemplate);
    }
    @Bean
    ReviewRepository reviewRepository(MongoReviewRepository mongoReviewRepository, NoSQLMapper noSQLMapper, MongoTemplate mongoTemplate) {
        return new ReviewRepositoryAdapter(mongoReviewRepository, noSQLMapper, mongoTemplate);
    }
    @Bean
    TwoFAService twoFAService(){
        return new TwoFAServiceAdapter();
    }
    @Bean
    EmailService emailService(JavaMailSender mailSender){
        return new EmailServiceAdapter(mailSender);
    }
    @Bean
    UserMapper userMapper(){
        return new UserMapper();
    }
    @Bean
    ModeratorMapper moderatorMapper(){
        return new ModeratorMapper();
    }
    @Bean
    GameMapper gameMapper(){
        return new GameMapper();
    }
    @Bean
    ReviewMapper reviewMapper(){
        return new ReviewMapper();
    }
    @Bean
    GetGameByIdUseCase getGameByIdUseCase(GameRepository gameRepository){
        return new GetGameByIdUseCase(gameRepository);
    }
    @Bean
    GetGamesUseCase getGamesUseCase(GameRepository gameRepository){
        return new GetGamesUseCase(gameRepository);
    }
    @Bean
    SaveGameUseCase saveGameUseCase(GameRepository gameRepository, GameMapper gameMapper){
        return new SaveGameUseCase(gameMapper, gameRepository);
    }
    @Bean
    DeleteReviewUseCase deleteReviewUseCase(ReviewRepository reviewRepository){
        return new DeleteReviewUseCase(reviewRepository);
    }
    @Bean
    SaveReviewUseCase saveReviewUseCase(ReviewRepository reviewRepository, GameRepository gameRepository, UserRepository userRepository, ReviewMapper reviewMapper){
        return new SaveReviewUseCase(reviewRepository, gameRepository, userRepository, reviewMapper);
    }
    @Bean
    ValidateReviewService validateReviewService(ReviewRepository reviewRepository, UserRepository userRepository){
        return new ValidateReviewService(reviewRepository, userRepository);
    }
    @Bean
    UpdateRatingReviewUseCase updateRatingReviewUseCase(ReviewRepository reviewRepository, ValidateReviewService validateReviewService){
        return new UpdateRatingReviewUseCase(reviewRepository, validateReviewService);
    }
    @Bean
    UpdateReviewUseCase updateReviewUseCase(ReviewRepository reviewRepository, ValidateReviewService validateReviewService){
        return new UpdateReviewUseCase(reviewRepository, validateReviewService);
    }
    @Bean
    ActivateUseCase activateUseCase(UserRepository userRepository){
        return new ActivateUseCase(userRepository);
    }
    @Bean
    BanUseCase banUseCase(UserRepository userRepository, ReviewRepository reviewRepository){
        return new BanUseCase(userRepository, reviewRepository);
    }
    @Bean
    ChangeDescriptionUseCase changeDescriptionUseCase(UserRepository userRepository){
        return new ChangeDescriptionUseCase(userRepository);
    }
    @Bean
    ChangeEmailUseCase changeEmailUseCase(UserRepository userRepository){
        return new ChangeEmailUseCase(userRepository);
    }
    @Bean
    ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository){
        return new ChangePasswordUseCase(userRepository);
    }
    @Bean
    ChangeUsernameUseCase changeUsernameUseCase(UserRepository userRepository){
        return new ChangeUsernameUseCase(userRepository);
    }
    @Bean
    ChangeProfilePhotoUseCase changeProfilePhotoUseCase(UserRepository userRepository){
        return new ChangeProfilePhotoUseCase(userRepository);
    }
    @Bean
    Check2FAUseCase check2FAUseCase(UserRepository userRepository, TwoFAService twoFAService){
        return new Check2FAUseCase(userRepository, twoFAService);
    }
    @Bean
    FollowUseCase followUseCase(UserRepository userRepository){
        return new FollowUseCase(userRepository);
    }
    @Bean
    GetUserReviewsUseCase getUserReviewsUseCase(UserRepository userRepository, ReviewRepository reviewRepository, ReviewMapper reviewMapper){
        return new GetUserReviewsUseCase(userRepository, reviewRepository, reviewMapper);
    }
    @Bean
    GetUserUseCase getUserUseCase(UserRepository userRepository, ReviewRepository reviewRepository,UserMapper userMapper, ReviewMapper reviewMapper){
        return new GetUserUseCase(userRepository, reviewRepository, userMapper, reviewMapper);
    }
    @Bean
    SaveUserUseCase saveUserUseCase(UserRepository userRepository, UserMapper userMapper, TwoFAService twoFAService, EmailService emailService){
        return new SaveUserUseCase(userRepository, userMapper, twoFAService, emailService);
    }
    @Bean
    SearchUsersUseCase searchUsersUseCase(UserRepository userRepository, ReviewRepository reviewRepository, UserMapper userMapper, ReviewMapper reviewMapper){
        return new SearchUsersUseCase(userRepository, reviewRepository, userMapper, reviewMapper);
    }
    @Bean
    StartChangeEmailUseCase startChangeEmailUseCase(UserRepository userRepository, EmailService emailService){
        return new StartChangeEmailUseCase(userRepository, emailService);
    }
    @Bean
    StartChangePasswordUseCase startChangePasswordUseCase(UserRepository userRepository, EmailService emailService) {
        return new StartChangePasswordUseCase(userRepository, emailService);
    }
    @Bean
    UnfollowUseCase unfollowUseCase(UserRepository userRepository){
        return new UnfollowUseCase(userRepository);
    }
    @Bean
    com.quack.quack_app.Application.UseCases.Users.Moderators.ActivateUseCase activateModeratorUseCase(ModeratorRepository moderatorRepository){
        return new com.quack.quack_app.Application.UseCases.Users.Moderators.ActivateUseCase(moderatorRepository);
    }
    @Bean
    com.quack.quack_app.Application.UseCases.Users.Moderators.BanUseCase banModeratorUseCase(ModeratorRepository moderatorRepository){
        return new com.quack.quack_app.Application.UseCases.Users.Moderators.BanUseCase(moderatorRepository);
    }
    @Bean
    com.quack.quack_app.Application.UseCases.Users.Moderators.StartChangeEmailUseCase startChangeEmailModeratorUseCase(ModeratorRepository moderatorRepository, EmailService emailService){
        return new com.quack.quack_app.Application.UseCases.Users.Moderators.StartChangeEmailUseCase(moderatorRepository, emailService);
    }
    @Bean
    com.quack.quack_app.Application.UseCases.Users.Moderators.StartChangePasswordUseCase startChangePasswordModeratorUseCase(ModeratorRepository moderatorRepository, EmailService emailService) {
        return new com.quack.quack_app.Application.UseCases.Users.Moderators.StartChangePasswordUseCase(moderatorRepository, emailService);
    }
    @Bean
    com.quack.quack_app.Application.UseCases.Users.Moderators.ChangeEmailUseCase changeEmailModeratorUseCase(ModeratorRepository moderatorRepository){
        return new com.quack.quack_app.Application.UseCases.Users.Moderators.ChangeEmailUseCase(moderatorRepository);
    }
    @Bean
    com.quack.quack_app.Application.UseCases.Users.Moderators.ChangePasswordUseCase changePasswordModeratorUseCase(ModeratorRepository moderatorRepository){
        return new com.quack.quack_app.Application.UseCases.Users.Moderators.ChangePasswordUseCase(moderatorRepository);
    }
    @Bean
    com.quack.quack_app.Application.UseCases.Users.Moderators.Check2FAUseCase check2FAModeratorUseCase(ModeratorRepository moderatorRepository, TwoFAService twoFAService){
        return new com.quack.quack_app.Application.UseCases.Users.Moderators.Check2FAUseCase(moderatorRepository, twoFAService);
    }
    @Bean
    SaveModeratorUseCase saveModeratorUseCase(ModeratorRepository moderatorRepository, ModeratorMapper moderatorMapper, TwoFAService twoFAService, EmailService emailService){
        return new SaveModeratorUseCase(moderatorRepository, moderatorMapper, twoFAService, emailService);
    }
}
