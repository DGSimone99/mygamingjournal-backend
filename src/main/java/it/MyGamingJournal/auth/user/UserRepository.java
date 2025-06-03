package it.MyGamingJournal.auth.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.gameEntries WHERE u.id = :id")
    Optional<User> findByIdWithGameEntries(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.friends WHERE u.id = :id")
    Optional<User> findByIdWithFriends(@Param("id") Long id);

    @Query("SELECT f FROM User u JOIN u.friends f WHERE u.id = :userId AND " +
            "(LOWER(f.username) LIKE %:query% OR LOWER(f.displayName) LIKE %:query%)")
    Page<User> findFriendsByUserId(@Param("userId") Long userId,
                                   @Param("query") String query,
                                   Pageable pageable);

    @Query("FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.displayName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<User> searchByUsernameOrDisplayName(@Param("query") String query, Pageable pageable);


}
