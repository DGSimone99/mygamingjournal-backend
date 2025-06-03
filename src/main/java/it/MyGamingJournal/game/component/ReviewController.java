package it.MyGamingJournal.game.component;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    public void saveReview(@AuthenticationPrincipal User user, long gameId, ReviewRequest reviewRequest) {
        reviewService.saveReview(reviewRequest, gameId, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@AuthenticationPrincipal User user, @PathVariable long id) {
        reviewService.deleteReview(id, user);
    }

    @GetMapping("/game/{gameId}/")
    public Page<ReviewResponse> getReviewsByGame(@PathVariable Long gameId, @PageableDefault(page = 0, size = 10)  Pageable pageable) {
        return reviewRepository.findByGameId(gameId, pageable)
                .map(reviewService::toResponse);
    }

    @GetMapping("/user/{userId}/")
    public Page<ReviewResponse> getReviewsByUser(@PathVariable Long userId, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(reviewService::toResponse);
    }

    @GetMapping("/me")
    public Page<ReviewResponse> getMyReviews(@AuthenticationPrincipal User user, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return reviewRepository.findByUserId(user.getId(), pageable)
                .map(reviewService::toResponse);
    }
}
