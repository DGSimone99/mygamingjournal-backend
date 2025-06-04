package it.MyGamingJournal.game.component.game;


import it.MyGamingJournal.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g JOIN g.genres genre WHERE LOWER(genre) = LOWER(:genre)")
    Page<Game> findByGenre(@Param("genre") String genre, Pageable pageable);

    Page<Game> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT g FROM Game g JOIN g.developers developer WHERE LOWER(developer) LIKE LOWER(CONCAT('%', :developer, '%'))")
    Page<Game> findByDeveloper(@Param("developer") String developer, Pageable pageable);

    @Query("SELECT g FROM Game g JOIN g.tags tag WHERE LOWER(tag) LIKE LOWER(CONCAT('%', :tag, '%'))")
    Page<Game> findByTag(@Param("tag") String tag, Pageable pageable);

    @Query("""
    SELECT g FROM Game g
    LEFT JOIN g.developers dev
    LEFT JOIN g.publishers pub
    LEFT JOIN g.genres genre
    WHERE LOWER(g.name) LIKE CONCAT('%', LOWER(:query), '%')
       OR LOWER(dev) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(pub) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(genre) LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY
       CASE
           WHEN LOWER(g.name) LIKE CONCAT('%', LOWER(:query), '%') THEN 1
           WHEN LOWER(dev) LIKE CONCAT('%', LOWER(:query), '%') THEN 2
           WHEN LOWER(pub) LIKE CONCAT('%', LOWER(:query), '%') THEN 3
           WHEN LOWER(genre) LIKE CONCAT('%', LOWER(:query), '%') THEN 4
           ELSE 5
       END
    """)
    Page<Game> findByAllFields(@Param("query") String query, Pageable pageable);

    @Modifying
    @Query("UPDATE Game g SET g.added = :added, g.rating = :rating WHERE g.id = :id")
    void updateStatsById(@Param("id") Long id,
                         @Param("added") int added,
                         @Param("rating") double rating);

    @Query("SELECT g FROM Game g WHERE g.released > CURRENT_DATE ORDER BY g.released ASC")
    Page<Game> findByReleasedAfterNowOrderByReleasedAsc(Pageable pageable);

}