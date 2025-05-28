package it.MyGamingJournal.game.component;


import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByGameAndUser(Game game, AppUser user);

    Page<Review>  findByGameId(Long gameId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
}