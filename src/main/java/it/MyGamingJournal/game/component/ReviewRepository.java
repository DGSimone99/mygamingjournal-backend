package it.MyGamingJournal.game.component;


import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByGameAndUser(Game game, AppUser user);
}