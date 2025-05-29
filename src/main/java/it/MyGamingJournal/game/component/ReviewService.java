package it.MyGamingJournal.game.component;

import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.auth.User.AppUserService;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.entity.Review;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private AppUserService appUserService;

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void saveReview(ReviewRequest reviewRequest, Long gameId, Long userId) {
        Game game = gameService.getDetailsGame(gameId);
        AppUser user = appUserService.findById(userId);

        if (reviewRepository.existsByGameAndUser(game, user)) {
            throw new IllegalArgumentException("You already reviewed this game");
        }

        if (reviewRequest.getScore() < 1 || reviewRequest.getScore() > 10) {
            throw new IllegalArgumentException("Score must be between 1 and 10");
        }
        if (reviewRequest.getText() == null || reviewRequest.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Review text cannot be empty");
        }

        Review review = new Review();
        review.setText(reviewRequest.getText());
        review.setScore(reviewRequest.getScore());
        review.setDate(LocalDate.now());
        review.setGame(game);
        review.setUser(user);
        reviewRepository.save(review);
    }

    public void deleteReview(Long id, AppUser user) {
        Review review = findById(id);
        if (!review.getUser().equals(user)) {
            throw new AccessDeniedException("You can't delete a review you didn't write");
        }
        reviewRepository.deleteById(id);
    }

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getText(),
                review.getScore(),
                review.getDate(),
                new ReviewResponse.Author(
                        review.getUser().getId(),
                        review.getUser().getUsername(),
                        review.getUser().getAvatarUrl(),
                        review.getUser().getDisplayName()
                ),
                new ReviewResponse.Game(
                        review.getGame().getId(),
                        review.getGame().getName(),
                        review.getGame().getBackgroundImage()
                )
        );
    }
}
