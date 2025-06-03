package it.MyGamingJournal.game.component.game;


import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.game.entity.Game;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findAchievementsByGame(Game game, Sort sort);
}