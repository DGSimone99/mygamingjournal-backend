package it.MyGamingJournal.gameEntry.gameEntry;


import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GameEntryRepository extends JpaRepository<GameEntry, Long> {
    List<GameEntry> findByUser(AppUser user);

    @Query("""
    SELECT e FROM GameEntry e
    WHERE e.availableToPlay = true
      AND ( :lang IS NULL OR :lang MEMBER OF e.availableLanguages OR e.availableLanguages IS EMPTY )
      AND e.availableUntil >= CURRENT_DATE
    """)
    List<GameEntry> findAvailableEntries(@Param("lang") String lang);

    List<GameEntry> findByAvailableToPlayTrueAndAvailableUntilBefore(LocalDate date);

    GameEntry findByUserAndGameId(AppUser user, long idGame);

    GameEntry findByUserAndId(AppUser user, long id);

    @Query("SELECT ge FROM GameEntry ge LEFT JOIN FETCH ge.achievements WHERE ge IN :entries")
    List<GameEntry> findAllWithAchievements(@Param("entries") List<GameEntry> entries);

    @Query("SELECT COUNT(ge) FROM GameEntry ge WHERE ge.game = :game")
    int countByGame(@Param("game") Game game);

    @Query("SELECT AVG(ge.personalRating) FROM GameEntry ge WHERE ge.game = :game  AND ge.personalRating > 0")
    Double averageRatingByGame(@Param("game") Game game);
}