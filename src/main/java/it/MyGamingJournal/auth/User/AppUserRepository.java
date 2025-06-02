package it.MyGamingJournal.auth.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.gameEntries WHERE u.id = :id")
    Optional<AppUser> findByIdWithGameEntries(@Param("id") Long id);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.friends WHERE u.id = :id")
    Optional<AppUser> findByIdWithFriends(@Param("id") Long id);

}
