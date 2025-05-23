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

    int countByGame(Game game);

    @Query("SELECT AVG(e.personalRating) FROM GameEntry e WHERE e.game = :game AND e.personalRating > 0")
    Double findAverageRatingByGame(@Param("game") Game game);


    GameEntry findByUserAndGameId(AppUser user, long idGame);
}