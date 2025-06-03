package it.MyGamingJournal.game.component.review;

import it.MyGamingJournal.auth.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/game/{gameId}")
    public Page<ReviewResponse> getReviewsByGame(
            @PathVariable Long gameId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return reviewService.getReviewsByGame(gameId, pageable);
    }

    @GetMapping("/user/{userId}")
    public Page<ReviewResponse> getReviewsByUser(
            @PathVariable Long userId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return reviewService.getReviewsByUser(userId, pageable);
    }

    @GetMapping("/me")
    public Page<ReviewResponse> getMyReviews(
            @AuthenticationPrincipal User user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return reviewService.getReviewsByUser(user.getId(), pageable);
    }

    @PostMapping
    public void saveReview(
            @AuthenticationPrincipal User user,
            @RequestParam Long gameId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ) {
        reviewService.saveReview(reviewRequest, gameId, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@AuthenticationPrincipal User user, @PathVariable Long id) {
        reviewService.deleteReview(id, user);
    }
}