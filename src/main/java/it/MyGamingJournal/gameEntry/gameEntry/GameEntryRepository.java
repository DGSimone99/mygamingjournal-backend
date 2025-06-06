package it.MyGamingJournal.gameEntry.gameEntry;


import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface GameEntryRepository extends JpaRepository<GameEntry, Long> {
    List<GameEntry> findByUser(User user);

    @Query("""
    SELECT e FROM GameEntry e
    WHERE e.availableToPlay = true
      AND ( :lang IS NULL OR :lang MEMBER OF e.availableLanguages OR e.availableLanguages IS EMPTY )
      AND e.availableUntil >= CURRENT_DATE
    """)
    List<GameEntry> findAvailableEntries(@Param("lang") String lang);

    List<GameEntry> findByAvailableToPlayTrueAndAvailableUntilBefore(LocalDate date);

    GameEntry findByUserAndGameId(User user, long idGame);

    GameEntry findByUserAndId(User user, long id);

    @Query("SELECT ge FROM GameEntry ge LEFT JOIN FETCH ge.achievements WHERE ge IN :entries")
    List<GameEntry> findAllWithAchievements(@Param("entries") List<GameEntry> entries);

    @Query("SELECT COUNT(ge) FROM GameEntry ge WHERE ge.game = :game")
    int countByGame(@Param("game") Game game);

    @Query("SELECT AVG(ge.personalRating) FROM GameEntry ge WHERE ge.game = :game  AND ge.personalRating > 0")
    Double averageRatingByGame(@Param("game") Game game);

    @Query("""
    SELECT ge
    FROM GameEntry ge
    WHERE ge.availableToPlay = true
      AND ge.game.id = :realGameId
      AND (
           :languages IS NULL OR EXISTS (
               SELECT 1 FROM GameEntry ge2 JOIN ge2.availableLanguages lang
               WHERE ge2.id = ge.id AND lang IN :languages
           )
      )
      AND (
           :platforms IS NULL OR EXISTS (
               SELECT 1 FROM GameEntry ge3 JOIN ge3.availablePlatforms plat
               WHERE ge3.id = ge.id AND plat IN :platforms
           )
      )
    """)
    Page<GameEntry> findAvailablePlayers(
            @Param("realGameId") Long realGameId,
            @Param("languages") Set<String> languages,
            @Param("platforms") Set<String> platforms,
            Pageable pageable
    );

    @Query("SELECT g.game.id FROM GameEntry g WHERE g.user = :user")
    List<Long> findAllGameIdsByUser(@Param("user") User user);

    GameEntry findTopByUserIdOrderByAddedAtDesc(Long userId);

    GameEntry findTopByUserIdOrderByHoursPlayedDesc(Long userId);

}