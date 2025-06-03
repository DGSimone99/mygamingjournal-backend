package it.MyGamingJournal.game.component.review;


import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByGameAndUser(Game game, User user);

    Page<Review>  findByGameId(Long gameId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
}