package it.MyGamingJournal.auth.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_languages", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "language_code")
    private List<String> languages = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<it.MyGamingJournal.game.entity.Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<it.MyGamingJournal.gameEntry.gameEntry.GameEntry> gameEntries = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    private int experience = 0;
    private int level = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "steam_username", length = 100)
    private String steamUsername;

    @Column(name = "psn_username", length = 100)
    private String psnUsername;

    @Column(name = "xbox_username", length = 100)
    private String xboxUsername;

    @Column(name = "nintendo_username", length = 100)
    private String nintendoUsername;

    @Column(name = "epic_username", length = 100)
    private String epicUsername;

    @Column(name = "riot_id", length = 100)
    private String riotId;

    @Column(name = "discord_tag", length = 100)
    private String discordTag;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


    // 👇 Metodi statistici: da spostare in un service, tenuti per ora come riferimento

//    public int getTotalGames() {
//        return gameEntries.size();
//    }
//
//    public double getTotalHoursPlayed() {
//        return Math.round(
//                gameEntries.stream()
//                        .mapToDouble(GameEntry::getHoursPlayed)
//                        .sum() * 10.0
//        ) / 10.0;
//    }
//
//    public long getCompletedGamesCount() {
//        return gameEntries.stream()
//                .filter(entry -> entry.getStatus() == GameStatus.COMPLETED)
//                .count();
//    }
//
//    public long getWishlistedGamesCount() {
//        return gameEntries.stream()
//                .filter(entry -> entry.getStatus() == GameStatus.WISHLIST)
//                .count();
//    }
//
//    public long getUnlockedAchievements() {
//        return gameEntries.stream()
//                .flatMap(entry -> entry.getAchievements().stream())
//                .filter(AchievementEntry::isUnlocked)
//                .count();
//    }
