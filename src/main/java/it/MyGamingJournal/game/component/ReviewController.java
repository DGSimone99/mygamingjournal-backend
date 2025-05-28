package it.MyGamingJournal.game.component;

import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.auth.User.AppUserService;
import it.MyGamingJournal.game.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    public void saveReview( @AuthenticationPrincipal AppUser user, long gameId, ReviewRequest reviewRequest) {
        reviewService.saveReview(reviewRequest, gameId, user.getId());
    }

    @PutMapping
    public void updateReview( @AuthenticationPrincipal AppUser user, long id, ReviewRequest reviewRequest) {
        reviewService.updateReview(reviewRequest, id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@AuthenticationPrincipal AppUser user, @PathVariable long id) {
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
    public Page<ReviewResponse> getMyReviews(@AuthenticationPrincipal AppUser user, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return reviewRepository.findByUserId(user.getId(), pageable)
                .map(reviewService::toResponse);
    }
}
