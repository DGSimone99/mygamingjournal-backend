package it.MyGamingJournal.game.component.review;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.UserService;
import it.MyGamingJournal.game.component.game.GameService;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.entity.Review;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GameService gameService;
    private final UserService userService;

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
    }

    public void saveReview(ReviewRequest reviewRequest, Long gameId, Long userId) {
        Game game = gameService.getDetailsGame(gameId);
        User user = userService.findUserById(userId);

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

    public void deleteReview(Long id, User user) {
        Review review = findById(id);
        if (!review.getUser().equals(user)) {
            throw new AccessDeniedException("You can't delete a review you didn't write");
        }
        reviewRepository.deleteById(id);
    }

    public Page<ReviewResponse> getReviewsByGame(Long gameId, Pageable pageable) {
        return reviewRepository.findByGameId(gameId, pageable).map(this::toResponse);
    }

    public Page<ReviewResponse> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable).map(this::toResponse);
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
