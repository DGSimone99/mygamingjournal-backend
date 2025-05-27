package it.MyGamingJournal.game.component;

import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.auth.User.AppUserService;
import it.MyGamingJournal.game.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private GameService gameService;

    @PostMapping
    public void saveReview( @AuthenticationPrincipal AppUser user, long gameId, Review review) {
        reviewService.saveReview(review, gameId, user.getId());
    }

    @PutMapping
    public void updateReview( @AuthenticationPrincipal AppUser user, long id, ReviewRequest reviewRequest) {
        reviewService.updateReview(reviewRequest, id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@AuthenticationPrincipal AppUser user, @PathVariable long id) {
        reviewService.deleteReview(id, user);
    }
}
