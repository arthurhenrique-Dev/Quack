package com.quack.quack_app.Application.UseCases.Users.Users;

import com.quack.quack_app.Application.Ports.Input.Users.BanPort;
import com.quack.quack_app.Application.Ports.Output.Repositories.ReviewRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Application.UseCases.Services.User.BanimentService;
import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class BanUseCase implements BanPort {

    private static final Logger log = LoggerFactory.getLogger(BanUseCase.class);

    private final UserRepository repository;
    private final ReviewRepository reviewRepository;

    public BanUseCase(UserRepository repository, ReviewRepository reviewRepository) {
        this.repository = repository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void ban(UUID id) {

        BanimentService.execute(
                ()-> repository.getUserById(id),
                ()-> new UserNotFoundException(),
                user -> repository.saveUser(user),
                log
        );
        try {
            reviewRepository.getReviews(id)
                    .filterActiveReviews()
                    .reviews()
                    .forEach(review -> {
                        review.removeReview();
                        reviewRepository.saveReview(review);
                    });
        } catch (Exception e) {
            var pe = new ProcessingErrorException();
            log.error(pe.getMessage(), e);
            throw pe;
        }

    }
}
